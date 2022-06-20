package com.kerencev.movieapp.views.main

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.databinding.MainFragmentBinding
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.model.receivers.LoadMovieDetailsBR
import com.kerencev.movieapp.model.receivers.NetworkChangeBR
import com.kerencev.movieapp.viewmodels.MainViewModel
import com.kerencev.movieapp.views.adapters.MoviesListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), CoroutineScope by MainScope() {
    private val viewModel: MainViewModel by viewModel()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviesListAdapter
    private val receiverNetworkChange = NetworkChangeBR()
    private val receiverLoadMovieDetails = LoadMovieDetailsBR()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.registerReceiver(
            receiverNetworkChange,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
        activity?.registerReceiver(
            receiverLoadMovieDetails,
            IntentFilter("com.kerencev.movieapp.load.movie.details")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList()
        val observer = Observer<MainState> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getMovies()
    }

    private fun initRecyclerList() {
        adapter = MoviesListAdapter(fragmentManager = parentFragmentManager)
        binding.recycler.adapter = adapter
    }

    private fun renderData(mainState: MainState) = with(binding) {
        when (mainState) {
            is MainState.Success -> {
                val moviesData = mainState.moviesData
                progressBar.visibility = View.GONE
                adapter.setData(moviesData as List<List<MovieApi>>)
                adapter.notifyDataSetChanged()
            }
            is MainState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is MainState.Error -> {
                progressBar.visibility = View.GONE
                Snackbar
                    .make(
                        main,
                        R.string.data_could_not_be_retrieved_check_your_internet_connection,
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction(R.string.reload) { viewModel.getMovies() }
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(receiverNetworkChange)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.unregisterReceiver(receiverLoadMovieDetails)
    }
}
