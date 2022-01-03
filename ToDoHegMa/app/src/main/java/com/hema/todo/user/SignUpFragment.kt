package com.hema.todo.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hema.todo.databinding.FragmentAuthentificationBinding
import com.hema.todo.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {

    var _binding: FragmentSignupBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}