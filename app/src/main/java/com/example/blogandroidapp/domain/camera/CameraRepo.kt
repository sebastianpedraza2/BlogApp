package com.example.blogandroidapp.domain.camera

import android.graphics.Bitmap

/**
 * Created by Sebastián Pedraza on 14/09/2021.
 */
interface CameraRepo {
    suspend fun createPost(imageBitmap: Bitmap, imageDescription: String)
}