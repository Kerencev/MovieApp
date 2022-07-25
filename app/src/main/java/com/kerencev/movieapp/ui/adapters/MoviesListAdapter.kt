package com.kerencev.movieapp.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.list.MoviesListApi
import com.kerencev.movieapp.ui.details.DetailsFragment
import com.kerencev.movieapp.ui.main.MainFragment

class MoviesListAdapter(private val onCategoryClickListener: OnCategoryClickListener, private val fragmentManager: FragmentManager?) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(movies: MoviesListApi)
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: MovieApi)
    }

    private var data = ArrayList<MoviesListApi>()

    fun setData(categories: ArrayList<MoviesListApi>) {
        data.addAll(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies_list, parent, false)

        return MoviesListViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        with(holder) {
            title.text = data[position].title
            recyclerView.layoutManager =
                LinearLayoutManager(holder.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = MoviesAdapter(object : OnItemViewClickListener {
                override fun onItemViewClick(movie: MovieApi) {
                    fragmentManager?.let { manager ->
                        val bundle = Bundle().apply {
                            putString(DetailsFragment.BUNDLE_MOVIE, movie.id)
                        }
                        val mainFragment = manager.findFragmentByTag(MainFragment.MAIN_FRAGMENT_TAG)
                        manager.beginTransaction()
                            .hide(mainFragment!!)
                            .add(R.id.container, DetailsFragment.newInstance(bundle))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
            })
            rootTitle.setOnClickListener {
                onCategoryClickListener.onCategoryClick(data[position])
            }
            recyclerView.adapter = adapter
            data[position].items?.let { movies ->
                adapter.setData(movies)
                adapter.notifyItemInserted(position)
            }
        }
    }

    private fun setTitle(holder: MoviesListViewHolder, position: Int) {
        with(holder) {
            when (position) {
                0 -> title.text = context.resources.getString(R.string.most_popular_movies)
                1 -> title.text = context.resources.getString(R.string.top250)
                2 -> title.text = context.resources.getString(R.string.coming_soon)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MoviesListViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val rootTitle: LinearLayoutCompat = itemView.findViewById(R.id.root_title_recycler_list)
        val title: TextView = itemView.findViewById(R.id.title_recycler_list)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_child);
    }
}