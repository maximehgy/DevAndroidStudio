package com.hema.todo.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.hema.todo.databinding.ActivityUserInfoBinding
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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
            viewModel.userInfo.collect { userInfo ->
                if (userInfo != null) {
                    if (userInfo.avatar != null) {
                        binding.imageView.load(userInfo.avatar)
                    } else binding.imageView.load("https://goo.gl/gEgYUd")
                }
            }
        }
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
        val view = binding.root
            if (accepted) handleImage()
            else Snackbar.make(view, "??chec!", Snackbar.LENGTH_LONG).show()
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
        // ici on construit une pop-up syst??me (Dialog) pour expliquer la n??cessit?? de la demande de permission
        AlertDialog.Builder(this)
            .setMessage("???? On a besoin de la cam??ra, vraiment! ????????")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les param??tres de l'app (pour modifier les permissions d??j?? refus??es par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        // ici pas besoin de v??rifier avant car on vise un ??cran syst??me:
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