package com.example.wulwallet.ui

import CustomTopAppBarWithTabs
import android.util.Log
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
import com.example.wulwallet.ui.screens.CostsScreen
import com.example.wulwallet.ui.screens.UserScreen
import com.example.wulwallet.ui.screens.CategoryScreen
import com.example.wulwallet.ui.screens.PlanningScreen
import com.example.wulwallet.ui.screens.TodoScreen

@Composable
fun NavGraph(userViewModel: UserViewModel, categoryViewModel: CategoryViewModel, costsViewModel: CostsViewModel, todoViewModel: TodoViewModel) {
    val navController = rememberNavController()
    val user by userViewModel.user.collectAsState(initial = null)
    val userName = user?.name ?: ""

    var pagerState = rememberPagerState(pageCount = { 4 })
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
                            when (page) {
                                0 -> CategoryScreen(categoryViewModel, navController)
                                1 -> PlanningScreen(categoryViewModel)
                                2 -> TodoScreen(todoViewModel)
                                3 -> UserScreen(userViewModel)
                            }
                    }
                }
                composable("costs_screen/{categoryId}") {backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
                    if (categoryId != null) {
                        CostsScreen(categoryViewModel, costsViewModel, categoryId)
                    } else {
                        Log.e("NavGraph", "Invalid or missing categoryId")
                    }

                }
            }
        }
    )
}


