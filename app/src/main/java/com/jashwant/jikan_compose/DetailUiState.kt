package com.jashwant.jikan_compose

import com.jashwant.jikan_compose.models.Anime
import com.jashwant.jikan_compose.models.Data

sealed class DetailUiState {
    data object Loading: DetailUiState()
    data class Success(val response:List<Anime>):DetailUiState()
    data class Failed(val message:String? = null): DetailUiState()
}