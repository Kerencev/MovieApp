package com.kerencev.movieapp.views.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.database.entities.HistoryEntity
import com.kerencev.movieapp.databinding.HistoryFragmentBinding
import com.kerencev.movieapp.model.appstate.HistoryState
import com.kerencev.movieapp.viewmodels.HistoryViewModel
import com.kerencev.movieapp.views.adapters.HistoryAdapter
import com.kerencev.movieapp.views.details.DetailsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), CoroutineScope by MainScope() {
    private val viewModel: HistoryViewModel by viewModel()
    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HistoryAdapter(object : HistoryAdapter.OnItemHistoryClickListener {
            override fun onItemViewClick(movie: HistoryEntity) {
                parentFragmentManager.let { manager ->
                    val bundle = Bundle().apply {
                        putString(DetailsFragment.BUNDLE_MOVIE, movie.id)
                    }
                    manager.beginTransaction()
                        .hide(this@HistoryFragment)
                        .add(R.id.container, DetailsFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }
        })
        recycler.adapter = adapter
        val observer = Observer<HistoryState> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getHistoryFromDataBase()
    }

    private fun renderData(historyState: HistoryState) = with(binding) {
        when (historyState) {
            is HistoryState.Success -> {
                val moviesData = historyState.moviesData
                if (moviesData.isEmpty()) {
                    showEmptyMessage()
                    return
                }
                adapter.setData(moviesData)
            }
            is HistoryState.Loading -> {
            }
            is HistoryState.Error -> {
                Snackbar
                    .make(main, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getHistoryFromDataBase() }
                    .show()
            }
        }
    }

    private fun showEmptyMessage() = with(binding) {
        recycler.visibility = View.GONE
        tvEmpty.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}