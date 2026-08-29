// ================================================================
// FILE: data/local/CostsDao.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface CostsDao {

    // ============================================================
    // COSTS
    // ============================================================

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertCosts(
        costs: Costs
    ): Long

    @Update
    suspend fun updateCosts(
        costs: Costs
    )

    @Delete
    suspend fun deleteCosts(
        costs: Costs
    )

    @Query(
        """
        SELECT *
        FROM costs
        WHERE categoryId = :categoryId
        ORDER BY id DESC
        """
    )
    suspend fun getCostsByCategory(
        categoryId: Int
    ): List<Costs>

    @Query(
        """
        SELECT *
        FROM costs
        ORDER BY id DESC
        """
    )
    suspend fun getAllCosts(): List<Costs>


    // ============================================================
    // PAYERS
    // ============================================================

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertPayers(
        payers: List<CostPayer>
    )

    @Query(
        """
        SELECT *
        FROM cost_payers
        WHERE costId = :costId
        """
    )
    suspend fun getPayers(
        costId: Int
    ): List<CostPayer>

    @Query(
        """
        SELECT *
        FROM cost_payers
        WHERE userId = :userId
        """
    )
    suspend fun getPayersForUser(
        userId: Int
    ): List<CostPayer>

    @Query(
        """
        DELETE FROM cost_payers
        WHERE costId = :costId
        """
    )
    suspend fun deletePayersForCost(
        costId: Int
    )


    // ============================================================
    // PARTICIPANTS
    // ============================================================

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertParticipants(
        participants: List<CostParticipant>
    )

    @Query(
        """
        SELECT *
        FROM cost_participants
        WHERE costId = :costId
        """
    )
    suspend fun getParticipants(
        costId: Int
    ): List<CostParticipant>

    @Query(
        """
        SELECT *
        FROM cost_participants
        WHERE userId = :userId
        """
    )
    suspend fun getParticipantsForUser(
        userId: Int
    ): List<CostParticipant>

    @Query(
        """
        DELETE FROM cost_participants
        WHERE costId = :costId
        """
    )
    suspend fun deleteParticipantsForCost(
        costId: Int
    )


    // ============================================================
    // SPLIT ERSETZEN
    // ============================================================

    @Transaction
    suspend fun replaceSplitData(
        costId: Int,
        payers: List<CostPayer>,
        participants: List<CostParticipant>
    ) {

        deletePayersForCost(
            costId
        )

        deleteParticipantsForCost(
            costId
        )

        if (payers.isNotEmpty()) {

            insertPayers(
                payers
            )
        }

        if (participants.isNotEmpty()) {

            insertParticipants(
                participants
            )
        }
    }


    // ============================================================
    // EINZELNE KOSTE KOMPLETT LÖSCHEN
    // ============================================================

    @Transaction
    suspend fun deleteCostCompletely(
        cost: Costs
    ) {

        deletePayersForCost(
            cost.id
        )

        deleteParticipantsForCost(
            cost.id
        )

        deleteCosts(
            cost
        )
    }


    // ============================================================
    // ALLE KOSTEN EINER KATEGORIE LÖSCHEN
    // ============================================================

    @Transaction
    suspend fun deleteCostsForCategory(
        categoryId: Int
    ) {

        val costs =
            getCostsByCategory(
                categoryId
            )

        costs.forEach { cost ->

            deletePayersForCost(
                cost.id
            )

            deleteParticipantsForCost(
                cost.id
            )

            deleteCosts(
                cost
            )
        }
    }
}
