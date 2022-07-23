package com.kerencev.movieapp.views.test

import android.os.Build
import android.os.Bundle
import android.view.View
import com.kerencev.movieapp.databinding.FragmentTestBinding
import com.kerencev.movieapp.views.ViewBindingFragment

class TestFragment: ViewBindingFragment<FragmentTestBinding>(FragmentTestBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                binding.poster.alpha = ((1000 - scrollY.toFloat())/1000) + 0.5f
                if (scrollY >= binding.scroll.height - binding.toolbar.height + binding.title.height) {
                    binding.toolbar.title = binding.title.text.toString()
                } else {
                    binding.toolbar.title = null
                }
            }
        }
    }
}