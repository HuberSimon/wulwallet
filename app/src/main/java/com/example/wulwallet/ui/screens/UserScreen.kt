package com.example.wulidee.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.wulidee.data.local.User
import com.example.wulidee.ui.UserViewModel

@Composable
fun UserScreen(userViewModel: UserViewModel) {
    val user by userViewModel.user.collectAsState(initial = null)
    var showDialog by remember { mutableStateOf(false) }
    var pinIsChecked by remember { mutableStateOf(false) }
    pinIsChecked = user?.pinLock == true
    var reminderEnabledIsChecked by remember { mutableStateOf(false) }
    reminderEnabledIsChecked = user?.reminderEnabled == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "${user?.name}'s Einstellungen",
            modifier = Modifier
                .padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        Divider(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "User ändern",
            modifier = Modifier
                .clickable {
                    if (user != null && user!!.name.isNotEmpty()) {
                        userViewModel.updateUser(
                            User(
                                id = user!!.id,
                                name = "",
                                pinEncrypted = 0,
                                pinLock = false,
                                reminderEnabled = false
                            )
                        )
                    }
                }
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Pin Sperre",
                modifier = Modifier
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            Column(
                modifier = Modifier.padding(end =32.dp)
            ) {
                Switch(
                    checked = pinIsChecked,
                    onCheckedChange = {
                        pinIsChecked = it
                        if (it &&  user!!.pinEncrypted == 0) showDialog = true
                        userViewModel.updateUser(
                            User(
                                id = user!!.id,
                                name = user!!.name,
                                pinEncrypted = if (it) user!!.pinEncrypted else 0,
                                pinLock = it,
                                reminderEnabled = user!!.reminderEnabled
                            )
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = Color.DarkGray,
                    )
                )

                Text(
                    text = "Pin ändern",
                    modifier = Modifier
                        .clickable {
                            showDialog = true
                        }
                        .padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Erinnerung an \nwichtige Termine \nfreischalten",
                modifier = Modifier
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            Column(
                modifier = Modifier.padding(end =32.dp)
            ) {
                Switch(
                    checked = reminderEnabledIsChecked,
                    onCheckedChange = {
                        reminderEnabledIsChecked = it
                        userViewModel.updateUser(
                            User(
                                id = user!!.id,
                                name = user!!.name,
                                pinEncrypted = user!!.pinEncrypted,
                                pinLock = user!!.pinLock,
                                reminderEnabled = reminderEnabledIsChecked
                            )
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = Color.DarkGray,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showDialog && user != null) {
        PinInputDialog(
            onDismiss = { showDialog = false },
            onConfirm = { enteredPin ->
                userViewModel.updateUser(
                    User(
                        id = user!!.id,
                        name = user!!.name,
                        pinEncrypted = enteredPin.hashCode(),
                        pinLock = true,
                        reminderEnabled = user!!.reminderEnabled
                    )
                )
                pinIsChecked = true
                showDialog = false
            }
        )
    }
}

@Composable
fun PinInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gib einen 4-stelligen PIN ein") },
        text = {
            TextField(
                value = pin,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        pin = input.take(4)
                    }
                },
                label = { Text("Pin") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(pin) },
                enabled = pin.length == 4
            ) {
                val textColor = if (pin.length == 4) Color.White else Color.DarkGray
                Text(text = "OK", color = textColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}

