package com.jashwant.jikan_compose

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jashwant.jikan_compose.models.Data

@Dao
interface AnimeListDao {

    @Insert
    suspend fun addAnimes(animes:List<Data>)

    @Query("SELECT * FROM anime")
    suspend fun getAnimes():List<Data>

}