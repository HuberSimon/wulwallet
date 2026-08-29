// ================================================================
// FILE: data/TodoRepository.kt
// ================================================================

package com.example.wulwallet.data

import com.example.wulwallet.data.local.Todo
import com.example.wulwallet.data.local.TodoDao

class TodoRepository(
    private val dao: TodoDao
) {

    val allTodos =
        dao.getTodos()

    suspend fun insertTodo(
        todo: Todo
    ) {
        dao.insertTodo(todo)
    }

    suspend fun updateTodo(
        todo: Todo
    ) {
        dao.updateTodo(todo)
    }

    suspend fun deleteTodo(
        todo: Todo
    ) {
        dao.deleteTodo(todo)
    }
}