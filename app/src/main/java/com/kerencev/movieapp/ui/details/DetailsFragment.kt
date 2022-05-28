package com.kerencev.movieapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.DetailsFragmentBinding
import com.kerencev.movieapp.databinding.MainFragmentBinding
import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.model.helpers.formatBudget
import com.kerencev.movieapp.model.helpers.formatDuration
import com.kerencev.movieapp.model.helpers.formatTitleDetails

class DetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Movie>(BUNDLE_MOVIE)?.let {
            renderData(it)
        }
    }

    private fun renderData(movie: Movie) {
        with(binding) {
            poster.setImageResource(movie.poster)
            title.text = formatTitleDetails(movie.title, movie.year)
            genre.text = movie.genre
            duration.text = formatDuration(movie.duration)
            rating.text = movie.rating.toString()
            budget.text = formatBudget(movie.budget)
            description.text = movie.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_MOVIE = "BUNDLE_MOVIE"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}