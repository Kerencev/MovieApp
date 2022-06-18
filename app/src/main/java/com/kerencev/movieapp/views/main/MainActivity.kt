package com.kerencev.movieapp.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.MainActivityBinding
import com.kerencev.movieapp.views.favorites.FavoritesFragment
import com.kerencev.movieapp.views.history.HistoryFragment
import com.kerencev.movieapp.views.maps.MapsFragment
import com.kerencev.movieapp.views.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MapsFragment())
                .replace(R.id.container, MainFragment.newInstance(), MainFragment.MAIN_FRAGMENT_TAG)
                .commitNow()
        }

        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FavoritesFragment())
                        .commitAllowingStateLoss()
                }
                R.id.main -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            MainFragment.newInstance(),
                            MainFragment.MAIN_FRAGMENT_TAG
                        )
                        .commitAllowingStateLoss()
                }
                R.id.history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .commitAllowingStateLoss()
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment())
                        .commitAllowingStateLoss()
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MapsFragment())
                        .commitAllowingStateLoss()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}