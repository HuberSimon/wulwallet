package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.TodoRepository
import com.example.wulwallet.data.local.Todo
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    val allTodos = todoRepository.allTodos

    fun addTodo(description: String) {
        viewModelScope.launch {
            todoRepository.insertTodo(
                Todo(
                    description = description,
                    completed = false
                )
            )
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}