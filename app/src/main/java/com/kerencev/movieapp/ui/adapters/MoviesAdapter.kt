package com.kerencev.movieapp.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MoviesAdapter(private val itemClickListener: MoviesListAdapter.OnItemViewClickListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(), CoroutineScope by MainScope() {

    private var data = mutableListOf<MovieApi>()

    inner class MovieViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val year: TextView = itemView.findViewById(R.id.year)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val rootCard: MaterialCardView = itemView.findViewById(R.id.root_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = data[position]
        with(holder) {
            title.text = movie.title
            year.text = movie.year
            rating.text = movie.imDbRating
            movie.image?.let {
                Glide.with(context)
                    .load(it)
                    .placeholder(R.drawable.movie)
                    .fitCenter()
                    .into(image)
            } ?: Glide.with(context).clear(image)
            rootCard.setOnClickListener { itemClickListener.onItemViewClick(movie) }
        }
        setRightBackgroundForRating(movie.colorOfRating, holder)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(movies: List<MovieApi>) {
        data.addAll(movies)
        notifyDataSetChanged()
    }

    private fun setRightBackgroundForRating(color: String, holder: MovieViewHolder) {
        when (color) {
            COLOR_NULL -> holder.rating.visibility = View.GONE
            COLOR_RATING_GREEN -> holder.rating.setBackgroundResource(R.drawable.background_rating_green)
            COLOR_RATING_GRAY -> holder.rating.setBackgroundResource(R.drawable.background_rating_gray)
            COLOR_RATING_RED -> holder.rating.setBackgroundResource(R.drawable.background_rating_red)
        }
    }

    private suspend fun loadImage(context: Context, path: String): Drawable {
        val loader: ImageLoader = ImageLoader(context)
        val request: ImageRequest = ImageRequest.Builder(context)
            .data(path)
            .build()
        return (loader.execute(request) as SuccessResult).drawable
    }
}
