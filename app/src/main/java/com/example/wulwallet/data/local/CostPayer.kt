// ================================================================
// FILE: data/local/CostPayer.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.Entity

@Entity(
    tableName = "cost_payers",
    primaryKeys = [
        "costId",
        "userId"
    ]
)
data class CostPayer(

    val costId: Int,

    val userId: Int,

    val share: Float
)