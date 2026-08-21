package com.example.wulidee.data

import com.example.wulidee.data.local.UserDao
import com.example.wulidee.data.local.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(private val userDao: UserDao) {

    val user: Flow<User> = userDao.getUser()

    suspend fun initializeUser() {
        val user = userDao.getUser().firstOrNull()
        if (user == null) {
            val newUser = User(name = "", pinEncrypted = 0, pinLock = false, reminderEnabled = false)
            userDao.insertUser(newUser)
        }
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}
