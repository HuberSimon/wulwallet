// ================================================================
// FILE: data/UserRepository.kt
// ================================================================

package com.example.wulwallet.data

import com.example.wulwallet.data.local.User
import com.example.wulwallet.data.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val dao: UserDao
) {

    val user: Flow<User?> =
        dao.getMainUser()

    val allUsers: Flow<List<User>> =
        dao.getAllUsers()

    suspend fun insertUser(
        user: User
    ) {
        dao.insertUser(user)
    }

    suspend fun updateUser(
        user: User
    ) {
        dao.updateUser(user)
    }

    suspend fun deleteUser(
        user: User
    ) {
        dao.deleteUser(user)
    }

    suspend fun initializeUser() {

        if (dao.getUserCount() == 0) {

            dao.insertUser(
                User(
                    name = "",
                    isMainUser = true
                )
            )
        }
    }
}