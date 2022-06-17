package com.kerencev.movieapp.views.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kerencev.movieapp.databinding.SettingsFragmentBinding

const val IS_SAVE_HISTORY_KEY = "IS_SAVE_HISTORY_KEY"

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
        switchSaveHistory.setOnCheckedChangeListener { _, isChecked ->
            saveDataToSPres(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveDataToSPres(isChecked: Boolean) {
        val editor = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(IS_SAVE_HISTORY_KEY, isChecked)
        editor?.apply()
    }

    private fun setRightSwitchPosition() {
        val isChecked =
            activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getBoolean(
                IS_SAVE_HISTORY_KEY, true
            ) ?: true
        binding.switchSaveHistory.isChecked = isChecked
    }
}