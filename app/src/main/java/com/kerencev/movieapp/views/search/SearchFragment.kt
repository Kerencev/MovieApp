package com.kerencev.movieapp.views.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.loaders.entities.search.SearchedMovies
import com.kerencev.movieapp.databinding.SearchFragmentBinding
import com.kerencev.movieapp.model.helpers.Keyboard
import com.kerencev.movieapp.viewmodels.SearchViewModel
import com.kerencev.movieapp.views.adapters.SearchAdapter
import com.kerencev.movieapp.views.details.DetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        Keyboard.showKeyBoard(requireActivity(), editSearch)
        actionSearch.setOnClickListener {
            progress.visibility = View.VISIBLE
            Keyboard.hideKeyBoard(requireActivity(), editSearch)
            recycler.visibility = View.GONE
            tvEmptyMessage.visibility = View.GONE
            viewModel.getData(binding.editSearch.text.toString())
        }
        initAdapter()
        recycler.adapter = adapter
        val observer = Observer<SearchedMovies?> { renderData(it) }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
    }

    private fun initAdapter() {
        adapter = SearchAdapter(object : SearchAdapter.OnItemSearchClickListener {
            override fun onItemViewClick(id: String) {
                val bundle = Bundle()
                bundle.putString(DetailsFragment.BUNDLE_MOVIE, id)
                parentFragmentManager.beginTransaction()
                    .hide(this@SearchFragment)
                    .add(R.id.container, DetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        })
    }

    private fun renderData(data: SearchedMovies?) {
        binding.progress.visibility = View.GONE
        when {
            data == null -> showError()
            data.results?.isEmpty() == true -> showEmptyMessage()
            else -> notifyAdapter(data)
        }
    }

    private fun notifyAdapter(dto: SearchedMovies) = with(binding) {
        recycler.visibility = View.VISIBLE
        tvEmptyMessage.visibility = View.GONE
        adapter.setData(dto)
    }

    private fun showEmptyMessage() = with(binding) {
        recycler.visibility = View.GONE
        tvEmptyMessage.visibility = View.VISIBLE
    }

    private fun showError() = with(binding) {
        val snackBar = Snackbar.make(
            root,
            R.string.data_could_not_be_retrieved_check_your_internet_connection,
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(R.string.reload) {
            viewModel.getData(editSearch.text.toString())
            snackBar.dismiss()
        }
        snackBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}