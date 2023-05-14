package com.example.blogsapp.domain.home

import com.example.blogsapp.core.Result
import com.example.blogsapp.data.model.Post
import com.example.blogsapp.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.flow.Flow

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {

    override suspend fun getLatestPost(): Result<List<Post>> = dataSource.getLatestPosts()

    override suspend fun registerLikeButtonState(postId: String, liked: Boolean) = dataSource.registerLikeButtonState(postId, liked)
}