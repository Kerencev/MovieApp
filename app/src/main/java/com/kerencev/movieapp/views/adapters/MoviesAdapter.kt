package com.kerencev.movieapp.views.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.material.card.MaterialCardView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

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

                    //Вариант с Coil
                    image.load(movie.image) {
                        crossfade(true)
                        placeholder(R.drawable.movie)
                    }
                    //Picasso
//                    Picasso.get()
//                        .load(it)
//                        .placeholder(R.drawable.movie)
//                        .error(R.drawable.movie)
//                        .into(image);

            }
            rootCard.setOnClickListener { itemClickListener.onItemViewClick(movie) }
        }
        setRightBackgroundForRating(movie.colorOfRating, holder)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(movies: List<MovieApi>) = data.addAll(movies)

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
