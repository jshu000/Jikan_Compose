package com.jashwant.jikan_compose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jashwant.jikan_compose.repository.AnimeListRepository

class MainViewModelFactory(private val repository: AnimeListRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}