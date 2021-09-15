package com.example.blogandroidapp.domain.home

import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.Post

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
interface HomeScreenRepo {
    suspend fun getLatestPosts(): Resource<List<Post>>
}