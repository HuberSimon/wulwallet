package com.example.wulwallet.data

import com.example.wulwallet.data.local.Todo
import com.example.wulwallet.data.local.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepository (private val todoDao: TodoDao) {

    val allTodos: Flow<List<Todo>> = todoDao.getTodos()

    suspend fun insertTodo(todo: Todo) {
        todoDao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }
}