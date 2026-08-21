package com.example.wulidee.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val pinEncrypted: Int,
    val pinLock: Boolean,
    val reminderEnabled: Boolean
)