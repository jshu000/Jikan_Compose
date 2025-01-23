package com.jashwant.jikan_compose

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jashwant.jikan_compose.models.Genre
import com.jashwant.jikan_compose.models.Images
import com.jashwant.jikan_compose.models.ImagesX
import com.jashwant.jikan_compose.models.Jpg
import com.jashwant.jikan_compose.models.Trailer

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

    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String? {
        if (genres == null) return null
        val gson = Gson()
        return gson.toJson(genres)
    }

    @TypeConverter
    fun toGenreList(genreString: String?): List<Genre>? {
        if (genreString == null) return null
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genreString, type)
    }

    @TypeConverter
    fun fromTrailer(trailer: Trailer?): String? {
        if (trailer == null) return null
        val gson = Gson()
        return gson.toJson(trailer)
    }

    @TypeConverter
    fun toTrailer(trailerString: String?): Trailer? {
        if (trailerString == null) return null
        val gson = Gson()
        return gson.fromJson(trailerString, Trailer::class.java)
    }

    @TypeConverter
    fun fromImagesX(imagesX: ImagesX?): String? {
        if (imagesX == null) return null
        val gson = Gson()
        return gson.toJson(imagesX)
    }

    @TypeConverter
    fun toImagesX(imagesXString: String?): ImagesX? {
        if (imagesXString == null) return null
        val gson = Gson()
        return gson.fromJson(imagesXString, ImagesX::class.java)
    }
}