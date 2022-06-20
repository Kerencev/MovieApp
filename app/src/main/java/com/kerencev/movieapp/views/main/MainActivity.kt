package com.kerencev.movieapp.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.MainActivityBinding
import com.kerencev.movieapp.model.extensions.showToast
import com.kerencev.movieapp.views.favorites.FavoritesFragment
import com.kerencev.movieapp.views.history.HistoryFragment
import com.kerencev.movieapp.views.person.PersonFragment
import com.kerencev.movieapp.views.settings.SettingsFragment

const val ID_NAME_DATA = "nm0000154"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), MainFragment.MAIN_FRAGMENT_TAG)
//                .replace(R.id.container, FavoritesFragment())
//                .replace(R.id.container, PersonFragment.newInstance(ID_NAME_DATA))
                .commitNow()
        }
        val tickedId = intent.extras?.getString("tickedId", "0")
        this.showToast(tickedId.toString())
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
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}