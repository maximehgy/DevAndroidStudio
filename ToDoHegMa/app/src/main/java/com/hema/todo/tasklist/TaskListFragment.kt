package com.hema.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hema.todo.databinding.FragmentTaskListBinding
import com.hema.todo.form.FormActivity
import java.util.*

interface TaskListListener {
    fun onClickDelete(task: Task)
}

class TaskListFragment : Fragment() {

    val adapterListener = TaskListListener1()

    inner class TaskListListener1 : TaskListListener {
        override fun onClickDelete(task: Task) {
            taskList.remove(task);
            myAdapter.submitList(taskList.toList())
        }
        fun onClickEdit(task : Task){
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("edittask", task)
            formLauncher.launch(intent)
        }
    }

    val myAdapter = TaskListAdapter(adapterListener)

    private var taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    //val myAdapter = TaskListAdapter()
    val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task ?: return@registerForActivityResult
        val oldTask = taskList.firstOrNull { it.id == task.id }
        if (oldTask != null) {
            taskList = (taskList - oldTask) as MutableList<Task>
        }
        taskList.add(task)
        myAdapter.submitList(taskList.toList())
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
            formLauncher.launch(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

