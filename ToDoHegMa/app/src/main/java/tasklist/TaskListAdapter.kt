package tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hema.todo.R
import com.hema.todo.databinding.ItemTaskBinding

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =  oldItem.id == newItem.id;
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.title == newItem.title && oldItem.description == newItem.description;
}

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder> (TasksDiffCallback){
    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title;
            binding.taskDescription.text = task.description;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val binding = ItemTaskBinding.inflate(inflater, parent, false);
        return TaskViewHolder(binding);
    }

    override fun onBindViewHolder(holder: TaskListAdapter.TaskViewHolder, position: Int) {
        holder.bind(currentList[position]);
    }


}

