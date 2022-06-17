package com.kerencev.movieapp.model.di

import androidx.room.Room
import com.kerencev.movieapp.data.database.DataBase
import com.kerencev.movieapp.model.repository.Repository
import com.kerencev.movieapp.model.repository.RepositoryImpl
import com.kerencev.movieapp.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            DataBase::class.java,
            "app.database.db",
        ).fallbackToDestructiveMigration().build()
    }
    single<Repository> { RepositoryImpl(get()) }
    //View Models
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { NoteViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}