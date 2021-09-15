package com.example.blogandroidapp.domain.home

import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.Post
import com.example.blogandroidapp.data.remote.home.HomeScreenDataSource

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource) : HomeScreenRepo {
    override suspend fun getLatestPosts(): Resource<List<Post>> = dataSource.getLatestPosts()
}