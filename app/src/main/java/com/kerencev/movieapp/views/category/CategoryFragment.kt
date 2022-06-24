package com.kerencev.movieapp.views.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.list.MoviesListApi
import com.kerencev.movieapp.databinding.CategoryDetailsFragmentBinding
import com.kerencev.movieapp.views.adapters.MoviesAdapter
import com.kerencev.movieapp.views.adapters.MoviesListAdapter
import com.kerencev.movieapp.views.details.DetailsFragment

class CategoryFragment : Fragment() {
    private var _binding: CategoryDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CategoryDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getParcelable<MoviesListApi>(BUNDLE_CATEGORIES_FRAGMENT)
        data?.let { renderData(it) }
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(moviesList: MoviesListApi) = with(binding) {
        toolbar.title = moviesList.title
        val adapter = MoviesAdapter(object : MoviesListAdapter.OnItemViewClickListener {
            override fun onItemViewClick(movie: MovieApi) {
                parentFragmentManager.let { manager ->
                    val bundle = Bundle().apply {
                        putString(DetailsFragment.BUNDLE_MOVIE, movie.id)
                    }
                    manager.beginTransaction()
                        .hide(this@CategoryFragment)
                        .add(R.id.container, DetailsFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }
        })
        recycler.adapter = adapter
        moviesList.items?.let { adapter.setData(it) }
    }

    companion object {
        const val BUNDLE_CATEGORIES_FRAGMENT = "BUNDLE_CATEGORIES_FRAGMENT"
        fun newInstance(movies: MoviesListApi): CategoryFragment {
            val args = Bundle()
            args.putParcelable(BUNDLE_CATEGORIES_FRAGMENT, movies)
            val fragment = CategoryFragment()
            fragment.arguments = args
            return fragment
        }
    }
}