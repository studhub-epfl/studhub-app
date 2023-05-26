package com.studhub.app.data.local.database

import android.net.Uri
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringToListOfStrings(value: String?): List<String>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun listOfStringsToString(list: List<String>?): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToMapOfStringBool(value: String?): Map<String, Boolean>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun mapOfStringBoolToString(map: Map<String, Boolean>?): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun listOfUrisToString(uris: List<Uri>?): String {
        return Json.encodeToString(uris?.map { it.toString() })
    }

    @TypeConverter
    fun listOfUrisFromString(value: String?): List<Uri> {
        if (value == null) return emptyList()
        return Json.decodeFromString<List<String>>(value).map { uri -> Uri.parse(uri) }
    }
}
