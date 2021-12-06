package com.hema.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hema.todo.databinding.FragmentTaskListBinding
import com.hema.todo.form.FormActivity
import com.hema.todo.network.Api
import com.hema.todo.network.TasksRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*

interface TaskListListener {
    fun onClickDelete(task: Task)
}

class TaskListFragment : Fragment() {

    val adapterListener = TaskListListener1()
    private val tasksRepository = TasksRepository()

    inner class TaskListListener1 : TaskListListener {
        override fun onClickDelete(task: Task) {
            lifecycleScope.launch {
                tasksRepository.delete(task)
                tasksRepository.refresh()

            }
        }
        fun onClickEdit(task : Task){
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("edittask", task)
            formLauncherEdit.launch(intent)
        }
    }

    val myAdapter = TaskListAdapter(adapterListener)


    //val myAdapter = TaskListAdapter()
    val formLauncherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task ?: return@registerForActivityResult
        lifecycleScope.launch {
            tasksRepository.updateTask(task)
            tasksRepository.refresh()
        }
    }
    val formLauncherCreate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task ?: return@registerForActivityResult
        lifecycleScope.launch {
            tasksRepository.create(task)
            tasksRepository.refresh()
        }
    }



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
            tasksRepository.taskList.collect{ newList ->
                myAdapter.submitList(newList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            tasksRepository.refresh()
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.UserInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

