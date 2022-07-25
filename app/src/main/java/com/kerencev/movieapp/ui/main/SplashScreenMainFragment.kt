package com.kerencev.movieapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.Observer
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.preferences.COMING_SOON_KEY
import com.kerencev.movieapp.data.preferences.MOST_POPULAR_KEY
import com.kerencev.movieapp.data.preferences.Pref
import com.kerencev.movieapp.data.preferences.TOP_250_KEY
import com.kerencev.movieapp.databinding.FragmentSplashScreenMainBinding
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.ui.ViewBindingFragment
import com.kerencev.movieapp.viewmodels.CATEGORY_COMING_SOON
import com.kerencev.movieapp.viewmodels.CATEGORY_MOST_POPULAR
import com.kerencev.movieapp.viewmodels.CATEGORY_TOP_250
import com.kerencev.movieapp.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenMainFragment :
    ViewBindingFragment<FragmentSplashScreenMainBinding>(FragmentSplashScreenMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var timerForProgress: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateImgMovie()
        animateProgressIndicator()
        val observer = Observer<MainState> {
            timerForProgress.cancel()
            binding.progressBar.progress = 100
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.alpha_to_1, R.animator.alpha_to_0)
                .replace(R.id.container, MainFragment(viewModel), MainFragment.MAIN_FRAGMENT_TAG)
                .commitAllowingStateLoss()
        }
        viewModel.liveData.observe(viewLifecycleOwner, observer)
        viewModel.getMovies(getCorrectCategoriesFromPref())
    }

    private fun animateImgMovie() = with(binding) {
        var flagForImgMovie = false
        object : CountDownTimer(20000, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                flagForImgMovie = !flagForImgMovie
                when (flagForImgMovie) {
                    true -> {
                        imgMovie.animate().rotation(-20f).setDuration(2000L).start()
                    }
                    false -> {
                        imgMovie.animate().rotation(20f).setDuration(2000L).start()
                    }
                }
            }

            override fun onFinish() {
                imgMovie.animate().rotation(0f).setDuration(2000L).start()
            }
        }.start()
    }

    private fun animateProgressIndicator() = with(binding) {
        var counter = 1
        timerForProgress = object : CountDownTimer(3000, 30) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = counter
                counter++
            }

            override fun onFinish() {}
        }.start()
    }

    private fun getCorrectCategoriesFromPref(): ArrayList<String> {
        val result = ArrayList<String>()
        if (Pref.getDataIsChecked(activity, TOP_250_KEY)) {
            result.add(CATEGORY_TOP_250)
        }
        if (Pref.getDataIsChecked(activity, MOST_POPULAR_KEY)) {
            result.add(CATEGORY_MOST_POPULAR)
        }
        if (Pref.getDataIsChecked(activity, COMING_SOON_KEY)) {
            result.add(CATEGORY_COMING_SOON)
        }
        return result
    }
}
