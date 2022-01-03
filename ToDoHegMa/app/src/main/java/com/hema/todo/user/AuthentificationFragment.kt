package com.hema.todo.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hema.todo.R
import com.hema.todo.databinding.FragmentAuthentificationBinding
import com.hema.todo.databinding.FragmentTaskListBinding

class AuthentificationFragment : Fragment() {

    var _binding: FragmentAuthentificationBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthentificationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_authentificationFragment_to_signUpFragment)
        }
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_authentificationFragment_to_loginFragment)
        }
    }
}
