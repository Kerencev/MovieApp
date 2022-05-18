package com.kerencev.movieapp

import com.kerencev.movieapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //View Models
    viewModel { MainViewModel() }
}