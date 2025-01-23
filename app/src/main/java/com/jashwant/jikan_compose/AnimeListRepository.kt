package com.jashwant.jikan_compose

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jashwant.jikan_compose.models.AnimeList
import com.jashwant.jikan_compose.models.Data
import retrofit2.Response

class AnimeListRepository(
    private val apiService: ApiService,
    private val animeListDao: AnimeListDao
) {
    private val _animelistrepo = MutableLiveData<List<Data>>()
    val animelistrepo : LiveData<List<Data>>
        get()=_animelistrepo


    suspend fun fetchAnimeList(): List<Data> {
        var result2 = emptyList<Data>()
        try {
            val result= apiService.getTopAnimeList()
            if(result.body()!=null){
                Log.d(TAG, "Data is now updated to DB: ${result.body()!!.data}")
                animeListDao.addAnimes(result.body()!!.data)
                _animelistrepo.postValue(animeListDao.getAnimes())
                result2=result.body()!!.data
            }
        }catch (e:NetworkErrorException){
            Log.d(TAG, "getTopAnimeList: exceptionnnnn maybe Due to Internet issue1  ${e.message}")
        }catch (e:Exception){
            Log.d(TAG, "getTopAnimeList: exceptionnnnn maybe Due to Internet issue2  ${e.message}")
        }
        return animeListDao.getAnimes()
    }
    suspend fun getanimelist(): List<Data>? {
        return animeListDao.getAnimes()
    }
}