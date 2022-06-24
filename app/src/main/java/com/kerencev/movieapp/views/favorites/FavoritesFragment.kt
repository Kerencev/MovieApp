package com.kerencev.movieapp.views.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.databinding.FavoritesFragmentBinding
import com.kerencev.movieapp.model.appstate.FavoritesState
import com.kerencev.movieapp.viewmodels.FavoritesViewModel
import com.kerencev.movieapp.views.adapters.FavoritesAdapter
import com.kerencev.movieapp.views.details.DetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        val observer = Observer<FavoritesState> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getFavoritesMovieFromDataBase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        adapter = FavoritesAdapter(object : FavoritesAdapter.OnItemFavoriteClickListener {
            override fun onItemViewClick(movie: MovieApi) {
                parentFragmentManager.let { manager ->
                    val bundle = Bundle().apply {
                        putString(DetailsFragment.BUNDLE_MOVIE, movie.id)
                    }
                    manager.beginTransaction()
                        .hide(this@FavoritesFragment)
                        .add(R.id.container, DetailsFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }
        })
        binding.recycler.adapter = adapter
    }

    private fun renderData(favoritesState: FavoritesState) = with(binding) {
        when (favoritesState) {
            is FavoritesState.Success -> {
                tvEmpty.visibility = View.GONE
                adapter.setData(favoritesState.moviesData)
            }
            is FavoritesState.Loading -> {
            }
            is FavoritesState.Error -> {
                Snackbar
                    .make(main, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getFavoritesMovieFromDataBase() }
                    .show()
            }
        }
    }
}