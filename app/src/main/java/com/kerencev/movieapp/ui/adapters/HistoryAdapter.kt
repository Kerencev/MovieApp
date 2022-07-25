package com.kerencev.movieapp.ui.adapters

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
import com.kerencev.movieapp.data.database.entities.HistoryEntity
import com.kerencev.movieapp.data.loaders.entities.list.COLOR_NULL
import com.kerencev.movieapp.data.loaders.entities.list.COLOR_RATING_GRAY
import com.kerencev.movieapp.data.loaders.entities.list.COLOR_RATING_GREEN
import com.kerencev.movieapp.data.loaders.entities.list.COLOR_RATING_RED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HistoryAdapter(private val itemClickListener: OnItemHistoryClickListener) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(), CoroutineScope by MainScope() {

    private var data = mutableListOf<HistoryEntity>()

    interface OnItemHistoryClickListener {
        fun onItemViewClick(movie: HistoryEntity)
    }

    inner class HistoryViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val title: TextView = itemView.findViewById(R.id.title)
        val year: TextView = itemView.findViewById(R.id.year)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val date: TextView = itemView.findViewById(R.id.date)
        val rootCard: MaterialCardView = itemView.findViewById(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_history_movie, parent, false)
        return HistoryViewHolder(itemView = itemView, context = parent.context)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val movie = data[position]
        with(holder) {
            launch(Dispatchers.Main) {
                poster.load(movie.poster) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                }
            }
            title.text = movie.title
            year.text = movie.year
            rating.text = movie.rating
            setRightBackgroundForRating(movie.colorOfRating, holder)
            date.text = movie.date
            rootCard.setOnClickListener {
                itemClickListener.onItemViewClick(movie)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(movies: List<HistoryEntity>) {
        data = movies as MutableList<HistoryEntity>
        notifyDataSetChanged()
    }

    private fun setRightBackgroundForRating(color: String, holder: HistoryViewHolder) {
        when (color) {
            COLOR_NULL -> holder.rating.visibility = View.GONE
            COLOR_RATING_GREEN -> holder.rating.setBackgroundResource(R.drawable.background_rating_green)
            COLOR_RATING_GRAY -> holder.rating.setBackgroundResource(R.drawable.background_rating_gray)
            COLOR_RATING_RED -> holder.rating.setBackgroundResource(R.drawable.background_rating_red)
        }
    }
}