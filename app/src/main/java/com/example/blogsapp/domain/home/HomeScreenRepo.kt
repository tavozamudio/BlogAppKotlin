package com.example.blogsapp.domain.home

import com.example.blogsapp.core.Result
import com.example.blogsapp.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {
    suspend fun getLatestPost(): Result<List<Post>>
    suspend fun registerLikeButtonState(postId: String, liked: Boolean)
}