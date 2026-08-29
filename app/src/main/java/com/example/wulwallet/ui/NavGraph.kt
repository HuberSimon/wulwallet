// ================================================================
// FILE: ui/NavGraph.kt
// ================================================================

package com.example.wulwallet.ui

import CustomTopAppBarWithTabs
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wulwallet.ui.screens.CategoryScreen
import com.example.wulwallet.ui.screens.CostsScreen
import com.example.wulwallet.ui.screens.SplitScreen
import com.example.wulwallet.ui.screens.TodoScreen
import com.example.wulwallet.ui.screens.UserScreen
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

// ================================================================
// NAV GRAPH
// ================================================================

@Composable
fun NavGraph(
    userViewModel: UserViewModel,
    categoryViewModel: CategoryViewModel,
    costsViewModel: CostsViewModel,
    todoViewModel: TodoViewModel
) {

    // ------------------------------------------------------------
    // NAV CONTROLLER
    // ------------------------------------------------------------

    val navController = rememberNavController()

    // ------------------------------------------------------------
    // CURRENT MAIN USER
    // ------------------------------------------------------------

    val user by userViewModel.user.collectAsState()

    // ------------------------------------------------------------
    // PAGER
    //
    // 0 = Kategorien
    // 1 = Budget
    // 2 = ToDo
    // 3 = Split
    // 4 = Benutzer
    // ------------------------------------------------------------

    val pagerState = rememberPagerState(
        initialPage = 0
    )

    val coroutineScope = rememberCoroutineScope()

    // ------------------------------------------------------------
    // SCAFFOLD
    // ------------------------------------------------------------

    Scaffold(

        topBar = {

            user?.let { currentUser ->

                CustomTopAppBarWithTabs(
                    user = currentUser,
                    pagerState = pagerState,
                    coroutineScope = coroutineScope,
                    navController = navController
                )
            }
        }

    ) { innerPadding ->

        // --------------------------------------------------------
        // NAVIGATION HOST
        // --------------------------------------------------------

        NavHost(
            navController = navController,
            startDestination = "horizontal_pager",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ====================================================
            // MAIN PAGER
            // ====================================================

            composable(
                route = "horizontal_pager"
            ) {

                HorizontalPager(
                    count = 4,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->

                    when (page) {
                        0 -> {

                            CategoryScreen(
                                categoryViewModel = categoryViewModel,
                                navController = navController,
                                costsViewModel = costsViewModel,
                                userViewModel = userViewModel
                            )
                        }

                        1 -> {

                            SplitScreen(
                                userViewModel = userViewModel,
                                costsViewModel = costsViewModel
                            )
                        }

                        2 -> {

                            UserScreen(
                                userViewModel = userViewModel,
                                costsViewModel = costsViewModel
                            )
                        }

                        3 -> {

                            TodoScreen(
                                todoViewModel
                            )
                        }
                    }
                }
            }

            // ====================================================
            // COSTS SCREEN
            // ====================================================

            composable(
                route = "costs_screen/{categoryId}"
            ) { entry ->

                val categoryId = entry.arguments
                    ?.getString("categoryId")
                    ?.toIntOrNull()

                if (categoryId != null) {

                    CostsScreen(
                        categoryViewModel = categoryViewModel,
                        costsViewModel = costsViewModel,
                        selectedCategoryId = categoryId,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}