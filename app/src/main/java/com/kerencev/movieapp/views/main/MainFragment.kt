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
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.databinding.MainFragmentBinding
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.model.receivers.NetworkChangeBR
import com.kerencev.movieapp.viewmodels.MainViewModel
import com.kerencev.movieapp.views.adapters.MoviesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviesListAdapter
    private val receiver = NetworkChangeBR()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.registerReceiver(receiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
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
        initRecyclerList(view)
        val observer = Observer<MainState> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getMovies()
    }

    private fun initRecyclerList(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = MoviesListAdapter(fragmentManager = parentFragmentManager)
        recyclerView.adapter = adapter
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
                    .make(main, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getMovies() }
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
        activity?.unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
