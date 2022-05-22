package com.kerencev.movieapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.ui.main.MainFragment

class MoviesAdapter(private val itemClickListener: MainFragment.OnItemViewClickListener)
    : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var data: ArrayList<Movie> = ArrayList()

    fun setData(movies: Collection<Movie>) = data.addAll(movies)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = data.get(position)

        with(holder) {
            title.text = movie.title
            genre.text = movie.genre
            year.text = movie.year.toString()
            rating.text = movie.rating.toString()
            image.setImageResource(movie.poster)
            rootCard.setOnClickListener { itemClickListener.onItemViewClick(movie) }
        }

        setRightBackgroundForRating(movie, holder)
    }

    private fun setRightBackgroundForRating(movie: Movie, holder: MovieViewHolder) {
        if (movie.rating < 5) {
            holder.rating.setBackgroundResource(R.drawable.background_rating_red)
        } else if (movie.rating < 7) {
            holder.rating.setBackgroundResource(R.drawable.background_rating_gray)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MovieViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val genre: TextView = itemView.findViewById(R.id.genre)
        val year: TextView = itemView.findViewById(R.id.year)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val rootCard: MaterialCardView = itemView.findViewById(R.id.root_card)
    }
}
