package com.example.blogandroidapp.data.remote.home

import android.util.Log
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenDataSource {

    suspend fun getLatestPosts(): Resource<List<Post>> {
        /**
         * Creo una lista que despues la voy a llenar con los documentos que llegan de firebase
         */
        val postList = mutableListOf<Post>()

        /**
         * Uso await para evitarme poner add on success y demas, await espera a que haya un resultado como un stream de datos y me lo devuelve.
         * No tengo que meterlo en un try catch porque con el await ya si hay un error tira el error y lo agarro en el viewmodel y alli lo wrapeo en un resource
         */
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()
        /**
         * hago un For convirtiendo cada documento de la collection a un objeto Post (Pojo)
         */
        for (post in querySnapshot.documents) {

            post.toObject(Post::class.java)?.let { postItem ->
                /**
                 * obtendo el timestamp del objeto q me llega de firebase y le coloco un estimado a lostimestamps que todavia no
                 * han sido asignados por el server (los timestamps agregados por el serivdor con el anotation  @ServerTimestamp)
                 *
                 * Para un post recien creado la asignacion del server timestamp toma un tiempo y cuando se muestra por primera vez aparece en null todavia,
                 * por eso debo poner un estimado antes de que el server de firebase asigne el valor final
                 */

                postItem.created_at = post.getTimestamp(
                    "created_at",
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )?.toDate()
                postList.add(postItem)


            }
        }

        return Resource.Success(postList)
    }
}