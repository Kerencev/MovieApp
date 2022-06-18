package com.kerencev.movieapp.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.card.MaterialCardView
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FavoritesAdapter(private val itemClickListener: OnItemFavoriteClickListener) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>(), CoroutineScope by MainScope() {

    inner class FavoriteViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.poster)
        val title: TextView = itemView.findViewById(R.id.title)
        val year: TextView = itemView.findViewById(R.id.year)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
        val cardImage: MaterialCardView = itemView.findViewById(R.id.card_image)
        val tvMyRating: TextView = itemView.findViewById(R.id.tv_my_rating)
        val tvMyNote: TextView = itemView.findViewById(R.id.tv_my_note)
    }

    interface OnItemFavoriteClickListener {
        fun onItemViewClick(movie: MovieApi)
    }

    private var data = mutableListOf<MovieApi>()

    fun setData(movies: List<MovieApi>) = data.addAll(movies)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_movie, parent, false)
        return FavoriteViewHolder(itemView = itemView, context = parent.context)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = data[position]
        with(holder) {
            launch(Dispatchers.Main) {
                image.load(movie.image) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                }
            }
            rating.text = movie.imDbRating
            setRightBackgroundForRating(movie.colorOfRating, holder)
            title.text = movie.title
            year.text = movie.year
            root.setOnClickListener {
                itemClickListener.onItemViewClick(movie)
                cardImage.setOnClickListener { }
            }
            movie.myRating?.let { rating ->
                tvMyRating.visibility = View.VISIBLE
                tvMyRating.text = tvMyRating.text.toString() + " " + rating
            }
            movie.myNote?.let { myNote ->
                tvMyNote.visibility = View.VISIBLE
                tvMyNote.text = myNote
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun setRightBackgroundForRating(color: String, holder: FavoriteViewHolder) {
        when (color) {
            COLOR_NULL -> holder.rating.visibility = View.GONE
            COLOR_RATING_GREEN -> holder.rating.setBackgroundResource(R.drawable.background_rating_green)
            COLOR_RATING_GRAY -> holder.rating.setBackgroundResource(R.drawable.background_rating_gray)
            COLOR_RATING_RED -> holder.rating.setBackgroundResource(R.drawable.background_rating_red)
        }
    }
}