package com.example.blogandroidapp.di

import com.example.blogandroidapp.presentation.auth.AuthViewModel
import com.example.blogandroidapp.presentation.camera.CameraViewModel
import com.example.blogandroidapp.presentation.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Configuracion para inyectar los viewmodels del proyecto
 */
val viewModelModule = module {

    viewModel { AuthViewModel(get()) }

    viewModel { HomeScreenViewModel(get()) }

    viewModel { CameraViewModel(get()) }

}