package com.example.blogandroidapp.presentation.home

import androidx.lifecycle.*
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenViewModel(private val homeScreenRepo: HomeScreenRepo) : ViewModel() {

    @ExperimentalCoroutinesApi
    fun fetchLatestPost() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        /**
         * Me devuelve un Flow<Result> y necesito un Result, lo puedo desencapsular asi
         */
        kotlin.runCatching {
            homeScreenRepo.getLatestPosts()
        }
            .onSuccess { flow ->
                /**
                 * collect para conseguir lo que esta adentro del flow
                 */

                flow.collect { value ->
                    emit(value)
                }
            }
            .onFailure {
                emit(Resource.Failure(Exception(it.message)))
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