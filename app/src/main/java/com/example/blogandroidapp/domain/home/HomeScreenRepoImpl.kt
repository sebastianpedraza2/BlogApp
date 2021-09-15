package com.example.blogandroidapp.domain.home

import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.Post
import com.example.blogandroidapp.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource) : HomeScreenRepo {
    @ExperimentalCoroutinesApi
    override suspend fun getLatestPosts(): Flow<Resource<List<Post>>> = dataSource.getLatestPosts()
}