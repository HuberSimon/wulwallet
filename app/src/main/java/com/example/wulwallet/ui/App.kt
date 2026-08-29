// ================================================================
// FILE: ui/App.kt
// ================================================================

package com.example.wulwallet.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.safeDrawingPadding
import com.example.wulwallet.data.local.User

@Composable
fun App(
    userViewModel: UserViewModel,
    categoryViewModel: CategoryViewModel,
    costsViewModel: CostsViewModel,
    todoViewModel: TodoViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {

        val initialized by
        userViewModel
            .isInitialized
            .collectAsState()

        if (!initialized) {

            LoadingScreen()

        } else {

            val user by
            userViewModel
                .user
                .collectAsState()

            if (
                user == null ||
                user!!.name.isBlank()
            ) {

                UserAddScreen(
                    userViewModel
                )

            } else {

                NavGraph(
                    userViewModel =
                        userViewModel,

                    categoryViewModel =
                        categoryViewModel,

                    costsViewModel =
                        costsViewModel,

                    todoViewModel =
                        todoViewModel
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {

    Box(
        Modifier.fillMaxSize(),
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()

            Spacer(
                Modifier.height(16.dp)
            )

            Text(
                "Daten werden geladen..."
            )
        }
    }
}

@Composable
private fun UserAddScreen(
    userViewModel: UserViewModel
) {

    var name by
    remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment =
            Alignment.Center
    ) {

        Card(
            Modifier.fillMaxWidth(),
            shape =
                androidx.compose.foundation.shape
                    .RoundedCornerShape(28.dp)
        ) {

            Column(
                Modifier.padding(24.dp)
            ) {

                Text(
                    "Willkommen bei WulWallet",
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall,
                    fontWeight =
                        androidx.compose.ui.text.font
                            .FontWeight.Bold
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Text(
                    "Wie heißt du?"
                )

                Spacer(
                    Modifier.height(18.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    Modifier.fillMaxWidth(),
                    label = {
                        Text("Dein Name")
                    },
                    singleLine = true
                )

                Spacer(
                    Modifier.height(18.dp)
                )

                Button(
                    onClick = {

                        if (
                            name.isNotBlank()
                        ) {

                            val current =
                                userViewModel
                                    .user
                                    .value

                            if (current != null) {

                                userViewModel
                                    .updateUser(
                                        User(
                                            id =
                                                current.id,
                                            name =
                                                name.trim(),
                                            isMainUser =
                                                true
                                        )
                                    )
                            }
                        }
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Los geht's"
                    )
                }
            }
        }
    }
}