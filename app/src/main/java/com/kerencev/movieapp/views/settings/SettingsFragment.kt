package com.kerencev.movieapp.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kerencev.movieapp.data.preferences.Pref
import com.kerencev.movieapp.databinding.SettingsFragmentBinding

const val IS_SAVE_HISTORY_KEY = "IS_SAVE_HISTORY_KEY"
const val TOP_250_KEY = "TOP_250_KEY"
const val MOST_POPULAR_KEY = "MOST_POPULAR_KEY"
const val COMING_SOON_KEY = "COMING_SOON_KEY"

class SettingsFragment : Fragment() {
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
        checkboxTop250.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, TOP_250_KEY, isChecked)
        }
        checkboxMostPopular.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, MOST_POPULAR_KEY, isChecked)
        }
        checkboxComingSoon.setOnCheckedChangeListener { _, isChecked ->
            Pref.saveDataIsChecked(activity, COMING_SOON_KEY, isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRightSwitchPosition() {
        binding.switchSaveHistory.isChecked = Pref.getDataIsChecked(activity, IS_SAVE_HISTORY_KEY)
    }

    private fun setRightCheckBoxPosition() {
        binding.checkboxTop250.isChecked = Pref.getDataIsChecked(activity, TOP_250_KEY)
        binding.checkboxMostPopular.isChecked = Pref.getDataIsChecked(activity, MOST_POPULAR_KEY)
        binding.checkboxComingSoon.isChecked = Pref.getDataIsChecked(activity, COMING_SOON_KEY)
    }
}