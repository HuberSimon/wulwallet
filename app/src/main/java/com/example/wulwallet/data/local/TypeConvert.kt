// ================================================================
// FILE: data/local/TypeConvert.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.TypeConverter

class TypeConvert {

    @TypeConverter
    fun fromFloat(
        value: Float?
    ): Double? {
        return value?.toDouble()
    }

    @TypeConverter
    fun toFloat(
        value: Double?
    ): Float? {
        return value?.toFloat()
    }
}