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
import kotlinx.coroutines.withTimeout
import retrofit2.Response

class MainViewModel(private val animeListRepository: AnimeListRepository):ViewModel(){

    var currentdata:Data?= null
    private val _animelist =MutableLiveData<List<Data>>()
    val animelist : LiveData<List<Data>>
        get()=animeListRepository.animelistrepo

    private var _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    private var _detailstate = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailstate = _detailstate.asStateFlow()

    private var _animelistflow = MutableStateFlow<List<Data>>(emptyList())
    val animelistflow = _animelistflow.asStateFlow()

    init {
        fetchAll()
    }

    fun fetchAll(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetchAll: ")
            with(_state) {
                emit(UiState.Loading)
                //remotedatafetching
                try {
                    var result = animeListRepository.fetchAnimeList()
                    if (result?.size!= 0) {
                        Log.d(TAG, "Result from repository ${result} ")

                        _animelistflow.value = result
                        emit(UiState.Success(_animelistflow.value))

                    }
                } catch (ex: Exception) {
                    Log.d(TAG, "Result from repository is zero")
                    emit(UiState.Failed(ex.message))
                }
            }
        }
    }
    fun fetchcurrentData(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetchAll: ")
            with(_detailstate) {
                emit(DetailUiState.Loading)
                //remotedatafetching
                try {
                    var result = animeListRepository.fetchAnimeList()
                    if (result?.size!= 0) {
                        Log.d(TAG, "Result from repository of Current data${currentdata} ")

                        emit(DetailUiState.Success(currentdata!!))
                    }
                } catch (ex: Exception) {
                    Log.d(TAG, "Result from repository of current data is zero")
                    emit(DetailUiState.Failed(ex.message))
                }
            }
        }
    }





}