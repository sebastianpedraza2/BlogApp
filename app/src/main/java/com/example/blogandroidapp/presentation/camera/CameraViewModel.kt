package com.example.blogandroidapp.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.domain.camera.CameraRepo
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.EmptyCoroutineContext


class CameraViewModel(private val cameraRepo: CameraRepo) : ViewModel() {

    /**
     *  Le paso al builder del livedata el coroutine context en el cual se va a ejecutar el block
     *  Si no le pasara nada lo hace por default en el [EmptyCoroutineContext] combined with [Dispatchers.Main.immediate], si la actividad o fragment que holdea o instancia el viewmodel
     *  muere entonces habria que limpiar esos procesos en el oncleared del viewmodel manualmente porque si no puede haber memory leaks.
     *  Es mejor pasarrle el contexto del viewmodelscope que se cancela cuando el viewmodel muere + ejecutandose en el hilo principal del UI
     */

     fun createPost(imageBitmap: Bitmap, imageDescription: String) =
        liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(cameraRepo.createPost(imageBitmap, imageDescription)))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
}