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
import com.hema.todo.databinding.FragmentSignupBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    var _binding: FragmentSignupBinding? = null
    val binding get() = _binding!!
    private val viewModel = UserInfoViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
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
                    findNavController().navigate(R.id.action_signUpFragment_to_taskListFragment)
                }
                else {
                    Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.SignupCompletebutton.setOnClickListener {
            val firstname = binding.firstname.text.toString()
            val lastname = binding.lastname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.passwordConf.text.toString()
            if (email != null && password!=null && firstname != null && lastname != null && passwordConfirm == password){
                viewModel.signup(SignupForm(firstname, lastname, email, password, passwordConfirm))
            }
            else {
                Toast.makeText(context, "Données incorrectes", Toast.LENGTH_LONG).show()
            }
        }
    }
}