package tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hema.todo.R
import com.hema.todo.databinding.FragmentTaskListBinding
import java.util.*

class TaskListFragment : Fragment() {


    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

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
        val myAdapter = TaskListAdapter()
        recyclerView.adapter = myAdapter
        binding.floatingActionButton2.setOnClickListener{
            taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
            myAdapter.submitList(taskList.toList());
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

