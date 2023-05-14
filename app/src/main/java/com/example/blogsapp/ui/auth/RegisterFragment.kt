package com.example.blogsapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogsapp.R
import com.example.blogsapp.core.Result
import com.example.blogsapp.data.remote.auth.AuthDataSource
import com.example.blogsapp.databinding.FragmentRegisterBinding
import com.example.blogsapp.domain.auth.AuthRepoImpl
import com.example.blogsapp.presentation.auth.AuthViewModel
import com.example.blogsapp.presentation.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> { AuthViewModelFactory(
        AuthRepoImpl(
        AuthDataSource()
    ) ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        signUp()
    }

    private fun signUp() {
        binding.btnSignup.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            if (password != confirmPassword) {
                binding.editTextConfirmPassword.error = "Password does not match"
                binding.editTextPassword.error = "Password does not match"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.editTextPassword.error = "Password is empty"
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                binding.editTextConfirmPassword.error = "Password confirm is empty"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Email is empty"
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                binding.editTextUsername.error = "Username is empty"
                return@setOnClickListener
            }

            createUser(email, password, username)
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        viewModel.signUp(email, password, username).observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                }
                is Result.Faillure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignup.isEnabled = true
                    Toast.makeText(requireContext(), "Error: ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}