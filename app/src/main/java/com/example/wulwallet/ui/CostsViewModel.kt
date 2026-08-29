// ================================================================
// FILE: ui/CostsViewModel.kt
// ================================================================

package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.CostsRepository
import com.example.wulwallet.data.local.CostPayer
import com.example.wulwallet.data.local.CostParticipant
import com.example.wulwallet.data.local.Costs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CostSplitData(
    val cost: Costs,
    val payers: List<CostPayer>,
    val participants: List<CostParticipant>
)

class CostsViewModel(
    private val repository: CostsRepository
) : ViewModel() {

    private val _categoryCosts =
        MutableStateFlow<List<Costs>>(
            emptyList()
        )

    val categoryCosts: StateFlow<List<Costs>> =
        _categoryCosts


    private val _allCosts =
        MutableStateFlow<List<Costs>>(
            emptyList()
        )

    val allCosts: StateFlow<List<Costs>> =
        _allCosts


    private val _splitData =
        MutableStateFlow<List<CostSplitData>>(
            emptyList()
        )

    val splitData: StateFlow<List<CostSplitData>> =
        _splitData


    private val _userTravelCosts =
        MutableStateFlow<Map<Int, Float>>(
            emptyMap()
        )

    val userTravelCosts: StateFlow<Map<Int, Float>> =
        _userTravelCosts


    // ============================================================
    // KOSTEN EINER KATEGORIE LADEN
    // ============================================================

    fun allCostsByCategory(
        categoryId: Int
    ) {

        viewModelScope.launch {

            _categoryCosts.value =
                repository.getAllCostsByCategoryId(
                    categoryId
                )
        }
    }


    // ============================================================
    // ALLE KOSTEN + SPLITS LADEN
    // ============================================================

    fun loadAllCosts() {

        viewModelScope.launch {

            val costs =
                repository.getAllCosts()

            _allCosts.value =
                costs

            val result =
                costs.map { cost ->

                    CostSplitData(
                        cost = cost,

                        payers =
                            repository.getPayers(
                                cost.id
                            ),

                        participants =
                            repository.getParticipants(
                                cost.id
                            )
                    )
                }

            _splitData.value =
                result


            // ========================================================
            // TATSÄCHLICHE REISEKOSTEN PRO USER BERECHNEN
            // ========================================================

            val userCosts =
                mutableMapOf<Int, Float>()

            result.forEach { split ->

                split.participants.forEach { participant ->

                    userCosts[participant.userId] =
                        (
                                userCosts[participant.userId]
                                    ?: 0f
                                ) + participant.share
                }
            }

            _userTravelCosts.value =
                userCosts
        }
    }


    // ============================================================
    // KOSTE HINZUFÜGEN
    // ============================================================

    fun addCosts(
        costs: Costs,
        payers: List<CostPayer>,
        participants: List<CostParticipant>,
        onFinished: () -> Unit = {}
    ) {

        viewModelScope.launch {

            // ----------------------------------------------------
            // 1. COST ANLEGEN
            // ----------------------------------------------------

            val id =
                repository.insertCosts(
                    costs
                ).toInt()


            // ----------------------------------------------------
            // 2. SPLITS MIT ECHTER COST-ID SPEICHERN
            // ----------------------------------------------------

            val savedPayers =
                payers.map { payer ->

                    payer.copy(
                        costId = id
                    )
                }

            val savedParticipants =
                participants.map { participant ->

                    participant.copy(
                        costId = id
                    )
                }


            repository.saveSplit(
                costId = id,

                payers =
                    savedPayers,

                participants =
                    savedParticipants
            )

            loadAllCosts()

            allCostsByCategory(
                costs.categoryId
            )

            onFinished()
        }
    }


    // ============================================================
    // KOSTE BEARBEITEN
    // ============================================================

    fun updateCosts(
        costs: Costs,
        payers: List<CostPayer>,
        participants: List<CostParticipant>,
        onFinished: () -> Unit = {}
    ) {

        viewModelScope.launch {

            // ----------------------------------------------------
            // 1. ALTE SPLITS LADEN
            // ----------------------------------------------------

            val oldParticipants =
                repository.getParticipants(
                    costs.id
                )


            // ----------------------------------------------------
            // 2. COST AKTUALISIEREN
            // ----------------------------------------------------

            repository.updateCosts(
                costs
            )


            // ----------------------------------------------------
            // 3. NEUE SPLITS AUF DIE COST-ID SETZEN
            // ----------------------------------------------------

            val savedPayers =
                payers.map { payer ->

                    payer.copy(
                        costId = costs.id
                    )
                }

            val savedParticipants =
                participants.map { participant ->

                    participant.copy(
                        costId = costs.id
                    )
                }


            repository.saveSplit(
                costId = costs.id,

                payers =
                    savedPayers,

                participants =
                    savedParticipants
            )


            loadAllCosts()

            allCostsByCategory(
                costs.categoryId
            )

            onFinished()
        }
    }


    // ============================================================
    // KOSTE LÖSCHEN
    // ============================================================

    fun deleteCosts(
        costs: Costs
    ) {

        viewModelScope.launch {

            // ----------------------------------------------------
            // 1. ALTE PARTICIPANTS LADEN
            // ----------------------------------------------------

            val oldParticipants =
                repository.getParticipants(
                    costs.id
                )


            // ----------------------------------------------------
            // 2. COST + SPLITS LÖSCHEN
            // ----------------------------------------------------

            repository.deleteCosts(
                costs
            )


            // ----------------------------------------------------
            // 4. UI AKTUALISIEREN
            // ----------------------------------------------------

            loadAllCosts()

            allCostsByCategory(
                costs.categoryId
            )
        }
    }
}