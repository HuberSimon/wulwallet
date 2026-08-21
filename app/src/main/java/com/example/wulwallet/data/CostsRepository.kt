package com.example.wulwallet.data


import com.example.wulwallet.data.local.Costs
import com.example.wulwallet.data.local.CostsDao
import kotlinx.coroutines.flow.Flow


class CostsRepository(private val costsDao: CostsDao) {

    suspend fun insertCosts(costs: Costs) {
        costsDao.insertCosts(costs)
    }

    suspend fun updateCosts(costs: Costs) {
        costsDao.updateCosts(costs)
    }

    suspend fun deleteCosts(costs: Costs) {
        costsDao.deleteCosts(costs)
    }

    suspend fun getAllCostsByCategoryId(categoryId: Int): List<Costs> {
        return costsDao.getCostsByCategory(categoryId)
    }
}