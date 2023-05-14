package com.example.blogsapp.ui.camera

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
import com.example.blogsapp.data.remote.camera.CameraDataSource
import com.example.blogsapp.data.remote.home.HomeScreenDataSource
import com.example.blogsapp.databinding.FragmentCameraBinding
import com.example.blogsapp.domain.camera.CameraRepoImpl
import com.example.blogsapp.domain.home.HomeScreenRepoImpl
import com.example.blogsapp.presentation.camera.CameraViewModel
import com.example.blogsapp.presentation.camera.CameraViewModelFactory
import com.example.blogsapp.presentation.home.HomeScreenViewModel
import com.example.blogsapp.presentation.home.HomeScreenViewModelFactory

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var binding: FragmentCameraBinding
    private var bitmap: Bitmap? = null
    private val viewModel by viewModels<CameraViewModel> { CameraViewModelFactory(
        CameraRepoImpl(
            CameraDataSource()
        )
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            getAction.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No se encontro ninguna app para abrir la camara", Toast.LENGTH_SHORT).show()
        }

        binding.btnUploadPhoto.setOnClickListener {
            bitmap?.let {
                viewModel.uploadPhoto(it, binding.etxtPhotoDescription.text.toString().trim()).observe(viewLifecycleOwner, { result ->
                    when(result) {
                        is Result.Loading -> {
                            Toast.makeText(requireContext(), "Uploading photo...", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
                        }
                        is Result.Faillure -> {
                            Toast.makeText(requireContext(), "Error: ${result.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val imageBitmap = it?.data?.extras?.get("data") as Bitmap
        binding.imgAddPhoto.setImageBitmap(imageBitmap)
        bitmap = imageBitmap
    }
}