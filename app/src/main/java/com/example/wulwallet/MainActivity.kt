package com.example.wulwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.wulwallet.ui.CostsViewModel
import com.example.wulwallet.ui.CategoryViewModel
import com.example.wulwallet.data.CostsRepository
import com.example.wulwallet.data.CategoryRepository
import com.example.wulwallet.data.TodoRepository
import com.example.wulwallet.data.UserRepository
import com.example.wulwallet.data.local.AppDatabase
import com.example.wulwallet.ui.App
import com.example.wulwallet.ui.TodoViewModel

import com.example.wulwallet.ui.UserViewModel
import com.example.wulwallet.ui.theme.WulwalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(appDatabase.userDao())
        val categoryRepository = CategoryRepository(appDatabase.categoryDao())
        val costsRepository = CostsRepository(appDatabase.costsDao())
        val todoRepository = TodoRepository(appDatabase.todoDao())
        val userViewModel = UserViewModel(userRepository)
        val categoryViewModel = CategoryViewModel(categoryRepository)
        val costsViewModel = CostsViewModel(costsRepository)
        val todoViewModel = TodoViewModel(todoRepository)

        setContent {
            WulwalletTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App(userViewModel, categoryViewModel, costsViewModel, todoViewModel)
                }
            }
        }
    }
}
