// ================================================================
// FILE: ui/screens/CategoryScreen.kt
// ================================================================

package com.example.wulwallet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.wulwallet.data.local.Category
import com.example.wulwallet.ui.CategoryViewModel
import com.example.wulwallet.ui.CostsViewModel
import com.example.wulwallet.ui.UserViewModel
import java.util.Locale

private val Green = Color(0xFF43A047)
private val GreenDark = Color(0xFF2E7D32)
private val Blue = Color(0xFF1976D2)
private val ExpenseRed = Color(0xFFFF6B6B)

@Composable
fun CategoryScreen(
    categoryViewModel: CategoryViewModel,
    navController: NavController,
    costsViewModel: CostsViewModel,
    userViewModel: UserViewModel
) {

    // ============================================================
    // DATA
    // ============================================================

    val categories by
    categoryViewModel
        .categories
        .collectAsState(initial = emptyList())

    val users by
    userViewModel
        .allUsers
        .collectAsState()

    val splitData by
    costsViewModel
        .splitData
        .collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var categoryToDelete by remember {
        mutableStateOf<Category?>(null)
    }


    // ============================================================
    // LOAD COSTS
    // ============================================================

    LaunchedEffect(Unit) {
        costsViewModel.loadAllCosts()
    }


    // ============================================================
    // MAIN USER
    // ============================================================

    val mainUser =
        users.firstOrNull {
            it.isMainUser
        }


    // ============================================================
    // MEIN GESAMTANTEIL
    // ============================================================

    val mainUserTotal =
        if (mainUser != null) {

            splitData.sumOf { split ->

                split.participants
                    .filter {
                        it.userId == mainUser.id
                    }
                    .sumOf {
                        it.share.toDouble()
                    }
            }

        } else {
            0.0
        }


    // ============================================================
    // SCREEN
    // ============================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            item {

                Spacer(
                    Modifier.height(18.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "WulWallet",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineMedium,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    Surface(
                        modifier =
                            Modifier.size(50.dp),

                        shape =
                            CircleShape,

                        color =
                            Color(0xFFE8F5E9)
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.TravelExplore,

                                contentDescription =
                                    null,

                                tint =
                                    Green
                            )
                        }
                    }
                }

                Spacer(
                    Modifier.height(20.dp)
                )


                // =================================================
                // MEINE GESAMTKOSTEN
                // =================================================

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(22.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Green
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {

                        Text(
                            text =
                                "Meine Reisekosten",

                            color =
                                Color.White.copy(
                                    alpha = 0.8f
                                )
                        )

                        Spacer(
                            Modifier.height(5.dp)
                        )

                        Text(
                            text =
                                formatEuro(
                                    mainUserTotal.toFloat()
                                ) + " €",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineLarge,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                Color.White
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "${categories.size} Kategorien",

                            color =
                                Color.White.copy(
                                    alpha = 0.8f
                                )
                        )
                    }
                }

                Spacer(
                    Modifier.height(22.dp)
                )


                Text(
                    text =
                        "Reisekategorien",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            // ====================================================
            // KEINE KATEGORIEN
            // ====================================================

            if (categories.isEmpty()) {

                item {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(22.dp),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(30.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Payments,

                                contentDescription =
                                    null,

                                tint =
                                    Blue,

                                modifier =
                                    Modifier.size(42.dp)
                            )

                            Spacer(
                                Modifier.height(12.dp)
                            )

                            Text(
                                text =
                                    "Noch keine Kategorien",

                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                Modifier.height(16.dp)
                            )

                            Button(
                                onClick = {
                                    showDialog = true
                                },

                                colors =
                                    ButtonDefaults
                                        .buttonColors(
                                            containerColor =
                                                GreenDark
                                        )
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Add,

                                    contentDescription =
                                        null
                                )

                                Spacer(
                                    Modifier.width(6.dp)
                                )

                                Text(
                                    "Kategorie hinzufügen"
                                )
                            }
                        }
                    }
                }

            } else {

                // =================================================
                // KATEGORIEN
                // =================================================

                items(
                    items = categories,

                    key = {
                        it.id
                    }
                ) { category ->

                    val mainUserCategoryTotal =
                        if (mainUser != null) {

                            splitData
                                .filter {
                                    it.cost.categoryId ==
                                            category.id
                                }
                                .sumOf { split ->

                                    split.participants
                                        .filter {
                                            it.userId ==
                                                    mainUser.id
                                        }
                                        .sumOf {
                                            it.share.toDouble()
                                        }
                                }

                        } else {
                            0.0
                        }


                    CategoryCard(
                        category =
                            category,

                        mainPaid =
                            mainUserCategoryTotal.toFloat(),

                        onClick = {

                            navController.navigate(
                                "costs_screen/${category.id}"
                            )
                        },

                        onDelete = {

                            categoryToDelete =
                                category
                        }
                    )
                }
            }


            item {

                Spacer(
                    Modifier.height(90.dp)
                )
            }
        }


        // ========================================================
        // ADD BUTTON
        // ========================================================

        FloatingActionButton(
            onClick = {
                showDialog = true
            },

            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp),

            containerColor =
                Green,

            contentColor =
                Color.White,

            shape =
                RoundedCornerShape(18.dp)
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Add,

                contentDescription =
                    "Kategorie hinzufügen"
            )
        }
    }


    // ============================================================
    // ADD CATEGORY DIALOG
    // ============================================================

    if (showDialog) {

        AddCategoryDialog(

            onDismiss = {
                showDialog = false
            },

            onAdd = { name ->

                categoryViewModel.addCategory(
                    Category(
                        name = name,
                        totalSum = 0f
                    )
                )

                showDialog = false
            }
        )
    }


    // ============================================================
    // DELETE CATEGORY DIALOG
    // ============================================================

    categoryToDelete?.let { category ->

        AlertDialog(

            onDismissRequest = {
                categoryToDelete = null
            },

            title = {

                Text(
                    text = "Kategorie löschen?"
                )
            },

            text = {

                Text(
                    text =
                        "Möchtest du die Kategorie " +
                                "\"${category.name}\" wirklich löschen?\n\n" +
                                "Alle zugehörigen Kosten werden ebenfalls gelöscht."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        categoryViewModel
                            .deleteCategory(
                                category
                            )

                        categoryToDelete = null
                    }
                ) {

                    Text(
                        text = "Löschen",

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

                        categoryToDelete = null
                    }
                ) {

                    Text(
                        text = "Abbrechen"
                    )
                }
            }
        )
    }
}


// =================================================================
// CATEGORY CARD
// =================================================================

@Composable
private fun CategoryCard(
    category: Category,
    mainPaid: Float,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(20.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    modifier =
                        Modifier.size(48.dp),

                    shape =
                        RoundedCornerShape(14.dp),

                    color =
                        Color(0xFFE8F5E9)
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Payments,

                            contentDescription =
                                null,

                            tint =
                                Green
                        )
                    }
                }

                Spacer(
                    Modifier.width(14.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            category.name,

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Gesamte Rechnung: ${
                                formatEuro(
                                    category.totalSum
                                )
                            } €"
                    )
                }


                // =================================================
                // DELETE BUTTON
                // =================================================

                IconButton(
                    onClick = onDelete
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Delete,

                        contentDescription =
                            "Löschen",

                        tint =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            }


            Spacer(
                Modifier.height(14.dp)
            )


            // =====================================================
            // MAIN USER SHARE
            // =====================================================

            Surface(
                shape =
                    RoundedCornerShape(12.dp),

                color =
                    Color(0xFFE3F2FD)
            ) {

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "Mein Anteil",

                        fontWeight =
                            FontWeight.Medium
                    )

                    Text(
                        text =
                            formatEuro(
                                mainPaid
                            ) + " €",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            ExpenseRed
                    )
                }
            }
        }
    }
}


// =================================================================
// ADD CATEGORY DIALOG
// =================================================================

@Composable
private fun AddCategoryDialog(
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
                dismissOnClickOutside = true
            )
    ) {

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

            shape =
                RoundedCornerShape(26.dp)
        ) {

            Column(
                modifier =
                    Modifier.padding(24.dp)
            ) {

                Text(
                    text =
                        "Neue Kategorie",

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
                    value = name,

                    onValueChange = {
                        name = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Name")
                    },

                    singleLine = true
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

                            if (
                                name.isNotBlank()
                            ) {

                                onAdd(
                                    name.trim()
                                )
                            }
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
// HELPER
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
