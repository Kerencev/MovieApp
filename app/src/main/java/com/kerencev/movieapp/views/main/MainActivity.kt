package com.kerencev.movieapp.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kerencev.movieapp.R
import com.kerencev.movieapp.views.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}