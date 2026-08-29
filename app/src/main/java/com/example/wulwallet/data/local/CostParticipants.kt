// ================================================================
// FILE: data/local/CostParticipant.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.Entity

@Entity(
    tableName = "cost_participants",
    primaryKeys = [
        "costId",
        "userId"
    ]
)
data class CostParticipant(

    val costId: Int,

    val userId: Int,

    val share: Float
)