package com.example.blogandroidapp.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Sebastián Pedraza on 09/09/2021.
 */
interface AuthRepo {
    suspend fun login(email: String, password: String): FirebaseUser?
    suspend fun signUp(email: String, password: String, username: String): FirebaseUser?
    suspend fun updateProfile(imageBitmap: Bitmap, username: String)

}