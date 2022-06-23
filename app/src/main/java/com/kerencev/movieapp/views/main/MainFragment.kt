package com.kerencev.movieapp.views.main

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.preferences.Pref
import com.kerencev.movieapp.databinding.MainFragmentBinding
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.model.receivers.LoadMovieDetailsBR
import com.kerencev.movieapp.model.receivers.NetworkChangeBR
import com.kerencev.movieapp.viewmodels.CATEGORY_COMING_SOON
import com.kerencev.movieapp.viewmodels.CATEGORY_MOST_POPULAR
import com.kerencev.movieapp.viewmodels.CATEGORY_TOP_250
import com.kerencev.movieapp.viewmodels.MainViewModel
import com.kerencev.movieapp.views.adapters.MoviesListAdapter
import com.kerencev.movieapp.views.settings.COMING_SOON_KEY
import com.kerencev.movieapp.views.settings.MOST_POPULAR_KEY
import com.kerencev.movieapp.views.settings.SettingsFragment
import com.kerencev.movieapp.views.settings.TOP_250_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), CoroutineScope by MainScope() {
    private val viewModel: MainViewModel by viewModel()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviesListAdapter
    private val receiverNetworkChange = NetworkChangeBR()
    private val receiverLoadMovieDetails = LoadMovieDetailsBR()
    private lateinit var categoriesToShow: ArrayList<String>

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
        categoriesToShow = getCorrectCategoriesFromPref()
        initRecyclerList()
        val observer = Observer<MainState> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getMovies(categoriesToShow)
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

    private fun initRecyclerList() {
        adapter = MoviesListAdapter(fragmentManager = parentFragmentManager)
        binding.recycler.adapter = adapter
    }

    private fun renderData(mainState: MainState) = with(binding) {
        when (mainState) {
            is MainState.Success -> {
                val moviesData = mainState.moviesData
                progressBar.visibility = View.GONE
                adapter.setData(moviesData)
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
                    .setAction(R.string.reload) { viewModel.getMovies(categoriesToShow) }
                    .show()
            }
        }
    }

    private fun getCorrectCategoriesFromPref(): ArrayList<String> {
        val result = ArrayList<String>()
        if(Pref.getDataIsChecked(activity, TOP_250_KEY)) {
            result.add(CATEGORY_TOP_250)
        }
        if(Pref.getDataIsChecked(activity, MOST_POPULAR_KEY)) {
            result.add(CATEGORY_MOST_POPULAR)
        }
        if(Pref.getDataIsChecked(activity, COMING_SOON_KEY)) {
            result.add(CATEGORY_COMING_SOON)
        }
        return result
    }

    companion object {
        fun newInstance() = MainFragment()
        const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
    }
}
