package com.example.blogandroidapp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */

data class Post(
    val profile_picture: String = "",
    val profile_name: String = "",
    /**
     * Si le mando null la propiedad marcada con este anotador, entonces el server lo asigna
     */
    @ServerTimestamp
    var created_at: Date? = null,
    val post_image: String = "",
    val post_description: String = "",
    val uid: String = ""
)

