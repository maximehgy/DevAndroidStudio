package com.hema.todo.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.audiofx.BassBoost
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.hema.todo.R
import com.hema.todo.databinding.ActivityFormBinding
import com.hema.todo.databinding.ActivityUserInfoBinding
import com.hema.todo.databinding.FragmentTaskListBinding
import com.hema.todo.network.Api.userWebService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class UserInfoActivity : AppCompatActivity() {

    lateinit var binding : ActivityUserInfoBinding
    val mediaStore by lazy { MediaStoreRepository(this) }
    private lateinit var photoUri: Uri
    private val viewModel = UserInfoViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.takePictureButton.setOnClickListener {
            launchCameraWithPermission()
        }
        binding.uploadImageButton.setOnClickListener {
            gallerylauncher.launch("image/*")
        }
        lifecycleScope.launch {
            val userInfo = viewModel.userInfo.collect { userInfo ->
                if (userInfo != null) {
                    if (userInfo.avatar != null) {
                        binding.imageView.load(userInfo.avatar)
                    } else error(R.drawable.ic_launcher_background)
                }
            }
        }
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
        val view = binding.root
            if (accepted) handleImage()
            else Snackbar.make(view, "Échec!", Snackbar.LENGTH_LONG).show()
    }

    private val permissionAndCameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        }

    private val gallerylauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri
            handleImage()
        }
    }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        when {
            mediaStore.canWriteSharedEntries() && isAlreadyAccepted -> launchCamera()
            isExplanationNeeded -> showExplanation()
            else -> permissionAndCameraLauncher.launch(arrayOf(camPermission, storagePermission))
        }
    }

    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(this)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les paramètres de l'app (pour modifier les permissions déjà refusées par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        // ici pas besoin de vérifier avant car on vise un écran système:
        startActivity(intent)
    }

    private fun handleImage() {
        binding.imageView.load(photoUri)
        viewModel.editAvatar(convert(photoUri))
    }

    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )
    }

    private fun launchCamera() {
        lifecycleScope.launch {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
            cameraLauncher.launch(photoUri)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

}