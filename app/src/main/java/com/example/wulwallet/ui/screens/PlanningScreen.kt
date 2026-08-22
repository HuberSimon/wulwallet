package com.example.wulwallet.ui.screens

import android.R
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wulwallet.data.local.Category
import com.example.wulwallet.ui.CategoryViewModel

// ================================================================
// REISE-FARBEN
// ================================================================

private val PlanningGreen = Color(0xFF43A047)
private val PlanningGreenDark = Color(0xFF388E3C)
private val PlanningGreenLight = Color(0xFFE8F5E9)

private val TravelBlue = Color(0xFF1976D2)
private val TravelBlueLight = Color(0xFFE3F2FD)

private val TravelOrange = Color(0xFFFF9800)

private val LightText = Color(0xFFF1F1F1)
private val SecondaryLightText = Color(0xFFDCDCDC)


// ================================================================
// PLANNING SCREEN
// ================================================================

@Composable
fun PlanningScreen(
    categoryViewModel: CategoryViewModel,
) {
    val allCategories by categoryViewModel.allCategories.collectAsState(
        initial = emptyList()
    )

    val plannedTotal = allCategories
        .sumOf { it.plannedSum.toDouble() }
        .toFloat()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        // ============================================================
        // HEADER
        // ============================================================

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
                        text = "Reiseplanung",
                        style =
                            MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Plane dein Budget für deine Reise.",
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
                    color = PlanningGreenLight
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Luggage,
                            contentDescription = null,
                            tint = PlanningGreen,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            PlannedTotalCard(
                plannedTotal = plannedTotal
            )
        }

        // ============================================================
        // KEINE KATEGORIEN
        // ============================================================

        if (allCategories.isEmpty()) {

            item {

                EmptyPlanningCard()
            }

        } else {

            // ========================================================
            // KATEGORIEN HEADER
            // ========================================================

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

            // ========================================================
            // KATEGORIEN
            // ========================================================

            items(
                items = allCategories,
                key = { it.id }
            ) { category ->

                PlanningCategoryItem(
                    category = category,

                    onSave = { newValue ->

                        categoryViewModel.updateCategory(
                            category.copy(
                                plannedSum = newValue
                            )
                        )
                    }
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}


// ================================================================
// PLANNED TOTAL CARD
// ================================================================

@Composable
private fun PlannedTotalCard(
    plannedTotal: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = PlanningGreen
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // --------------------------------------------------------
            // ICON
            // --------------------------------------------------------

            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(15.dp),
                color = Color.White.copy(
                    alpha = 0.15f
                )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Savings,
                        contentDescription = null,
                        tint = LightText,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            // --------------------------------------------------------
            // TEXT
            // --------------------------------------------------------

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Geplantes Gesamtbudget",

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
                        plannedTotal
                    ),

                    style =
                        MaterialTheme.typography
                            .headlineSmall,

                    fontWeight =
                        FontWeight.Bold,

                    color = LightText
                )
            }
        }
    }
}


// ================================================================
// EMPTY PLANNING CARD
// ================================================================

@Composable
private fun EmptyPlanningCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),

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
                color = PlanningGreenLight
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Savings,
                        contentDescription = null,
                        tint = PlanningGreen,
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
                    MaterialTheme.typography.titleMedium,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text =
                    "Erstelle zuerst Kategorien, um dein Reisebudget zu planen.",

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }
    }
}


// ================================================================
// PLANNING CATEGORY ITEM
// ================================================================

@Composable
fun PlanningCategoryItem(
    category: Category,
    onSave: (Float) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // --------------------------------------------------------
            // CATEGORY ICON
            // --------------------------------------------------------

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
                            Icons.Filled.Savings,
                        contentDescription = null,
                        tint = TravelBlue,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            // --------------------------------------------------------
            // BUDGET INFO
            // --------------------------------------------------------

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = category.name,

                    style =
                        MaterialTheme.typography.titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Geplantes Budget",

                    style =
                        MaterialTheme.typography.labelMedium,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = String.format(
                        "%.2f €",
                        category.plannedSum
                    ),

                    style =
                        MaterialTheme.typography.titleLarge,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        if (category.plannedSum > 0f) {
                            PlanningGreenDark
                        } else {
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                        }
                )
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            // --------------------------------------------------------
            // EDIT BUTTON
            // --------------------------------------------------------

            OutlinedButton(
                onClick = {
                    showDialog = true
                },

                shape =
                    RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = TravelOrange
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )
            }
        }
    }

    // ================================================================
    // EDIT DIALOG
    // ================================================================

    if (showDialog) {

        EditPlannedSumDialog(
            currentValue = category.plannedSum,

            onDismiss = {
                showDialog = false
            },

            onSave = {
                onSave(it)
                showDialog = false
            }
        )
    }
}


// ================================================================
// EDIT PLANNED SUM DIALOG
// ================================================================

@Composable
fun EditPlannedSumDialog(
    currentValue: Float,
    onDismiss: () -> Unit,
    onSave: (Float) -> Unit
) {

    var value by remember {

        mutableStateOf(
            TextFieldValue(
                if (currentValue == 0f) {
                    ""
                } else {
                    currentValue.toString()
                }
            )
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

                // ----------------------------------------------------
                // ICON
                // ----------------------------------------------------

                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = PlanningGreenLight
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Savings,
                            contentDescription = null,
                            tint = PlanningGreen,
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // ----------------------------------------------------
                // TITLE
                // ----------------------------------------------------

                Text(
                    text = "Budget bearbeiten",

                    style =
                        MaterialTheme.typography.headlineSmall,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Wie viel möchtest du für diese Kategorie einplanen?",

                    style =
                        MaterialTheme.typography.bodyMedium,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // ----------------------------------------------------
                // INPUT
                // ----------------------------------------------------

                Card(
                    shape = RoundedCornerShape(16.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme
                                .surfaceVariant
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 14.dp,
                                vertical = 12.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        BasicTextField(
                            value = value,

                            onValueChange = { newValue ->

                                val regex =
                                    Regex(
                                        """^\d*([.,]\d{0,2})?$"""
                                    )

                                if (
                                    newValue.text
                                        .matches(regex)
                                ) {
                                    value = newValue
                                }
                            },

                            modifier =
                                Modifier.weight(1f),

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Decimal
                                ),

                            singleLine = true,

                            textStyle = TextStyle(
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurface,

                                fontSize =
                                    MaterialTheme
                                        .typography
                                        .titleLarge
                                        .fontSize,

                                fontWeight =
                                    FontWeight.SemiBold
                            ),

                            decorationBox = {
                                    innerTextField ->

                                Box {

                                    if (
                                        value.text.isEmpty()
                                    ) {

                                        Text(
                                            text =
                                                "Betrag eingeben",

                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .titleLarge,

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

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = "€",

                            style =
                                MaterialTheme.typography
                                    .titleLarge,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // ----------------------------------------------------
                // BUTTONS
                // ----------------------------------------------------

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
                        modifier = Modifier.width(8.dp)
                    )

                    Button(
                        onClick = {

                            value.text
                                .replace(",", ".")
                                .toFloatOrNull()
                                ?.let {
                                    onSave(it)
                                }
                        },

                        enabled =
                            value.text
                                .trim()
                                .isNotEmpty(),

                        shape =
                            RoundedCornerShape(14.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    PlanningGreenDark
                            )
                    ) {

                        Text(
                            text = "Speichern",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}