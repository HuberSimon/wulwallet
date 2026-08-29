// ================================================================
// FILE: data/local/User.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isMainUser: Boolean = false,
    val totalCosts: Float = 0f
)