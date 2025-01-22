package com.jashwant.jikan_compose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jashwant.jikan_compose.models.AnimeList
import com.jashwant.jikan_compose.models.Data
import retrofit2.Response

val TAG="JashwantJikan"
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
        }catch (e:Exception){
            Log.d(TAG, "getTopAnimeList: exceptionnnnn maybe Due to Internet issue  ${e.message}")
        }
        return result2
    }
    suspend fun getanimelist(): List<Data>? {
        var result= animeListDao.getAnimes()
        return result
    }
}