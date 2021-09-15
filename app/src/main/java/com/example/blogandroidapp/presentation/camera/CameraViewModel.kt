package com.example.blogandroidapp.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.domain.camera.CameraRepo
import kotlinx.coroutines.Dispatchers


class CameraViewModel(private val cameraRepo: CameraRepo) : ViewModel() {

     fun createPost(imageBitmap: Bitmap, imageDescription: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(cameraRepo.createPost(imageBitmap, imageDescription)))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
}