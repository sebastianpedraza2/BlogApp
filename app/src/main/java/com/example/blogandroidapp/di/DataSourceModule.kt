package com.example.blogandroidapp.di

import com.example.blogandroidapp.data.remote.auth.AuthDataSource
import com.example.blogandroidapp.data.remote.camera.CameraDataSource
import com.example.blogandroidapp.data.remote.home.HomeScreenDataSource
import org.koin.dsl.module

/**
 * Configuracion para inyectar la capa de data source del proyecto
 */
val dataSourceModule = module {

    single { AuthDataSource() }

    single { HomeScreenDataSource() }

    single { CameraDataSource() }


}