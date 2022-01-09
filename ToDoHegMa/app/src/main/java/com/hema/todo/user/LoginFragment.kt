package com.hema.todo.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.hema.todo.R
import com.hema.todo.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

const val SHARED_PREF_TOKEN_KEY = "auth_token_key"

class LoginFragment : Fragment() {

    var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!
    private val viewModel = UserInfoViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.LoginResponse.collect { loginres ->
                if(loginres != null) {
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, loginres?.token)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
                }
                else {
                        Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()

                }
            }
        }
        binding.LoginCompletebutton.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            if (email!= null && password!= null){
                viewModel.login(LoginForm(email, password))
            }
        }

    }
}