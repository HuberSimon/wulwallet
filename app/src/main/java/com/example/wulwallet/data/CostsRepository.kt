// ================================================================
// FILE: data/CostsRepository.kt
// ================================================================

package com.example.wulwallet.data

import com.example.wulwallet.data.local.CostPayer
import com.example.wulwallet.data.local.CostParticipant
import com.example.wulwallet.data.local.Costs
import com.example.wulwallet.data.local.CostsDao

class CostsRepository(
    private val dao: CostsDao
) {

    // ============================================================
    // COSTS
    // ============================================================

    suspend fun insertCosts(
        costs: Costs
    ): Long {

        return dao.insertCosts(
            costs
        )
    }


    suspend fun updateCosts(
        costs: Costs
    ) {

        dao.updateCosts(
            costs
        )
    }


    suspend fun deleteCosts(
        costs: Costs
    ) {

        dao.deleteCostCompletely(
            costs
        )
    }


    suspend fun getAllCostsByCategoryId(
        categoryId: Int
    ): List<Costs> {

        return dao.getCostsByCategory(
            categoryId
        )
    }


    suspend fun getAllCosts(): List<Costs> {

        return dao.getAllCosts()
    }


    // ============================================================
    // PAYERS
    // ============================================================

    suspend fun getPayers(
        costId: Int
    ): List<CostPayer> {

        return dao.getPayers(
            costId
        )
    }


    // ============================================================
    // PARTICIPANTS
    // ============================================================

    suspend fun getParticipants(
        costId: Int
    ): List<CostParticipant> {

        return dao.getParticipants(
            costId
        )
    }


    // ============================================================
    // SPLIT SPEICHERN
    // ============================================================

    suspend fun saveSplit(
        costId: Int,
        payers: List<CostPayer>,
        participants: List<CostParticipant>
    ) {

        dao.replaceSplitData(
            costId = costId,
            payers = payers,
            participants = participants
        )
    }


    // ============================================================
    // ALLE KOSTEN EINER KATEGORIE LÖSCHEN
    // ============================================================

    suspend fun deleteCostsForCategory(
        categoryId: Int
    ) {

        dao.deleteCostsForCategory(
            categoryId
        )
    }
}
