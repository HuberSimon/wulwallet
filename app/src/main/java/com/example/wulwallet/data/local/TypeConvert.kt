package com.example.wulidee.data.local

import androidx.room.TypeConverter
import java.util.*

class TypeConvert {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}