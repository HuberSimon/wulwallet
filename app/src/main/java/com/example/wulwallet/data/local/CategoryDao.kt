package com.example.wulwallet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(
        category: Category
    ): Long

    @Update
    suspend fun updateCategory(
        category: Category
    )

    @Delete
    suspend fun deleteCategory(
        category: Category
    )

    @Query(
        """
        SELECT *
        FROM category
        ORDER BY id DESC
        """
    )
    fun getAllCategories(): Flow<List<Category>>

    @Query(
        """
        SELECT *
        FROM category
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getCategoryById(
        id: Int
    ): Category?

    @Query(
        """
        DELETE FROM category
        WHERE id = :categoryId
        """
    )
    suspend fun deleteCategoryById(
        categoryId: Int
    )
}