package com.kerencev.movieapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.MainActivityBinding
import com.kerencev.movieapp.ui.favorites.FavoritesFragment
import com.kerencev.movieapp.ui.history.HistoryFragment
import com.kerencev.movieapp.ui.search.SearchFragment
import com.kerencev.movieapp.ui.settings.SettingsFragment
import com.kerencev.movieapp.ui.test.TestFragment


class MainActivity : AppCompatActivity(), StatusBarHandler {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, SplashScreenMainFragment())
                .replace(R.id.container, TestFragment())
                .commitNow()
        }
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.favourites -> {
                    navigateTo(FavoritesFragment())
                }
                R.id.main -> {
                    navigateTo(SplashScreenMainFragment())
                }
                R.id.history -> {
                    navigateTo(HistoryFragment())
                }
                R.id.settings -> {
                    navigateTo(SettingsFragment())
                }
                R.id.search -> {
                    navigateTo(SearchFragment())
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.alpha_to_1_navigation, R.animator.alpha_to_0_navigation)
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }

    override fun showStatusBar(isShow: Boolean) {
        window.apply {
            when (isShow) {
                true -> decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                false -> decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    override fun statusBarColor(color: Int) {
        window.apply {
            statusBarColor = color
        }
    }
}