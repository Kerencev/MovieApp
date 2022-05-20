package com.kerencev.movieapp

import com.kerencev.movieapp.model.repository.Repository
import com.kerencev.movieapp.model.repository.RepositoryImpl
import com.kerencev.movieapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Repository> { RepositoryImpl() }

    //View Models
    viewModel { MainViewModel(get()) }
}