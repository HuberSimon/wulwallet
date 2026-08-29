// ================================================================
// FILE: data/local/TodoDao.kt
// ================================================================

package com.example.wulwallet.data.local

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query(
        "SELECT * FROM todos ORDER BY id DESC"
    )
    fun getTodos(): Flow<List<Todo>>
}