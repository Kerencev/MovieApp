package com.kerencev.movieapp.views.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.databinding.DetailsFragmentBinding
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.viewmodels.DetailsViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment(), CoroutineScope by MainScope() {

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
        val dataObserver = Observer<DetailsState> { id?.let { id -> renderData(it, id) } }
        viewModel.liveData.observe(viewLifecycleOwner, dataObserver)
        id?.let { viewModel.getMovieDetails(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(detailsState: DetailsState, id: String) = with(binding) {
        when (detailsState) {
            is DetailsState.Success -> {
                val moviesData = detailsState.moviesData
                progressBarDetails.visibility = View.GONE
                setAllViewContent(moviesData)
                setAllClicks()
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

    private fun setAllClicks() = with(binding) {
        actionUnrollDescription.setOnClickListener {
            if (description.maxLines == 200) {
                description.maxLines = 4
                actionUnrollDescription.setText(R.string.unroll)
            } else {
                description.maxLines = 200
                actionUnrollDescription.setText(R.string.roll_up)
            }
        }
        actionUnrollCastGroup.setOnClickListener {
            val s = actionUnrollCastGroup.text.toString()
            if (s.equals("Развернуть")) {
                val params = linear.layoutParams
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                linear.layoutParams = params
                actionUnrollCastGroup.text = "Свернуть"
                scroll.smoothScrollTo(0, linear.bottom)
            } else {
                val params = linear.layoutParams
                params.height = 300
                linear.layoutParams = params
                actionUnrollCastGroup.text = "Развернуть"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllViewContent(moviesData: MovieDetailsApi?) = with(binding) {
        launch(Dispatchers.Main) {
            poster.load(moviesData?.image) {
                crossfade(true)
                placeholder(R.drawable.movie)
            }
        }
        title.text = moviesData?.title
        rating.text = "${moviesData?.imDbRating} (${moviesData?.imDbRatingVotes})"
        details.text =
            "${moviesData?.year}, ${moviesData?.genres}, ${moviesData?.countries}, ${moviesData?.runtimeStr}"
        description.text = moviesData?.plotLocal
        setActorsList(moviesData)
    }

    private fun setActorsList(moviesData: MovieDetailsApi?) = with(binding) {
        val actorsList = moviesData?.actorList
        actorsList?.forEach { actor ->
            launch(Dispatchers.Main) {
                val view = layoutInflater.inflate(R.layout.item_cast_group, null, false)
                val actorImage = view.findViewById<ImageView>(R.id.image)
                val name = view.findViewById<TextView>(R.id.name)
                val nameAsCharacter = view.findViewById<TextView>(R.id.name_character)
                actorImage.load(actor.image) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                }
                name.text = actor.name
                nameAsCharacter.text = actor.asCharacter
                linear.addView(view)
            }
        }
    }

    private fun setErrorInfo(id: String) = with(binding) {
        poster.setImageResource(R.drawable.movie)
        imageStar.visibility = View.INVISIBLE
        Snackbar
            .make(details, "Error", Snackbar.LENGTH_INDEFINITE)
            .setAction("Reload") { viewModel.getMovieDetails(id) }
            .show()
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