package com.example.blogandroidapp.core

import java.util.concurrent.TimeUnit

/**
 * Created by Sebastián Pedraza on 14/09/2021.
 */

private const val SECOND = 1
private const val MINUTE = SECOND * 60
private const val HOUR = MINUTE * 60
private const val DAY = HOUR * 24

object TimeUtils {

    /**
     * Recibe un tiempo en segundos
     */
    fun getTimeAgo(time: Int): String {
        /**
         * obteniendo el tiempo actual y pasandolo a segundos
         */

         val currentTime =TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        /**
         * Si el tiempo que viene del server es mayor que el del dispositivo ( puede ser porq esta mal el tiempo del dispositivo)
         */
        if(time > currentTime || time <= 0) {
            return "In the future"
        }

        val difference = currentTime - time

       return when {
           difference < MINUTE -> "Just now"
           difference < 2 * MINUTE -> "A minute ago"
           difference < HOUR -> "${difference / MINUTE} minutes ago"
           difference < 2* HOUR -> "An hour ago"
           difference < DAY -> "${difference / HOUR} hours ago"
           difference < 48 * HOUR -> "Yesterday"
          else -> "${difference / DAY} days ago"

        }

    }
}