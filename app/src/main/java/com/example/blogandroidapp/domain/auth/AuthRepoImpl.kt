package com.example.blogandroidapp.domain.auth

import android.graphics.Bitmap
import com.example.blogandroidapp.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Sebastián Pedraza on 09/09/2021.
 */
class AuthRepoImpl(private val authDataSource: AuthDataSource) : AuthRepo {
    override suspend fun login(email: String, password: String): FirebaseUser? =
        authDataSource.login(email, password)

    override suspend fun signUp(email: String, password: String, username: String): FirebaseUser? =
        authDataSource.signUp(email, password, username)

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) = authDataSource.updateProfile(imageBitmap, username)
}