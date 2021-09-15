package com.example.blogandroidapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogandroidapp.R
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.remote.auth.AuthDataSource
import com.example.blogandroidapp.databinding.FragmentRegisterBinding
import com.example.blogandroidapp.domain.auth.AuthRepoImpl
import com.example.blogandroidapp.presentation.auth.AuthViewModel
import com.example.blogandroidapp.presentation.auth.AuthViewModelFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private val authScreenViewModel: AuthViewModel by viewModel()

//    private val authScreenViewModel by viewModels<AuthViewModel> {
//        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        signUpUser()

    }

    private fun signUpUser() {
        binding.registerButton.setOnClickListener {
            val userName = binding.editTextUsername.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            /**
             * Para extraer un metodo en adroid studio solo usar ctrl + alt + M
             */
            if (password != confirmPassword) {
                binding.editTextConfirmPassword.error = "Passwords does not match"
                binding.editTextPassword.error = "Passwords does not match"
                /**
                 * return at label, en caso de que no sean iguales retorna la funcion setOnclicklistener, puedo crear un label setonclickListener @lit{ y return@lit
                 *
                 * Seria como un break o continue de un loop porque no continua con la ejecucion del onclick listener pero si del signUpUser
                 */
                return@setOnClickListener
            }

            if (userName.isEmpty()) {
                binding.editTextUsername.error = "Username cant be empty"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Email cant be empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.editTextPassword.error = "Password cant be empty"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                binding.editTextConfirmPassword.error = "Password cant be empty"
                return@setOnClickListener
            }

            createUser(userName, password, email)

        }
    }

    private fun createUser(userName: String, password: String, email: String) {
        authScreenViewModel.signUp(email, password, userName)
            .observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.registerProgressBar.visibility = View.VISIBLE
                        binding.registerButton.visibility = View.GONE
                    }
                    is Resource.Success -> {
//                        binding.registerProgressBar.visibility = View.VISIBLE
//                        binding.registerButton.visibility = View.GONE
                        /**
                         * If register successful then go to home screen
                         */
                        findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                    }
                    is Resource.Failure -> {
                        binding.registerProgressBar.visibility = View.GONE
                        binding.registerButton.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            "Error: ${result.exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }


}