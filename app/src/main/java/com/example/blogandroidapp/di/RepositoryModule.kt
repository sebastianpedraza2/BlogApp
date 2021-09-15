package com.example.blogandroidapp.di

import com.example.blogandroidapp.domain.auth.AuthRepo
import com.example.blogandroidapp.domain.auth.AuthRepoImpl
import com.example.blogandroidapp.domain.camera.CameraRepo
import com.example.blogandroidapp.domain.camera.CameraRepoImpl
import com.example.blogandroidapp.domain.home.HomeScreenRepo
import com.example.blogandroidapp.domain.home.HomeScreenRepoImpl
import org.koin.dsl.module

/**
 * Configuracion para inyectar la capa de repositorio del proyecto
 */
val repositoryModule = module {

    single<AuthRepo> { AuthRepoImpl(get()) }

    single<HomeScreenRepo> { HomeScreenRepoImpl(get()) }

    single<CameraRepo> { CameraRepoImpl(get()) }
}