package com.example.blogsapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.blogsapp.R
import com.example.blogsapp.core.Result
import com.example.blogsapp.core.hide
import com.example.blogsapp.core.show
import com.example.blogsapp.data.model.Post
import com.example.blogsapp.data.remote.home.HomeScreenDataSource
import com.example.blogsapp.databinding.FragmentHomeScreenBinding
import com.example.blogsapp.domain.home.HomeScreenRepoImpl
import com.example.blogsapp.presentation.home.HomeScreenViewModel
import com.example.blogsapp.presentation.home.HomeScreenViewModelFactory
import com.example.blogsapp.ui.home.adapter.HomeScreenAdapter
import com.example.blogsapp.ui.home.adapter.OnPostClickListener

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen), OnPostClickListener {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> { HomeScreenViewModelFactory(
        HomeScreenRepoImpl(
        HomeScreenDataSource()
    )
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewModel.fetchLatestsPost().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.show()
                        return@Observer
                    } else {
                        binding.emptyContainer.hide()
                    }
                    binding.rvHome.adapter = HomeScreenAdapter(result.data, this)
                }
                is Result.Faillure -> {
                    binding.progressBar.hide()
                    Toast.makeText(requireContext(), "Ocurrio un error: ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onLikeButtonClick(post: Post, liked: Boolean) {
        viewModel.registerLikeButtonState(post.id, liked).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {  }
                is Result.Success -> {

                }
                is Result.Faillure -> {
                    Toast.makeText(requireContext(), "Ocurrio un error: ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}