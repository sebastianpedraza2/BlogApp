package com.example.blogandroidapp.data.remote.home

import android.util.Log
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.data.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenDataSource {

    /**
     * Cambio el return a un flow que es un data stream para escuchar cambios
     */

    @ExperimentalCoroutinesApi
    suspend fun getLatestPosts(): Flow<Resource<List<Post>>> = callbackFlow {

        /**
         * Callbackflow es el productor (productor(dataSource) - intermediario -consumidor(ui)).
         * Creo la referencia de donde voy a buscar la info y si falla cierro el canal del flow (close retorna false )
         */

        var postReference: Query? = null

        try {
            postReference = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING)
        } catch (e: Throwable) {
            close(e)
        }

        /**
         * Creo una lista que despues la voy a llenar con los documentos que llegan de firebase
         */
        val postList = mutableListOf<Post>()

        /**
         * Este listener va a estar escuchando a cambios
         */
        val subscription = postReference?.addSnapshotListener { value, error ->
            try {
                // Limpio la lista para evitar mostrar datos duplicados
                postList.clear()
                if (value == null) return@addSnapshotListener
                /**
                 * hago un For convirtiendo cada documento de la collection a un objeto Post (Pojo)
                 */
                for (post in value.documents) {

                    post.toObject(Post::class.java)?.let { postItem ->
                        /**
                         * obtendo el timestamp del objeto q me llega de firebase y le coloco un estimado a los timestamps que todavia no
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
            } catch (e: Throwable) {
                close(e)
            }
            /**
             * Agrego el nuevo elemento POST al channel del flow
             */
            offer(Resource.Success(postList))

        }
        /**
         * Por ultimo Cuando ya no necesite mas agarro la subscription a firebase que cree y la elimino para no estar escuchando datos sin necesidad
         * memoria, red, bateria, costo firebase
         */
        awaitClose { subscription?.remove() }


        /**
         * Uso await para evitarme poner add on success y demas, await espera a que haya un resultado como un stream de datos y me lo devuelve.
         * No tengo que meterlo en un try catch porque con el await ya si hay un error tira el error y lo agarro en el viewmodel y alli lo wrapeo en un resource
         */
//        val querySnapshot = FirebaseFirestore.getInstance().collection("posts")
//            .orderBy("created_at", Query.Direction.DESCENDING).get().await()


    }
}