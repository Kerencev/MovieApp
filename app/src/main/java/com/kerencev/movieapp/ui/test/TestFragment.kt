package com.kerencev.movieapp.ui.test

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import coil.load
import coil.transform.BlurTransformation
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.FragmentTestBinding
import com.kerencev.movieapp.ui.ViewBindingFragment
import com.kerencev.movieapp.ui.main.StatusBarHandler

class TestFragment : ViewBindingFragment<FragmentTestBinding>(FragmentTestBinding::inflate) {

    private var mainActivity: StatusBarHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as? StatusBarHandler)
        mainActivity?.showStatusBar(false)
        mainActivity?.statusBarColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setBackgroundResource(0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    poster.alpha = ((1000 - scrollY.toFloat()) / 1000) + 0.9f

                    val params = poster.layoutParams as CoordinatorLayout.LayoutParams
                    params.height = scroll.height - scrollY + 200
                    poster.layoutParams = params

                    if (scrollY >= scroll.height - scroll.y + toolbar.paddingTop + title.height) {
                        toolbar.title = title.text.toString()
                        toolbar.setBackgroundResource(R.color.toolbar)
                        mainActivity?.statusBarColor(resources.getColor(R.color.status_bar))
                    } else {
                        toolbar.title = null
                        toolbar.setBackgroundResource(0)
                        mainActivity?.statusBarColor(Color.TRANSPARENT)
                    }
                }
            }
            backgroundPoster.load(R.drawable.matrix) {
                transformations(
                    BlurTransformation(requireContext(), 25f, 10f)
                )
                build()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity?.showStatusBar(true)
        mainActivity?.statusBarColor(resources.getColor(R.color.status_bar))
    }
}