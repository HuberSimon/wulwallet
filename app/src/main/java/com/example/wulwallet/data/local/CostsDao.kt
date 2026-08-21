package com.example.wulwallet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCosts(costs: Costs)

    @Update
    suspend fun updateCosts(costs: Costs)

    @Delete
    suspend fun deleteCosts(costs: Costs)

    @Query("SELECT * FROM costs WHERE categoryId = :categoryId ORDER BY id DESC")
    suspend fun getCostsByCategory(categoryId: Int): List<Costs>
}