package com.jashwant.jikan_compose

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jashwant.jikan_compose.models.Data
import javax.inject.Singleton

@Singleton
@Database(entities = [Data::class], version = 1)
@TypeConverters(Converters::class)
abstract class AnimeDatabase:RoomDatabase() {

    abstract fun animeListDao():AnimeListDao

    companion object{
        @Volatile
        private var INSTANCE:AnimeDatabase ?= null

        fun getDatabase(context: Context):AnimeDatabase{
            if(INSTANCE==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        AnimeDatabase::class.java,"animeDB")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}