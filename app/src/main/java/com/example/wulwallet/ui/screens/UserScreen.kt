// ================================================================
// FILE: ui/screens/UserScreen.kt
// ================================================================

package com.example.wulwallet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wulwallet.data.local.User
import com.example.wulwallet.ui.CostsViewModel
import com.example.wulwallet.ui.UserViewModel
import java.util.Locale

private val Green = Color(0xFF43A047)
private val GreenDark = Color(0xFF2E7D32)

@Composable
fun UserScreen(
    userViewModel: UserViewModel,
    costsViewModel: CostsViewModel
) {

    // ============================================================
    // USERS
    // ============================================================

    val users by
    userViewModel
        .allUsers
        .collectAsState()


    // ============================================================
    // TATSÄCHLICHE REISEKOSTEN PRO USER
    // ============================================================

    val userTravelCosts by
    costsViewModel
        .userTravelCosts
        .collectAsState()


    // ============================================================
    // ADD DIALOG
    // ============================================================

    var showDialog by remember {
        mutableStateOf(false)
    }


    // ============================================================
    // DELETE DIALOG
    // ============================================================

    var userToDelete by remember {
        mutableStateOf<User?>(null)
    }


    // ============================================================
    // KOSTEN LADEN
    // ============================================================

    LaunchedEffect(Unit) {

        costsViewModel.loadAllCosts()
    }


    // ============================================================
    // GESAMTKOSTEN DER REISE
    // ============================================================

    val total =
        userTravelCosts
            .values
            .sum()


    // ============================================================
    // SCREEN
    // ============================================================

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {

        // ========================================================
        // HEADER
        // ========================================================

        item {

            Spacer(
                Modifier.height(18.dp)
            )


            Text(
                text =
                    "Reisegruppe",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                Modifier.height(22.dp)
            )
        }


        // ========================================================
        // USER LISTE
        // ========================================================

        items(
            items =
                users,

            key = {
                it.id
            }
        ) { user ->

            UserCard(
                user =
                    user,

                totalTravelCost =
                    userTravelCosts[user.id]
                        ?: 0f,

                onDelete = {

                    userToDelete =
                        user
                }
            )
        }


        // ========================================================
        // ADD BUTTON
        // ========================================================

        item {

            Spacer(
                Modifier.height(20.dp)
            )


            Button(
                onClick = {
                    showDialog = true
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                GreenDark
                        )
            ) {

                Icon(
                    Icons.Filled.Add,

                    contentDescription =
                        null
                )


                Spacer(
                    Modifier.width(7.dp)
                )


                Text(
                    "Reisebuddy hinzufügen"
                )
            }


            Spacer(
                Modifier.height(80.dp)
            )
        }
    }


    // ============================================================
    // ADD TRAVELER DIALOG
    // ============================================================

    if (showDialog) {

        AddTravelerDialog(

            onDismiss = {
                showDialog = false
            },

            onAdd = { name ->

                userViewModel.addUser(
                    name
                )

                showDialog = false
            }
        )
    }


    // ============================================================
    // DELETE USER ALERT DIALOG
    // ============================================================

    if (userToDelete != null) {

        val user =
            userToDelete!!


        AlertDialog(
            onDismissRequest = {

                userToDelete =
                    null
            },


            title = {

                Text(
                    text =
                        "Reisebuddy löschen?"
                )
            },


            text = {

                Text(
                    text =
                        "Möchtest du „${user.name}“ wirklich löschen?"
                )
            },


            confirmButton = {

                TextButton(
                    onClick = {

                        // =========================================
                        // BESTEHENDE LÖSCH-LOGIK
                        // =========================================

                        userViewModel
                            .deleteUser(
                                user
                            )


                        userToDelete =
                            null
                    }
                ) {

                    Text(
                        text =
                            "Löschen",

                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            },


            dismissButton = {

                TextButton(
                    onClick = {

                        userToDelete =
                            null
                    }
                ) {

                    Text(
                        text =
                            "Abbrechen"
                    )
                }
            }
        )
    }
}


// =================================================================
// USER CARD
// =================================================================

@Composable
private fun UserCard(
    user: User,
    totalTravelCost: Float,
    onDelete: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                20.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // ====================================================
            // USER ICON
            // ====================================================

            Surface(
                modifier =
                    Modifier.size(
                        50.dp
                    ),

                shape =
                    CircleShape,

                color =
                    if (user.isMainUser)
                        Color(0xFFE8F5E9)
                    else
                        Color(0xFFE3F2FD)
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Person,

                        contentDescription =
                            null,

                        tint =
                            if (user.isMainUser)
                                Green
                            else
                                Color(
                                    0xFF1976D2
                                )
                    )
                }
            }


            Spacer(
                Modifier.width(14.dp)
            )


            // ====================================================
            // USER INFORMATION
            // ====================================================

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        user.name.ifBlank {
                            "Noch kein Name"
                        },

                    fontWeight =
                        FontWeight.Bold
                )


                // =================================================
                // TATSÄCHLICHER REISEKOSTENANTEIL
                // =================================================

                Text(
                    text =
                        "${formatEuro(totalTravelCost)} € Gesamtausgaben",

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }


            // ====================================================
            // DELETE BUTTON
            // ====================================================

            if (!user.isMainUser) {

                IconButton(
                    onClick =
                        onDelete
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Delete,

                        contentDescription =
                            "Reisebuddy löschen",

                        tint =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            }
        }
    }
}


// =================================================================
// ADD TRAVELER
// =================================================================

@Composable
private fun AddTravelerDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }


    Dialog(
        onDismissRequest =
            onDismiss,

        properties =
            DialogProperties(
                dismissOnClickOutside =
                    true
            )
    ) {

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

            shape =
                RoundedCornerShape(
                    26.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        24.dp
                    )
            ) {

                Text(
                    text =
                        "Reisebuddy hinzufügen",

                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    Modifier.height(18.dp)
                )


                OutlinedTextField(
                    value =
                        name,

                    onValueChange = {
                        name = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "Name"
                        )
                    },

                    singleLine =
                        true
                )


                Spacer(
                    Modifier.height(22.dp)
                )


                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.End
                ) {

                    TextButton(
                        onClick =
                            onDismiss
                    ) {

                        Text(
                            "Abbrechen"
                        )
                    }


                    Spacer(
                        Modifier.width(8.dp)
                    )


                    Button(
                        onClick = {

                            onAdd(
                                name.trim()
                            )
                        },

                        enabled =
                            name.isNotBlank(),

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        GreenDark
                                )
                    ) {

                        Text(
                            "Hinzufügen"
                        )
                    }
                }
            }
        }
    }
}


// =================================================================
// FORMAT
// =================================================================

private fun formatEuro(
    value: Float
): String {

    return String.format(
        Locale.GERMANY,
        "%.2f",
        value
    )
}
