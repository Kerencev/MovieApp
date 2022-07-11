package com.kerencev.movieapp.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.images.ImageApi
import com.kerencev.movieapp.data.loaders.entities.images.ImagesApi

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {
    private var data: List<ImageApi?> = emptyList()

    fun setData(imagesApi: ImagesApi) {
        if (imagesApi.items != null && imagesApi.items.isNotEmpty()) {
            data = imagesApi.items
            notifyDataSetChanged()
        }
    }

    inner class ImagesViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_images, parent, false)
        return ImagesViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image: ImageApi? = data[position]
        image?.image?.let {
            Glide.with(holder.context)
                .load(it)
                .placeholder(R.drawable.photo)
                .fitCenter()
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}