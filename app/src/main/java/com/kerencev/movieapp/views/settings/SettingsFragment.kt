package com.kerencev.movieapp.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.preferences.*
import com.kerencev.movieapp.databinding.SettingsFragmentBinding
import com.kerencev.movieapp.model.extensions.showSnackBar
import com.kerencev.movieapp.viewmodels.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModel()
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        setRightSwitchPosition()
        setRightCheckBoxPosition()
        switchSaveHistory.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, IS_SAVE_HISTORY_KEY, isChecked)
        }
        switchSaveSearchHistory.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, IS_SAVE_SEARCH_HISTORY_KEY, isChecked)
        }
        checkboxTop250.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, TOP_250_KEY, isChecked)
        }
        checkboxMostPopular.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, MOST_POPULAR_KEY, isChecked)
        }
        checkboxComingSoon.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, COMING_SOON_KEY, isChecked)
        }
        actionCleanWatchedHistory.setOnClickListener {
            viewModel.clearHistoryFromDataBase()
        }
        actionCleanSearchHistory.setOnClickListener {
            viewModel.clearSearchHistoryFromDataBase()
        }
        val cleanHistoryObserver = Observer<Boolean> { showSnackHistory(it, R.string.the_browsing_history_has_been_cleared) }
        viewModel.liveDataIsClearHistory.observe(viewLifecycleOwner, cleanHistoryObserver)
        val cleanSearchHistoryObserver = Observer<Boolean> { showSnackHistory(it, R.string.search_history_has_been_cleared) }
        viewModel.isClearSearchHistory.observe(viewLifecycleOwner, cleanSearchHistoryObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRightSwitchPosition() = with(binding) {
        switchSaveHistory.isChecked = Pref.getDataIsChecked(activity, IS_SAVE_HISTORY_KEY)
        switchSaveSearchHistory.isChecked = Pref.getDataIsChecked(activity, IS_SAVE_SEARCH_HISTORY_KEY)
    }

    private fun setRightCheckBoxPosition() {
        binding.checkboxTop250.isChecked = Pref.getDataIsChecked(activity, TOP_250_KEY)
        binding.checkboxMostPopular.isChecked = Pref.getDataIsChecked(activity, MOST_POPULAR_KEY)
        binding.checkboxComingSoon.isChecked = Pref.getDataIsChecked(activity, COMING_SOON_KEY)
    }

    private fun showSnackHistory(isHistoryEmpty: Boolean, resId: Int) = with(binding) {
        when (isHistoryEmpty) {
            true -> {
                main.showSnackBar(R.string.empty)
            }
            false -> {
                main.showSnackBar(resId)
            }
        }
    }
}