package com.hema.todo.user

import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import androidx.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hema.todo.R
import com.hema.todo.databinding.FragmentAuthentificationBinding
import com.hema.todo.databinding.FragmentLoginBinding

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
        binding.LoginCompletebutton.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            if (email!= null && password!= null){
                viewModel.login(LoginForm(email, password))
                if (viewModel.loginResponse == null){
                    Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()
                }
                else {
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, viewModel.loginResponse!!.token)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
                }
            }
        }
    }

}