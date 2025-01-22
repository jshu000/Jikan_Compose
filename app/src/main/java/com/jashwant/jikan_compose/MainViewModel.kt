package com.jashwant.jikan_compose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jashwant.jikan_compose.models.AnimeList
import com.jashwant.jikan_compose.models.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val animeListRepository: AnimeListRepository):ViewModel(){

    private val _animelist =MutableLiveData<List<Data>?>()
    val animelist : LiveData<List<Data>?>
        get()=animeListRepository.animelistrepo



    fun getanimeList(){
        viewModelScope.launch(Dispatchers.IO) {
            animeListRepository.getTopAnimeList()
        }
    }



}