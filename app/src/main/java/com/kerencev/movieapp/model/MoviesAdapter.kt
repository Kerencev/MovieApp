package com.kerencev.movieapp.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.model.entities.Movie

//TODO Куда добавлять адаптер, в какой пакет ?

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

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
            year.text = movie.year.toString()
            rating.text = movie.rating.toString()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MovieViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val year: TextView = itemView.findViewById(R.id.year)
        val rating: TextView = itemView.findViewById(R.id.rating)
    }
}
