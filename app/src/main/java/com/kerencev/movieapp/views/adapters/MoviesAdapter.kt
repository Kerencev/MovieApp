package com.kerencev.movieapp.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.card.MaterialCardView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.entities.list.MovieApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoviesAdapter(private val itemClickListener: MoviesListAdapter.OnItemViewClickListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var data = mutableListOf<MovieApi>()

    fun setData(movies: List<MovieApi>) = data.addAll(movies)

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
            GlobalScope.launch(Dispatchers.Main) {
                image.load(movie.image) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                }
            }
            rootCard.setOnClickListener { itemClickListener.onItemViewClick(movie) }
        }
        setRightBackgroundForRating(movie, holder)
    }

    private fun setRightBackgroundForRating(movie: MovieApi, holder: MovieViewHolder) {
        if (movie.imDbRating?.length == 0) {
            holder.rating.visibility = View.GONE
            return
        }
        val rating = movie.imDbRating?.toDouble()
        when {
            rating == null -> holder.rating.visibility = View.GONE
            rating < 5 -> holder.rating.setBackgroundResource(R.drawable.background_rating_red)
            rating < 7 -> holder.rating.setBackgroundResource(R.drawable.background_rating_gray)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MovieViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val year: TextView = itemView.findViewById(R.id.year)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val rootCard: MaterialCardView = itemView.findViewById(R.id.root_card)
    }
}
