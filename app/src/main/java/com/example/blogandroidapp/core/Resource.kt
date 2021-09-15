package com.example.blogandroidapp.core

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
sealed class Resource<out T> {
    class Loading<out T>() : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()

    /**
     * Si me interesara el tipo de exception simplemente no pongo el nothing
     */
    data class Failure(val exception: Exception) : Resource<Nothing>()

}
