package com.hema.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hema.todo.databinding.ActivityFormBinding
import com.hema.todo.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val task = intent.getSerializableExtra("edittask") as? Task
        binding.editTextTitle.setText(task?.title?: "")
        binding.editTextDescription.setText(task?.description?: "")
        val id = task?.id ?: UUID.randomUUID().toString()
        binding.button2.setOnClickListener{
            val newTask = Task(id, title = binding.editTextTitle.text.toString(), description=binding.editTextDescription.text.toString())
            intent.putExtra("task", newTask);
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}