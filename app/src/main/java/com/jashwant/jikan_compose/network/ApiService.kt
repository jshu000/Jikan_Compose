package com.jashwant.jikan_compose.network

import com.jashwant.jikan_compose.models.AnimeList
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {


    @GET("v4/top/anime/")
    suspend fun getTopAnimeList():Response<AnimeList>


}