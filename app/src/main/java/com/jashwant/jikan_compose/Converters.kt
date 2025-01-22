package com.jashwant.jikan_compose

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jashwant.jikan_compose.models.Images
import com.jashwant.jikan_compose.models.Jpg

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromJpg(jpg: Jpg?): String? {
        return jpg?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toJpg(json: String?): Jpg? {
        return json?.let { gson.fromJson(it, Jpg::class.java) }
    }

    @TypeConverter
    fun fromImages(images: Images?): String? {
        return images?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toImages(json: String?): Images? {
        return json?.let { gson.fromJson(it, Images::class.java) }
    }
}