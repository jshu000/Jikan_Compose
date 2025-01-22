package com.jashwant.jikan_compose

import com.jashwant.jikan_compose.models.Data

sealed class UiState {
    data object Loading: UiState()
    data class Success(val response:List<Data>):UiState()
    data class Failed(val message:String? = null): UiState()
}