package com.example.blogandroidapp.domain.camera

import android.graphics.Bitmap
import com.example.blogandroidapp.data.remote.camera.CameraDataSource

/**
 * Created by Sebastián Pedraza on 14/09/2021.
 */
class CameraRepoImpl(private val cameraDataSource: CameraDataSource) : CameraRepo {

    override suspend fun createPost(imageBitmap: Bitmap, imageDescription: String) =
        cameraDataSource.createPost(imageBitmap, imageDescription)
}