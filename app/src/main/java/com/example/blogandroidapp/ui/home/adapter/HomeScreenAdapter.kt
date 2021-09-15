package com.example.blogandroidapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogandroidapp.core.BaseViewHolder
import com.example.blogandroidapp.core.TimeUtils
import com.example.blogandroidapp.core.hide
import com.example.blogandroidapp.core.show
import com.example.blogandroidapp.data.Post
import com.example.blogandroidapp.databinding.PostItemLayoutBinding

/**
 * Created by Sebastián Pedraza on 01/09/2021.
 */
class HomeScreenAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    /**
     * Esta lo unico que hace es inflar la vista postItemLayout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            PostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeScreenViewHolder(parent.context, itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        /**
         * Aca le pasamos a la funcion bind del baseviewHolder implementada abajo cada elemento post de la lista a la vez
         */
        when (holder) {
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }
    }

    override fun getItemCount(): Int = postList.size


    /**
     * Esta funcion bindea cada elemento post de la lista con el view, la llama onBindViewHolder
     */
    private inner class HomeScreenViewHolder(
        val context: Context,
        val binding: PostItemLayoutBinding
    ) : BaseViewHolder<Post>(binding.root) {
        override fun bind(item: Post) {
            /**
             * relacionando las propiedades del item POST que se le pasa con la vista
             */
            Glide.with(context).load(item.post_image).centerCrop().into(binding.postImage)
            Glide.with(context).load(item.profile_picture).centerCrop().into(binding.profilePic)
            /**
             * Si no llega nada del back y se inicializa en vacio entonces oculto la vista de descripcion
             */
            if (item.post_description.isEmpty())
                binding.postDescription.hide() else
                binding.postDescription.show()
            binding.postDescription.text = item.post_description
            binding.profileName.text = item.profile_name

            /**
             * obtengo el tiempo q puso el server y lo paso a segundos long dividiendolo en 1000, ese valor se lo mando a
             * la funcion q me devuelve el string con el 'tanto ago'
             */

            val createdAt = item.created_at?.time?.div(1000L)?.let {
                TimeUtils.getTimeAgo(it.toInt())
            }

            binding.postTimestamp.text = createdAt


        }
    }

}
