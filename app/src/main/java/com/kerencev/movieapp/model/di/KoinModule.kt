package com.kerencev.movieapp.model.di

import com.kerencev.movieapp.model.repository.Repository
import com.kerencev.movieapp.model.repository.RepositoryImpl
import com.kerencev.movieapp.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Repository> { RepositoryImpl() }

    //View Models
    viewModel { MainViewModel(get()) }
}