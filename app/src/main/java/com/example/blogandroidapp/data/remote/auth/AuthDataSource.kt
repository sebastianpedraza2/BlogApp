package com.example.blogandroidapp.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import com.example.blogandroidapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

/**
 * Created by Sebastián Pedraza on 09/09/2021.
 */
class AuthDataSource {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseStorage by lazy { FirebaseStorage.getInstance() }

    suspend fun login(email: String, password: String): FirebaseUser? {
        val fireBaseAuth = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return fireBaseAuth.user
    }

    suspend fun signUp(email: String, password: String, username: String): FirebaseUser? {
        val fireBaseAuth = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        /**
         * Creo la coleccion 'users' y dentro el documento con el id del usuario creado en el paso anterior,
         * En ese nuevo documento almaceno infomarcion del usuario adicional
         */
        fireBaseAuth.user?.let {
            firebaseFirestore.collection("users").document(it.uid)
                .set(User(username, email, "IMAGE.PNG")).await()
        }
        return fireBaseAuth.user
    }

    suspend fun updateProfile(imageBitmap: Bitmap, username: String) {
        val user = firebaseAuth.currentUser

        /**
         * Asigno el path de donde se va a guardar la imagen, carpeta uid y archivo profile_image
         * El nombre del archivo para que cuando lo cambie simplemente lo sobrescriba y no cree uno nuevo
         */
        val imageRef = firebaseStorage.reference.child("${user?.uid}/profile_image")

        /**
         * Comprimo la imagen
         */
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        /**
         * Subo la imagen contraida a firebase storage, espero por eso, pido el url de la imagen que se subio, espero por eso
         */
        val downloadUrl =
            imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        /**
         * update user info in firebase
         */
        val profileUpdates = userProfileChangeRequest {
            displayName = username
            photoUri = Uri.parse(downloadUrl)
        }
        /**
         * funcion de firebase para actualizar la info del usuario
         */
        user?.updateProfile(profileUpdates)?.await()

    }
}