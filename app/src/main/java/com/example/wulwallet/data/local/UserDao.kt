// ================================================================
// FILE: data/local/UserDao.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // ============================================================
    // USER
    // ============================================================

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertUser(
        user: User
    ): Long

    @Update
    suspend fun updateUser(
        user: User
    )

    @Delete
    suspend fun deleteUser(
        user: User
    )


    // ============================================================
    // USER LADEN
    // ============================================================

    @Query(
        """
        SELECT *
        FROM users
        ORDER BY isMainUser DESC, id ASC
        """
    )
    fun getAllUsers(): Flow<List<User>>

    @Query(
        """
        SELECT *
        FROM users
        WHERE isMainUser = 1
        LIMIT 1
        """
    )
    fun getMainUser(): Flow<User?>

    @Query(
        """
        SELECT *
        FROM users
        ORDER BY id DESC
        LIMIT 1
        """
    )
    fun getUser(): Flow<User?>

    @Query(
        """
        SELECT COUNT(*)
        FROM users
        """
    )
    suspend fun getUserCount(): Int


    // ============================================================
    // TOTAL COSTS
    // ============================================================

    @Query(
        """
        UPDATE users
        SET totalCosts = totalCosts + :delta
        WHERE id = :userId
        """
    )
    suspend fun addToTotalCosts(
        userId: Int,
        delta: Float
    )

    @Query(
        """
        UPDATE users
        SET totalCosts = MAX(0, totalCosts - :amount)
        WHERE id = :userId
        """
    )
    suspend fun subtractFromTotalCosts(
        userId: Int,
        amount: Float
    )

    @Query(
        """
        UPDATE users
        SET totalCosts = :total
        WHERE id = :userId
        """
    )
    suspend fun setTotalCosts(
        userId: Int,
        total: Float
    )
}