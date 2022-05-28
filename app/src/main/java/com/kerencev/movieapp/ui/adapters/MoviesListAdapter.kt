package com.kerencev.movieapp.ui.adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.model.entities.MoviesList
import com.kerencev.movieapp.ui.details.DetailsFragment
import com.kerencev.movieapp.ui.main.MainFragment
import com.kerencev.movieapp.ui.main.MainFragment.OnItemViewClickListener

class MoviesListAdapter(private val fragmentManager: FragmentManager?) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    private var data: ArrayList<MoviesList> = ArrayList()

    fun setData(movies: Collection<MoviesList>) = data.addAll(movies)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies_list, parent, false)

        return MoviesListViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        val moviesList = data[position]

        with(holder) {
            title.text = moviesList.title

            recyclerView.layoutManager =
                LinearLayoutManager(holder.context, LinearLayoutManager.HORIZONTAL, false)

            val adapter = MoviesAdapter(object : OnItemViewClickListener {
                override fun onItemViewClick(movie: Movie) {
                    fragmentManager?.let { manager ->
                        val bundle = Bundle().apply {
                            putParcelable(DetailsFragment.BUNDLE_MOVIE, movie)
                        }
                        manager.beginTransaction()
                            .add(R.id.container, DetailsFragment.newInstance(bundle))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }

            })
            recyclerView.adapter = adapter
            adapter.setData(data[position].listOfMovies)
            adapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MoviesListViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_recycler_list)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_child);
    }
}