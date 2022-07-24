package com.kerencev.movieapp.views.test

import android.graphics.BlurMaskFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import coil.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.FragmentTestBinding
import com.kerencev.movieapp.views.ViewBindingFragment

class TestFragment : ViewBindingFragment<FragmentTestBinding>(FragmentTestBinding::inflate) {

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

                    if (scrollY >= scroll.height - scroll.y) {
                        toolbar.title = title.text.toString()
                        toolbar.setBackgroundResource(R.color.toolbar)
                        backgroundPoster.visibility = View.INVISIBLE
                    } else {
                        toolbar.title = null
                        toolbar.setBackgroundResource(0)
                        backgroundPoster.visibility = View.VISIBLE
                    }
                }
            }
            backgroundPoster.load("https://images.freeimages.com/images/large-previews/825/linked-hands-1308777.jpg") {
                transformations(
                    BlurTransformation(requireContext(), 25f)
                )
                build()
            }
        }
    }
}