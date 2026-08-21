package com.example.wulwallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "costs")
data class Costs(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Float,
    val categoryId: Int
)