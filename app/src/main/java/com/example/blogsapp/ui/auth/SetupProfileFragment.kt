package com.example.blogsapp.ui.auth

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogsapp.R
import com.example.blogsapp.core.Result
import com.example.blogsapp.data.remote.auth.AuthDataSource
import com.example.blogsapp.databinding.FragmentSetupProfileBinding
import com.example.blogsapp.domain.auth.AuthRepoImpl
import com.example.blogsapp.presentation.auth.AuthViewModel
import com.example.blogsapp.presentation.auth.AuthViewModelFactory

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding
    private val viewModel by viewModels<AuthViewModel> { AuthViewModelFactory(
        AuthRepoImpl(
            AuthDataSource()
        ) ) }

    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        binding.profileImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                getAction.launch(takePictureIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No se encontro ninguna app para abrir la camara", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCreateProfile.setOnClickListener {
            val username = binding.etxtUsername.text.toString().trim()
            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Uploading photo...").create()
            if (bitmap != null && username.isNotEmpty()) {
                viewModel.updateUserProfile(imageBitmap = bitmap!!, username = username).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            alertDialog.show()
                        }

                        is Result.Success -> {
                            alertDialog.dismiss()
                            findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                        }

                        is Result.Faillure -> {
                            alertDialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val imageBitmap = it?.data?.extras?.get("data") as Bitmap
        binding.profileImage.setImageBitmap(imageBitmap)
        bitmap = imageBitmap
    }
}