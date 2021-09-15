package com.example.blogandroidapp.core

import android.view.View

/**
 * Created by Sebastián Pedraza on 11/09/2021.
 */

/**
 * Extension functions, se pueden usar en todas las Views
 */
fun View.hide() {
    // La visibilidad del objeto el esta llamando a la funcion
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}