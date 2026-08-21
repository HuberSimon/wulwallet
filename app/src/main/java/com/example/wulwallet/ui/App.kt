package com.example.wulwallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wulwallet.data.local.User

@Composable
fun App(userViewModel: UserViewModel, categoryViewModel: CategoryViewModel, costsViewModel: CostsViewModel, todoViewModel: TodoViewModel) {
    val isInitialized by userViewModel.isInitialized.collectAsState()

    if (!isInitialized) {
        LoadingScreen()
    } else {
        val user = userViewModel.user.collectAsState(initial = null).value

        if (user == null || user.name.isEmpty()) {
            userAdd(userViewModel)
        } else {
                NavGraph(userViewModel, categoryViewModel, costsViewModel, todoViewModel)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Daten werden geladen...")
        }
    }
}


@Composable
fun userAdd(userViewModel: UserViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val user by userViewModel.user.collectAsState(initial = null)
    val userName = user?.name

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (userName == "") {
            showDialog = true
        }

        if (showDialog) {
            NameInputDialog(
                onDismiss = { showDialog = false },
                onConfirm = { enteredName ->
                    userViewModel.updateUser(User(id = user!!.id, name = enteredName))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun NameInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gib deinen Namen ein") },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(name) }) {
                Text(text = "OK", color = Color.White)
            }
        }
    )
}
