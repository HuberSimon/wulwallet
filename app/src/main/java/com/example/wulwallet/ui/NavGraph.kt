package com.example.wulidee.ui

import CustomTopAppBarWithTabs
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wulidee.ui.screens.IdeaScreen
import com.example.wulidee.ui.screens.UserScreen
import com.example.wulidee.ui.screens.PersonScreen
import com.example.wulidee.ui.screens.ReminderScreen

@Composable
fun NavGraph(userViewModel: UserViewModel, personViewModel: PersonViewModel, ideaViewModel: IdeaViewModel, reminderViewModel: ReminderViewModel) {
    val navController = rememberNavController()
    val user by userViewModel.user.collectAsState(initial = null)
    val userName = user?.name ?: ""

    var pagerState = rememberPagerState(pageCount = { 2 })
    if (user?.reminderEnabled == true){
        pagerState = rememberPagerState(pageCount = { 3 })
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            user?.let {
                CustomTopAppBarWithTabs(
                    user = it,
                    pagerState = pagerState,
                    coroutineScope = coroutineScope,
                    navController = navController
                )
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "horizontal_pager",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable("horizontal_pager") {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) { page ->
                        if (user?.reminderEnabled == true) {
                            when (page) {
                                0 -> PersonScreen(personViewModel, navController)
                                1 -> ReminderScreen(personViewModel, reminderViewModel)
                                2 -> UserScreen(userViewModel)
                            }
                        }else {
                            when (page) {
                                0 -> PersonScreen(personViewModel, navController)
                                1 -> UserScreen(userViewModel)
                            }
                        }
                    }
                }
                composable("idea_screen/{personId}") {backStackEntry ->
                    val personId = backStackEntry.arguments?.getString("personId")?.toIntOrNull()
                    if (personId != null) {
                        IdeaScreen(personViewModel, ideaViewModel, personId)
                    } else {
                        Log.e("NavGraph", "Invalid or missing personId")
                    }

                }
            }
        }
    )
}


