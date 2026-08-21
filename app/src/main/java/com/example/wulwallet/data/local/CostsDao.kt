package com.example.wulwallet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: Idea)

    @Update
    suspend fun updateIdea(idea: Idea)

    @Delete
    suspend fun deleteIdea(idea: Idea)

    @Query("SELECT * FROM ideas WHERE userId = :userId ORDER BY id DESC")
    suspend fun getIdeasByUser(userId: Int): List<Idea>

    @Query("SELECT * FROM ideas ORDER BY id DESC")
    fun getAllIdeas(): Flow<List<Idea>>
}