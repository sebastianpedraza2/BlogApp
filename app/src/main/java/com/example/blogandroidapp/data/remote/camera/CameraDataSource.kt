package com.example.blogandroidapp.data.remote.camera

import android.graphics.Bitmap
import com.example.blogandroidapp.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*


class CameraDataSource {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseStorage by lazy { FirebaseStorage.getInstance() }

    suspend fun createPost(imageBitmap: Bitmap, imageDescription: String) {
        val user = firebaseAuth.currentUser

        /**
         * Asigno el path de donde se va a guardar la imagen, carpeta uid/posts y  archivo con nombre unico para que no se
         * sobrescriba
         */
        val randomName = UUID.randomUUID().toString()
        val imageRef = firebaseStorage.reference.child("${user?.uid}/posts/$randomName")

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
         * Agrego un nuevo documento a la coleccion posts, con add y no document() para que lo cree con un nombre random unico
         */
        user?.displayName?.let {
            user.photoUrl
            firebaseFirestore.collection("posts").add(
                Post(
                    post_image = downloadUrl,
                    post_description = imageDescription,
                    profile_name = it,
                    profile_picture = user.photoUrl.toString(), // tostring si llega un null manda el string "null" por eso no es necesario verificar nulabilidad
                    uid = user.uid
                )
            ).await()
        }

    }
}