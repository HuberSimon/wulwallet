package com.example.wulidee

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.wulidee.ui.IdeaViewModel
import com.example.wulidee.ui.PersonViewModel
import com.example.wulidee.data.IdeaRepository
import com.example.wulidee.data.PersonRepository
import com.example.wulidee.data.ReminderRepository
import com.example.wulidee.data.UserRepository
import com.example.wulidee.data.local.AppDatabase
import com.example.wulidee.ui.App
import com.example.wulidee.ui.ReminderViewModel
import com.example.wulidee.ui.UserViewModel
import com.example.wulidee.ui.theme.WulideeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(appDatabase.userDao())
        val personRepository = PersonRepository(appDatabase.personDao())
        val ideaRepository = IdeaRepository(appDatabase.ideaDao())
        val reminderRepository = ReminderRepository(appDatabase.reminderDao())
        val userViewModel = UserViewModel(userRepository)
        val personViewModel = PersonViewModel(personRepository)
        val ideaViewModel = IdeaViewModel(ideaRepository)
        val reminderViewModel = ReminderViewModel(reminderRepository)

        setContent {
            WulideeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App(userViewModel, personViewModel, ideaViewModel, reminderViewModel)
                }
            }
        }
    }
}
