package com.kerencev.movieapp.views.details

import android.annotation.SuppressLint
import android.content.Context
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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.data.preferences.IS_SAVE_HISTORY_KEY
import com.kerencev.movieapp.databinding.DetailsFragmentBinding
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.viewmodels.DetailsViewModel
import com.kerencev.movieapp.views.dialogfragments.NoteDialogFragment
import com.kerencev.movieapp.views.person.PersonFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment(), CoroutineScope by MainScope() {

    private val viewModel: DetailsViewModel by viewModel()
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val rollUP: String by lazy { resources.getString(R.string.roll_up) }
    private val unroll: String by lazy { resources.getString(R.string.unroll) }
    private val limitedActorsListHeight: Float by lazy { resources.getDimension(R.dimen.limited_actors_list_height) }
    private var id: String? = null
    private var isSaveHistory = true

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
        isSaveHistory = getIsSaveHistoryFromPref()
        id = arguments?.getString(BUNDLE_MOVIE)
        initMainDataObserver()
        initDirectorsDataObserver()
        initLikedMovieDataObserver()
        initUserRatingDataObserver()
        initListenerOnChangeUserRating()
        setToolbarClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLikedMovieDataObserver() {
        val isLikedMovieObserver = Observer<Boolean> { changeLikeIcon(it) }
        viewModel.liveDataIsLiked.observe(viewLifecycleOwner, isLikedMovieObserver)
        id?.let { viewModel.isLikedMovie(it) }
    }

    private fun initMainDataObserver() {
        val dataObserver = Observer<DetailsState> { id?.let { id -> renderData(it, id) } }
        viewModel.liveData.observe(viewLifecycleOwner, dataObserver)
        id?.let { viewModel.getMovieDetails(it, isSaveHistory) }
    }

    private fun initDirectorsDataObserver() {
        val nameDataObserver = Observer<List<NameData>> { listNameData ->
            if (viewModel.liveNameData.value?.size ?: 0 > 0) {
                renderDirectorsList(listNameData)
            }
        }
        viewModel.liveNameData.observe(viewLifecycleOwner, nameDataObserver)
    }

    private fun initUserRatingDataObserver() {
        val noteObserver = Observer<NoteEntity?> { renderRatingView(it) }
        viewModel.noteData.observe(viewLifecycleOwner, noteObserver)
        id?.let { viewModel.getNote(it) }
    }

    private fun initListenerOnChangeUserRating() {
        parentFragmentManager.setFragmentResultListener(
            NoteDialogFragment.RESULT_SAVED_RATING,
            viewLifecycleOwner
        ) { requestKey, result ->
            viewModel.getNote(
                result.getString(
                    NoteDialogFragment.BUNDLE_NOTE_ID,
                    ""
                )
            )
        }
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

    private fun renderRatingView(note: NoteEntity?) = with(binding) {
        if (note == null) {
            return
        }
        when (note.rating) {
            0 -> userRating.visibility = View.GONE
            else -> {
                userRating.visibility = View.VISIBLE
                userRating.text = note.rating.toString()
                changeUserRatingTextColor(note.rating)
            }
        }
        when {
            note.note.isEmpty() -> userNote.visibility = View.GONE
            else -> {
                userNote.visibility = View.VISIBLE
                userNote.text = note.note
            }
        }
    }

    private fun changeUserRatingTextColor(rating: Int) = with(binding) {
        when {
            rating == 0 -> {
                userRating.setTextColor(resources.getColor(R.color.black))
                return
            }
            rating < 5 -> userRating.setTextColor(resources.getColor(R.color.red))
            else -> userRating.setTextColor(resources.getColor(R.color.green))
        }
    }

    private fun renderDirectorsList(listNameData: List<NameData>?) = with(binding) {
        listNameData?.forEach { nameData ->
            launch(Dispatchers.Main) {
                val root = layoutInflater.inflate(R.layout.item_director, null, false)
                val directorImage = root.findViewById<ImageView>(R.id.image)
                val nameView = root.findViewById<TextView>(R.id.tv_name)
                directorImage.load(nameData.image) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                    error(R.drawable.movie)
                }
                nameView.text = nameData.name
                linearDirectorsList.addView(root)
                root.setOnClickListener {
                    nameData.id?.let {
                        parentFragmentManager.beginTransaction()
                            .hide(this@DetailsFragment)
                            .add(R.id.container, PersonFragment.newInstance(it))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
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
        cardUserRating.setOnClickListener {
            id?.let { id ->
                NoteDialogFragment.newInstance(id).show(
                    parentFragmentManager, ""
                )
            }
        }
    }

    private fun setToolbarClicks() = with(binding) {
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.action_like -> viewModel.saveLikedMovieInDataBase()
                }
                return true
            }
        })
    }

    private fun changeLikeIcon(isLiked: Boolean?) {
        when (isLiked) {
            true -> binding.toolbar.menu.getItem(0).icon =
                resources.getDrawable(R.drawable.favorite_check)
            false -> binding.toolbar.menu.getItem(0).icon =
                resources.getDrawable(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllViewContent(moviesData: MovieDetailsApi?) = with(binding) {
        launch(Dispatchers.Main) {
            moviesData?.image?.let {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.movie)
                    .fitCenter()
                    .into(poster)
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
                val root = layoutInflater.inflate(R.layout.item_cast_group, null, false)
                val actorImage = root.findViewById<ImageView>(R.id.image)
                val name = root.findViewById<TextView>(R.id.name)
                val nameAsCharacter = root.findViewById<TextView>(R.id.name_character)
                actorImage.load(actor.image) {
                    crossfade(true)
                    placeholder(R.drawable.movie)
                }
                name.text = actor.name
                nameAsCharacter.text = actor.asCharacter
                linearActorsList.addView(root)
                root.setOnClickListener {
                    actor.id?.let {
                        parentFragmentManager.beginTransaction()
                            .hide(this@DetailsFragment)
                            .add(R.id.container, PersonFragment.newInstance(it))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
            }
        }
    }

    private fun setErrorInfo(id: String) = with(binding) {
        poster.setImageResource(R.drawable.movie)
        imageStar.visibility = View.INVISIBLE
        Snackbar
            .make(details, "Error", Snackbar.LENGTH_INDEFINITE)
            .setAction("Reload") { viewModel.getMovieDetails(id, isSaveHistory) }
            .show()
    }

    private fun getIsSaveHistoryFromPref(): Boolean {
        return activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getBoolean(
            IS_SAVE_HISTORY_KEY, true
        ) ?: true
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