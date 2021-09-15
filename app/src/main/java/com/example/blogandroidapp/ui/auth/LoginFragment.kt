package com.example.blogandroidapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogandroidapp.R
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.remote.auth.AuthDataSource
import com.example.blogandroidapp.databinding.FragmentLoginBinding
import com.example.blogandroidapp.domain.auth.AuthRepoImpl
import com.example.blogandroidapp.presentation.auth.AuthViewModel
import com.example.blogandroidapp.presentation.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    /**
     * By lazy crea la instancia solamente cuando se vaya a usar puedo ahorrar memoria, en vez de crearlo lateinit var e inicializarlo en el oncreate
     */
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loginScreenViewModel: AuthViewModel by viewModel()

//    private val loginScreenViewModel by viewModels<AuthViewModel> {
//        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Binding "view binding" to the view
         */
        binding = FragmentLoginBinding.bind(view)

        /**
         * Validating if current user is logged in
         */
        isUserLoggedIn()
        loginUser()
        goToSignUp()
    }

    private fun goToSignUp() {
        /**
         * If the user doesnt have an account then go to register screen
         */
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginUser() {
        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            if (validateUserData(email, password)) {
                /**
                 * En caso de que validate fun retorne algo entonces no sigue con la ejecucion
                 */
                doLogin(email, password)
            }

        }
    }


    private fun doLogin(email: String, password: String) {
        loginScreenViewModel.loginWithUserAndPassword(email, password)
            .observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.loginProgressBar.visibility = View.VISIBLE
                        binding.loginButton.visibility = View.GONE

                    }
                    is Resource.Success -> {
                        binding.loginProgressBar.visibility = View.GONE
                        binding.loginButton.visibility = View.VISIBLE

                        if (result.data?.displayName.isNullOrEmpty()) {
                            findNavController().navigate(R.id.action_loginFragment_to_setupProfileFragment)
                        } else {
                            findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                        }

                    }
                    is Resource.Failure -> {
                        binding.loginProgressBar.visibility = View.GONE
                        binding.loginButton.visibility = View.VISIBLE
                        /**
                         * Show a toast if failed
                         */
                        Toast.makeText(
                            requireContext(),
                            "Error: ${result.exception.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
    }

    private fun validateUserData(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email can't be empty"
            return false
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password can't be empty"
            return false
        }
        return true
    }

    private fun isUserLoggedIn() {
        firebaseAuth.currentUser?.let {
            if (it.displayName.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_loginFragment_to_setupProfileFragment)
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
            }

        }
    }
}