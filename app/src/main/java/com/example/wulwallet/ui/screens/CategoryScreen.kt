package com.example.wulwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.wulwallet.data.local.Category
import com.example.wulwallet.ui.CategoryViewModel

private val TravelGreen = Color(0xFF43A047)
private val TravelGreenLight = Color(0xFFE8F5E9)
private val TravelGreenDark = Color(0xFF388E3C)

private val TravelBlue = Color(0xFF1976D2)
private val TravelBlueLight = Color(0xFFE3F2FD)

private val TravelOrange = Color(0xFFFF9800)
private val TravelOrangeLight = Color(0xFFFFF3E0)

private val ExpenseRed = Color(0xFFE85D5D)
private val ExpenseGreen = Color(0xFF43A047)

private val LightText = Color(0xFFF1F1F1)
private val SecondaryLightText = Color(0xFFDCDCDC)

@Composable
fun CategoryScreen(
    categoryViewModel: CategoryViewModel,
    navController: NavController
) {
    val allCategories by categoryViewModel.allCategories.collectAsState(
        initial = emptyList()
    )

    var showDialog by remember {
        mutableStateOf(false)
    }

    var categoryToDelete by remember {
        mutableStateOf<Category?>(null)
    }

    val totalTravelCosts =
        allCategories.sumOf { it.totalSum.toDouble() }.toFloat()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            // =========================================================
            // HEADER
            // =========================================================

            item {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Meine Reise",
                            style =
                                MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Verwalte deine Kategorien und Ausgaben.",
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = TravelGreenLight
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Luggage,
                                contentDescription = null,
                                tint = TravelGreen,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // =====================================================
                // GESAMTKOSTEN
                // =====================================================

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = TravelGreen
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = Color.White.copy(
                                alpha = 0.16f
                            )
                        ) {

                            Box(
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Luggage,
                                    contentDescription = null,
                                    tint = LightText,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.size(14.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Reisekosten",
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color = SecondaryLightText
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                            Text(
                                text = String.format(
                                    "%.2f €",
                                    totalTravelCosts
                                ),
                                style =
                                    MaterialTheme.typography
                                        .headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = LightText
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            // =========================================================
            // KEINE KATEGORIEN
            // =========================================================

            if (allCategories.isEmpty()) {

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),

                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme
                                    .surfaceVariant
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = CircleShape,
                                color = TravelOrangeLight
                            ) {

                                Box(
                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Filled.Folder,
                                        contentDescription = null,
                                        tint = TravelOrange,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            Text(
                                text = "Noch keine Kategorien",
                                style =
                                    MaterialTheme.typography
                                        .titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(5.dp)
                            )

                            Text(
                                text =
                                    "Erstelle deine erste Kategorie für deine Reise.",
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant
                            )

                            Spacer(
                                modifier = Modifier.height(18.dp)
                            )

                            Button(
                                onClick = {
                                    showDialog = true
                                },

                                shape =
                                    RoundedCornerShape(14.dp),
                                colors =
                                            ButtonDefaults.buttonColors(
                                            containerColor =
                                                TravelGreenDark
                                            )
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Add,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier.size(6.dp)
                                )

                                Text(
                                    text = "Kategorie hinzufügen"
                                )
                            }
                        }
                    }
                }

            } else {

                // =====================================================
                // KATEGORIEN HEADER
                // =====================================================

                item {

                    Text(
                        text = "Kategorien",
                        style =
                            MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,

                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 2.dp
                        )
                    )
                }

                // =====================================================
                // KATEGORIEN
                // =====================================================

                items(
                    items = allCategories,
                    key = { it.id }
                ) { category ->

                    CategoryItem(
                        category = category,

                        onDeleteCategory = {
                            categoryToDelete = category
                        },

                        onClick = {

                            navController.navigate(
                                "costs_screen/${category.id}"
                            )
                        }
                    )
                }

                item {

                    Spacer(
                        modifier = Modifier.height(90.dp)
                    )
                }
            }
        }

        // =============================================================
        // FLOATING ACTION BUTTON
        // =============================================================

        FloatingActionButton(
            onClick = {
                showDialog = true
            },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp),

            shape = RoundedCornerShape(18.dp),

            containerColor = TravelGreen,
            contentColor = Color.White
        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription =
                    "Kategorie hinzufügen"
            )
        }
    }

    // ================================================================
    // ADD CATEGORY DIALOG
    // ================================================================

    if (showDialog) {

        AddCategoryDialog(

            onDismiss = {
                showDialog = false
            },

            onAddCategory = { name ->

                categoryViewModel.addCategory(
                    Category(
                        name = name,
                        totalSum = 0.0f,
                        plannedSum = 0.0f
                    )
                )

                showDialog = false
            }
        )
    }

    // ================================================================
    // DELETE DIALOG
    // ================================================================

    if (categoryToDelete != null) {

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
                    "Möchtest du die Kategorie " +
                            "\"${categoryToDelete!!.name}\" " +
                            "wirklich löschen?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        categoryToDelete?.let { category ->
                            categoryViewModel.deleteCategory(
                                category
                            )
                        }

                        categoryToDelete = null
                    }
                ) {

                    Text(
                        text = "Löschen",
                        color =
                            MaterialTheme.colorScheme.error
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


// =====================================================================
// CATEGORY ITEM
// =====================================================================

@Composable
fun CategoryItem(
    category: Category,
    onDeleteCategory: () -> Unit,
    onClick: () -> Unit
) {

    val isOverBudget =
        category.plannedSum > 0f &&
                category.totalSum > category.plannedSum

    val expenseColor =
        if (isOverBudget) {
            ExpenseRed
        } else {
            ExpenseGreen
        }

    val expenseBackground =
        if (isOverBudget) {
            ExpenseRed.copy(alpha = 0.10f)
        } else {
            ExpenseGreen.copy(alpha = 0.10f)
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(20.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // ---------------------------------------------------------
            // CATEGORY ICON
            // ---------------------------------------------------------

            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(15.dp),
                color = TravelBlueLight
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Folder,
                        contentDescription = null,
                        tint = TravelBlue,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            // ---------------------------------------------------------
            // CATEGORY INFO
            // ---------------------------------------------------------

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = category.name,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Geplant: ${
                        String.format(
                            "%.2f",
                            category.plannedSum
                        )
                    } €",

                    style =
                        MaterialTheme.typography.bodySmall,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // -----------------------------------------------------
                // ACTUAL COST
                // -----------------------------------------------------

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(22.dp),
                        shape = CircleShape,
                        color = expenseBackground
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    if (isOverBudget) {
                                        Icons.Filled.TrendingUp
                                    } else {
                                        Icons.Filled.TrendingDown
                                    },

                                contentDescription = null,

                                tint = expenseColor,

                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.size(6.dp)
                    )

                    Text(
                        text = "Ausgegeben: ${
                            String.format(
                                "%.2f",
                                category.totalSum
                            )
                        } €",

                        style =
                            MaterialTheme.typography
                                .bodyMedium,

                        fontWeight =
                            FontWeight.Bold,

                        color = expenseColor
                    )
                }
            }

            // ---------------------------------------------------------
            // DELETE
            // ---------------------------------------------------------

            Icon(
                imageVector = Icons.Filled.Delete,

                contentDescription =
                    "Kategorie löschen",

                tint =
                    MaterialTheme.colorScheme.error
                        .copy(alpha = 0.70f),

                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        onDeleteCategory()
                    }
                    .padding(10.dp)
            )
        }
    }
}


// =====================================================================
// ADD CATEGORY DIALOG
// =====================================================================

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onAddCategory: (String) -> Unit
) {

    var name by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    Dialog(
        onDismissRequest = onDismiss,

        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),

            shape = RoundedCornerShape(26.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                // -----------------------------------------------------
                // ICON
                // -----------------------------------------------------

                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = TravelGreenLight
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Folder,
                            contentDescription = null,
                            tint = TravelGreen,
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // -----------------------------------------------------
                // TITLE
                // -----------------------------------------------------

                Text(
                    text = "Kategorie hinzufügen",
                    style =
                        MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Wie möchtest du deine Reisekosten organisieren?",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // -----------------------------------------------------
                // INPUT
                // -----------------------------------------------------

                Card(
                    shape = RoundedCornerShape(16.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme
                                .surfaceVariant
                    )
                ) {

                    BasicTextField(
                        value = name,

                        onValueChange = {
                            name = it
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        singleLine = true,

                        textStyle = TextStyle(
                            color =
                                MaterialTheme.colorScheme
                                    .onSurface
                        ),

                        decorationBox = {
                                innerTextField ->

                            Box {

                                if (name.text.isEmpty()) {

                                    Text(
                                        text =
                                            "z. B. Unterkunft",

                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .onSurfaceVariant
                                    )
                                }

                                innerTextField()
                            }
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                // -----------------------------------------------------
                // BUTTONS
                // -----------------------------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.End,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    TextButton(
                        onClick = onDismiss
                    ) {

                        Text(
                            text = "Abbrechen"
                        )
                    }

                    Spacer(
                        modifier = Modifier.size(6.dp)
                    )

                    Button(
                        onClick = {

                            val text =
                                name.text.trim()

                            if (text.isNotEmpty()) {

                                onAddCategory(text)
                            }
                        },

                        enabled =
                            name.text
                                .trim()
                                .isNotEmpty(),

                        shape =
                            RoundedCornerShape(14.dp)
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Add,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.size(5.dp)
                        )

                        Text(
                            text = "Hinzufügen", color = Color.White
                        )
                    }
                }
            }
        }
    }
}