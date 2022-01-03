package com.hema.todo.user


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hema.todo.databinding.ActivityFormBinding
import com.hema.todo.databinding.ActivityMainBinding

class AuthentificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}