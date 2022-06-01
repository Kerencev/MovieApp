package com.kerencev.movieapp.views.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.databinding.DetailsFragmentBinding
import com.kerencev.movieapp.model.AppState
import com.kerencev.movieapp.viewmodels.DetailsViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModel()
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString(BUNDLE_MOVIE)
        val observer = Observer<AppState> { id?.let { id -> renderData(it, id) } }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        id?.let { viewModel.getMovieDetails(it) }
    }

    private fun renderData(appState: AppState, id: String) = with(binding) {
        when (appState) {
            is AppState.SuccessLoadMovieDetailsApi -> {
                val moviesData = appState.moviesData
                progressBarDetails.visibility = View.GONE
                setAllViewContent(moviesData)
            }

            is AppState.Loading -> {
                progressBarDetails.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                progressBarDetails.visibility = View.GONE
                Snackbar
                    .make(details, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getMovieDetails(id) }
                    .show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllViewContent(moviesData: MovieDetailsApi?) = with(binding) {
        Picasso.get().load(moviesData?.image).into(poster)
        title.text = moviesData?.title
        rating.text = "${moviesData?.imDbRating} (${moviesData?.imDbRatingVotes})"
        details.text = "${moviesData?.year}, ${moviesData?.genres}, ${moviesData?.countries}, ${moviesData?.runtimeStr}"
        description.text = moviesData?.plotLocal
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