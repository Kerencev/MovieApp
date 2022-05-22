package com.kerencev.movieapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.MainFragmentBinding
import com.kerencev.movieapp.model.AppState
import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.model.entities.MoviesList
import com.kerencev.movieapp.ui.adapters.MoviesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }

    private val viewModel: MainViewModel by viewModel()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MoviesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerList(view)

        val observer = Observer<AppState> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getMovies()
    }

    //TODO Надо ли инициализировать и пользоваться recycler и adapter здесь, или перенести всё в MainViewModel ?
    private fun initRecyclerList(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = MoviesListAdapter(fragmentManager = parentFragmentManager)
        recyclerView.adapter = adapter
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                val moviesData: List<MoviesList> = appState.moviesData
                progressBar.visibility = View.GONE
                adapter.setData(moviesData)
                adapter.notifyDataSetChanged()
            }

            is AppState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                progressBar.visibility = View.GONE
                Snackbar
                    .make(main, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getMovies() }
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
