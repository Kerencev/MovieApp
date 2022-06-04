package com.kerencev.movieapp.views.details

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.databinding.DetailsFragmentBinding
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.model.receivers.LoadMovieDetailsBR
import com.kerencev.movieapp.viewmodels.DetailsViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModel()
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString(BUNDLE_MOVIE)
        val observer = Observer<DetailsState> { id?.let { id -> renderData(it, id) } }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        id?.let { viewModel.getMovieDetails(it) }
    }

    private fun renderData(detailsState: DetailsState, id: String) = with(binding) {
        when (detailsState) {
            is DetailsState.Success -> {
                val moviesData = detailsState.moviesData
                progressBarDetails.visibility = View.GONE
                setAllViewContent(moviesData)
            }
            is DetailsState.Loading -> {
                progressBarDetails.visibility = View.VISIBLE
            }
            is DetailsState.Error -> {
                progressBarDetails.visibility = View.GONE
                setErrorInfo(id)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllViewContent(moviesData: MovieDetailsApi?) = with(binding) {
        Picasso.get().load(moviesData?.image).into(poster)
        title.text = moviesData?.title
        rating.text = "${moviesData?.imDbRating} (${moviesData?.imDbRatingVotes})"
        details.text =
            "${moviesData?.year}, ${moviesData?.genres}, ${moviesData?.countries}, ${moviesData?.runtimeStr}"
        description.text = moviesData?.plotLocal
    }

    private fun setErrorInfo(id: String) = with(binding) {
        poster.setImageResource(R.drawable.movie)
        imageStar.visibility = View.INVISIBLE
        Snackbar
            .make(details, "Error", Snackbar.LENGTH_INDEFINITE)
            .setAction("Reload") { viewModel.getMovieDetails(id) }
            .show()
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