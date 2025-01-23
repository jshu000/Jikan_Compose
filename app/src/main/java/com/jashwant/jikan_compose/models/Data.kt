package com.jashwant.jikan_compose.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "anime")
data class Data(
    val episodes: Int,
    val images: Images,
    @PrimaryKey(autoGenerate = true)
    val mal_id: Int,
    val rating: String,
    val title: String,
    val title_english: String?,
    val url: String,
    val synopsis: String,
    val genres: List<Genre>,
    val trailer: Trailer,
)


