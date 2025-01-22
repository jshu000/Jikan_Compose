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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainViewModel(private val animeListRepository: AnimeListRepository):ViewModel(){

    private val _animelist =MutableLiveData<List<Data>>()
    val animelist : LiveData<List<Data>>
        get()=animeListRepository.animelistrepo

    private var _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    private var _animelistflow = MutableStateFlow<List<Data>>(emptyList())
    val animelistflow = _animelistflow.asStateFlow()

    init {
        getAll()
        fetchAll()
    }

    fun fetchAll(){
        viewModelScope.launch(Dispatchers.IO) {
            animeListRepository.fetchAnimeList()
            with(_state) {
                emit(UiState.Loading)
                try {
                    val result = async { animeListRepository.fetchAnimeList() }.await()
                    if (result.size!= 0) {
                        Log.d("jashwant", "Result from remote ${result} ")

                        _animelistflow.value = result
                        emit(UiState.Success(_animelistflow.value))

                    } else {
                        Log.e("jashwant", "Result from Remote size is zero")
                    }

                } catch (ex: Exception) {
                    emit(UiState.Failed(ex.message))
                }
            }
        }
    }
    fun getAll(){
        viewModelScope.launch(Dispatchers.IO) {
            animeListRepository.getanimelist()
            with(_state) {
                emit(UiState.Loading)
                try {
                    val result = async { animeListRepository.getanimelist() }.await()
                    if (result?.size!= 0) {
                        Log.d("jashwant", "Result from local ${result}")

                        _animelistflow.value = result!!
                        emit(UiState.Success(_animelistflow.value))

                    } else {
                        Log.e("jashwant", "Result from local size is zero")
                    }

                } catch (ex: Exception) {
                    emit(UiState.Failed(ex.message))
                }
            }
        }
    }





}