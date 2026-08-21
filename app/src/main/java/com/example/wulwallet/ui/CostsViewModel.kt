package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.CostsRepository
import com.example.wulwallet.data.local.Costs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CostsViewModel(private val repository: CostsRepository) : ViewModel() {

    private val _categoryCosts = MutableStateFlow<List<Costs>>(emptyList())
    val categoryCosts: StateFlow<List<Costs>> = _categoryCosts

    fun allCostsByCategory(categoryId: Int) {
        viewModelScope.launch {
            val costs = repository.getAllCostsByCategoryId(categoryId)
            _categoryCosts.value = costs
        }
    }

    fun addCosts(costs: Costs) {
        viewModelScope.launch {
            repository.insertCosts(costs)
        }
    }

    fun updateCosts(costs: Costs) {
        viewModelScope.launch {
            repository.updateCosts(costs)
        }
    }

    fun deleteCosts(costs: Costs) {
        viewModelScope.launch {
            repository.deleteCosts(costs)
        }
    }
}