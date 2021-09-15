package com.example.blogandroidapp.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}