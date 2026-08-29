// ================================================================
// FILE: ui/UserViewModel.kt
// ================================================================

package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.UserRepository
import com.example.wulwallet.data.local.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _isInitialized =
        MutableStateFlow(false)

    val isInitialized: StateFlow<Boolean> =
        _isInitialized

    val user: StateFlow<User?> =
        repository.user.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val allUsers: StateFlow<List<User>> =
        repository.allUsers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {

        viewModelScope.launch(
            Dispatchers.IO
        ) {

            repository.initializeUser()

            _isInitialized.value = true
        }
    }

    fun addUser(
        name: String
    ) {

        if (name.isBlank()) return

        viewModelScope.launch {

            repository.insertUser(
                User(
                    name = name.trim(),
                    isMainUser = false
                )
            )
        }
    }

    fun updateUser(
        user: User
    ) {

        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    fun deleteUser(
        user: User
    ) {

        if (user.isMainUser) return

        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun resetMainUser(
        user: User
    ) {

        viewModelScope.launch {

            repository.updateUser(
                user.copy(
                    name = ""
                )
            )
        }
    }
}