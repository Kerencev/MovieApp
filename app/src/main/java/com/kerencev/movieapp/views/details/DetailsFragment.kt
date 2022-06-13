package com.kerencev.movieapp.views.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.databinding.DetailsFragmentBinding
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.viewmodels.DetailsViewModel
import com.kerencev.movieapp.viewmodels.NoteViewModel
import com.kerencev.movieapp.views.dialogfragments.NoteDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment(), CoroutineScope by MainScope() {

    private val viewModel: DetailsViewModel by viewModel()
    private val noteViewModel: NoteViewModel by viewModel()
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val rollUP: String by lazy { resources.getString(R.string.roll_up) }
    private val unroll: String by lazy { resources.getString(R.string.unroll) }
    private val limitedActorsListHeight: Float by lazy { resources.getDimension(R.dimen.limited_actors_list_height) }
    private var id: String? = null

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
        id = arguments?.getString(BUNDLE_MOVIE)

        val dataObserver = Observer<DetailsState> { id?.let { id -> renderData(it, id) } }
        viewModel.liveData.observe(viewLifecycleOwner, dataObserver)
        id?.let { viewModel.getMovieDetails(it) }

        val nameDataObserver = Observer<List<NameData>> { listNameData ->
            if (viewModel.liveNameData.value?.size ?: 0 > 0) {
                renderDirectorsList(listNameData)
            }
        }
        viewModel.liveNameData.observe(viewLifecycleOwner, nameDataObserver)

        val isLikedMovieObserver = Observer<Boolean> { changeLikeIcon(it) }
        viewModel.liveDataIsLiked.observe(viewLifecycleOwner, isLikedMovieObserver)
        id?.let { viewModel.isLikedMovie(it) }

        setToolbarClicks()
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

    private fun renderDirectorsList(listNameData: List<NameData>?) = with(binding) {
        listNameData?.forEach { nameData ->
            launch(Dispatchers.Main) {
                val view = layoutInflater.inflate(R.layout.item_director, null, false)
                val directorImage = view.findViewById<ImageView>(R.id.image)
                val nameView = view.findViewById<TextView>(R.id.tv_name)
                directorImage.load(nameData.image) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                }
                nameView.text = nameData.name
                linearDirectorsList.addView(view)
            }
        }
    }

    private fun setAllClicks() = with(binding) {
        actionUnrollDescription.setOnClickListener {
            when (description.maxLines) {
                DESCRIPTION_UNLIMITED_HEIGHT -> {
                    description.maxLines = DESCRIPTION_LIMITED_HEIGHT
                    actionUnrollDescription.text = unroll
                }
                else -> {
                    description.maxLines = DESCRIPTION_UNLIMITED_HEIGHT
                    actionUnrollDescription.text = rollUP
                }
            }
        }
        actionUnrollCastGroup.setOnClickListener {
            val actionRollBtn = actionUnrollCastGroup.text.toString()
            if (actionRollBtn == unroll) {
                val params = linearActorsList.layoutParams
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                linearActorsList.layoutParams = params
                actionUnrollCastGroup.text = rollUP
                scroll.smoothScrollTo(0, linearFullActorTitle.top)
            } else {
                val params = linearActorsList.layoutParams
                params.height = limitedActorsListHeight.toInt()
                linearActorsList.layoutParams = params
                actionUnrollCastGroup.text = unroll
            }
        }
    }

    private fun setToolbarClicks() = with(binding) {
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.action_like -> viewModel.saveLikedMovieInDataBase()
                    R.id.action_add_note -> id?.let {
                        NoteDialogFragment.newInstance(it).show(
                            parentFragmentManager, ""
                        )
                    }
                }
                return true
            }
        })
    }

    private fun changeLikeIcon(isLiked: Boolean?) {
        when (isLiked) {
            true -> binding.toolbar.menu.getItem(0).icon = resources.getDrawable(R.drawable.favorite_check)
            false -> binding.toolbar.menu.getItem(0).icon = resources.getDrawable(R.drawable.ic_baseline_favorite_border_24)
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
                linearActorsList.addView(view)
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
        private const val DESCRIPTION_LIMITED_HEIGHT = 4
        private const val DESCRIPTION_UNLIMITED_HEIGHT = 1000

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}