package com.kerencev.movieapp.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.search.Result
import com.kerencev.movieapp.data.loaders.entities.search.SearchedMovies

class SearchAdapter(private val itemClickListener: OnItemSearchClickListener) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var data = emptyList<Result?>()

    fun setData(dto: SearchedMovies) {
        dto.results?.let {
            data = it
            notifyDataSetChanged()
        }
    }

    interface OnItemSearchClickListener {
        fun onItemViewClick(id: String)
    }

    inner class SearchViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val title: TextView = itemView.findViewById(R.id.title)
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_searched_movie, parent, false)
        return SearchViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = data[position]
        movie?.image?.let {
            holder.poster.load(movie.image) {
                crossfade(true)
                placeholder(R.drawable.movie)
            }
        }
        movie?.title?.let { holder.title.text = movie.title }
        movie?.id?.let { id ->
            holder.root.setOnClickListener { itemClickListener.onItemViewClick(id) }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}