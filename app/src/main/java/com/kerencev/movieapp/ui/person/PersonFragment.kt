package com.kerencev.movieapp.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.databinding.PersonFragmentBinding
import com.kerencev.movieapp.model.helpers.FormatBirthDate
import com.kerencev.movieapp.model.helpers.FormatHeight
import com.kerencev.movieapp.ui.adapters.MoviesAdapter
import com.kerencev.movieapp.ui.adapters.MoviesListAdapter
import com.kerencev.movieapp.ui.details.DetailsFragment
import com.kerencev.movieapp.viewmodels.PersonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_NAME_DATA = "BUNDLE_NAME_DATA"

class PersonFragment : Fragment(), CoroutineScope by MainScope() {
    private val viewModel: PersonViewModel by viewModel()
    private var _binding: PersonFragmentBinding? = null
    private val binding get() = _binding!!
    private var id: String? = null
    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PersonFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(BUNDLE_NAME_DATA, null)

        val nameDataObserver = Observer<NameData?> { renderData(it) }
        viewModel.nameData.observe(viewLifecycleOwner, nameDataObserver)
        id?.let { viewModel.getData(it) }

        val featuringMovieObserver = Observer<List<MovieApi>> { initRecycler(it) }
        viewModel.featMovieData.observe(viewLifecycleOwner, featuringMovieObserver)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(nameData: NameData?) = with(binding) {
        when (nameData) {
            null -> {
                Snackbar
                    .make(root, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { id?.let { viewModel.getData(it) } }
                    .show()
            }
            else -> {
                toolbar.title = nameData.name
                tvName.text = nameData.name
                tvRole.text = nameData.role
                tvSummary.text = nameData.summary
                awards.text = nameData.awards
                nameData.image?.let { loadPortrait(portrait, it) }
                tvBirthDate.text = nameData.birthDate?.let {
                    when {
                        it.isEmpty() -> "null"
                        else -> FormatBirthDate.init(it)
                    }
                }
                tvHeight.text = nameData.height?.let {
                    when {
                        it.isEmpty() -> "null"
                        else -> FormatHeight.init(it)
                    }
                }
            }
        }
    }

    private fun loadPortrait(imageView: ImageView, image: String) {
        launch {
            imageView.load(image) {
                crossfade(true)
                placeholder(R.drawable.person)
            }
        }
    }

    private fun initRecycler(data: List<MovieApi>) = with(binding) {
        adapter = MoviesAdapter(object : MoviesListAdapter.OnItemViewClickListener {
            override fun onItemViewClick(movie: MovieApi) {
                val bundle = Bundle().apply {
                    putString(DetailsFragment.BUNDLE_MOVIE, movie.id)
                }
                parentFragmentManager.beginTransaction()
                    .hide(this@PersonFragment)
                    .add(R.id.container, DetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        })
        recycler.adapter = adapter
        adapter.setData(data)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(id: String): PersonFragment {
            val fragment = PersonFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_NAME_DATA, id)
            fragment.arguments = bundle
            return fragment
        }
    }
}