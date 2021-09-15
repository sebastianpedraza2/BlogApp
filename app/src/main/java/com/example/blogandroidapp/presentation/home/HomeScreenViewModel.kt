package com.example.blogandroidapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenViewModel(private val homeScreenRepo: HomeScreenRepo) : ViewModel() {

    fun fetchLatestPost() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(homeScreenRepo.getLatestPosts())

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}

/**
 *  El view model factory es el encargado de crear la instancia que se va ausar en la capa de ui
 *  el metodo de factory por defalt no permite crear el viewmodel con parametros en el contructor por eso hay que cambiarla y ensenarle a crear la instancia con parametros
 */

class HomeScreenViewModelFactory(private val homeScreenRepo: HomeScreenRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(homeScreenRepo)
    }
}