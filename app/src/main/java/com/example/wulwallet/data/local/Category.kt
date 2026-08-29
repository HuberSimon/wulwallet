// ================================================================
// FILE: data/local/Category.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val totalSum: Float = 0f
)