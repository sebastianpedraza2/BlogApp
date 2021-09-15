package com.example.blogandroidapp.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.domain.auth.AuthRepo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Sebastián Pedraza on 09/09/2021.
 */
class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    fun loginWithUserAndPassword(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(authRepo.login(email, password)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun signUp(email: String, password: String, username: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(authRepo.signUp(email, password, username)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun updateProfileData(imageBitmap: Bitmap, username: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(authRepo.updateProfile(imageBitmap, username)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}

class AuthViewModelFactory(private val authRepo: AuthRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(LoginRepo::class.java).newInstance(loginRepo)
        /**
         * Se puede instanciar el LoginScreenViewModel solo retornando la instancia con el constructor y casteandolo como T porque el metodo override espera un T
         */
        return AuthViewModel(authRepo) as T
    }

}