package com.hema.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.hema.todo.R
import com.hema.todo.databinding.FragmentTaskListBinding
import com.hema.todo.form.FormActivity
import com.hema.todo.network.Api
import com.hema.todo.user.UserInfoActivity
import com.hema.todo.user.UserInfoViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

interface TaskListListener {
    fun onClickDelete(task: Task)
}

class TaskListFragment : Fragment() {

    val adapterListener = TaskListListener1()
    //private val tasksRepository = TasksRepository()

    inner class TaskListListener1 : TaskListListener {
        override fun onClickDelete(task: Task) {
            lifecycleScope.launch {
                viewModel.delete(task)
                viewModel.refresh()

            }
        }
        fun onClickEdit(task : Task){
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("edittask", task)
            formLauncherEdit.launch(intent)
        }
    }

    val myAdapter = TaskListAdapter(adapterListener)
    private val viewModel: TaskListViewModel by viewModels()
    private val UserInfoViewModel : UserInfoViewModel by viewModels()

    val formLauncherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task ?: return@registerForActivityResult
        lifecycleScope.launch {
            viewModel.edit(task)
            viewModel.refresh()
        }
    }
    val formLauncherCreate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task ?: return@registerForActivityResult
        lifecycleScope.launch {
            viewModel.add(task)
            viewModel.refresh()
        }
    }

    val UserInfoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}



    var _binding: FragmentTaskListBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter = myAdapter
        binding.floatingActionButton2.setOnClickListener{
            val intent = Intent(activity, FormActivity::class.java)
            formLauncherCreate.launch(intent)
        }
        lifecycleScope.launch{
            viewModel.taskList.collectLatest{ newList ->
                myAdapter.submitList(newList)
        }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refresh()
            UserInfoViewModel.refresh()
            lifecycleScope.launch {
                val userInfo = UserInfoViewModel.userInfo.collect { userInfo ->
                    if (userInfo != null) {
                        if (userInfo.avatar != null) {
                            binding.AvatarImage.load(userInfo.avatar){
                                transformations(CircleCropTransformation())
                            }
                        } else error(R.drawable.ic_launcher_background)
                        binding.UserInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
                    }
                }
            }
            binding.AvatarImage.setOnClickListener{
                val intent = Intent(activity, UserInfoActivity::class.java)
                UserInfoLauncher.launch(intent)
            }
            binding.UserInfoTextView.setOnClickListener{

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

