package com.example.wulwallet.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Luggage
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.wulwallet.data.local.Costs
import com.example.wulwallet.ui.CategoryViewModel
import com.example.wulwallet.ui.CostsViewModel

// ================================================================
// REISE-FARBEN
// ================================================================

private val TravelGreen = Color(0xFF43A047)
private val TravelGreenDark = Color(0xFF388E3C)
private val TravelGreenLight = Color(0xFFE8F5E9)

private val TravelBlue = Color(0xFF1976D2)
private val TravelBlueLight = Color(0xFFE3F2FD)

private val ExpenseRed = Color(0xFFE85D5D)
private val ExpenseRedLight = Color(0xFFFDECEC)

private val LightText = Color(0xFFF1F1F1)
private val SecondaryLightText = Color(0xFFDCDCDC)


// ================================================================
// COSTS SCREEN
// ================================================================

@Composable
fun CostsScreen(
    categoryViewModel: CategoryViewModel,
    costsViewModel: CostsViewModel,
    selectedCategoryId: Int
) {
    val selectedCategory by categoryViewModel.selectedCategory
        .collectAsState()

    val costs by costsViewModel.categoryCosts.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var refreshKey by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(refreshKey) {

        categoryViewModel.getCategoryById(
            selectedCategoryId
        )

        costsViewModel.allCostsByCategory(
            selectedCategoryId
        )
    }

    val totalCosts = costs
        .sumOf { it.amount.toDouble() }
        .toFloat()

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

            // ========================================================
            // HEADER
            // ========================================================

            item {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                selectedCategory?.name
                                    ?: "Kosten",

                            style =
                                MaterialTheme.typography
                                    .headlineMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Alle Ausgaben dieser Kategorie.",

                            style =
                                MaterialTheme.typography
                                    .bodyMedium,

                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = TravelBlueLight
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = TravelBlue,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // ====================================================
                // GESAMTAUSGABEN
                // ====================================================

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(22.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                TravelGreen
                        ),

                    elevation =
                        CardDefaults.cardElevation(
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

                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape =
                                RoundedCornerShape(15.dp),

                            color =
                                Color.White.copy(
                                    alpha = 0.15f
                                )
                        ) {

                            Box(
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.ReceiptLong,

                                    contentDescription =
                                        null,

                                    tint = LightText,

                                    modifier =
                                        Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Gesamtausgaben",

                                style =
                                    MaterialTheme.typography
                                        .bodyMedium,

                                color =
                                    SecondaryLightText
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    String.format(
                                        "%.2f €",
                                        totalCosts
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

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            // ========================================================
            // KEINE AUSGABEN
            // ========================================================

            if (costs.isEmpty()) {

                item {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(22.dp),

                        colors =
                            CardDefaults.cardColors(
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
                                modifier =
                                    Modifier.size(64.dp),

                                shape =
                                    CircleShape,

                                color =
                                    TravelBlueLight
                            ) {

                                Box(
                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Filled
                                                .ReceiptLong,

                                        contentDescription =
                                            null,

                                        tint = TravelBlue,

                                        modifier =
                                            Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(14.dp)
                            )

                            Text(
                                text =
                                    "Noch keine Ausgaben",

                                style =
                                    MaterialTheme.typography
                                        .titleMedium,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(5.dp)
                            )

                            Text(
                                text =
                                    "Füge deine erste Ausgabe für diese Kategorie hinzu.",

                                style =
                                    MaterialTheme.typography
                                        .bodyMedium,

                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
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
                                            TravelGreen
                                    )
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Add,
                                    contentDescription =
                                        null
                                )

                                Spacer(
                                    modifier =
                                        Modifier.size(6.dp)
                                )

                                Text(
                                    text =
                                        "Ausgabe hinzufügen"
                                )
                            }
                        }
                    }
                }

            } else {

                // ====================================================
                // AUSGABEN HEADER
                // ====================================================

                item {

                    Text(
                        text = "Ausgaben",

                        style =
                            MaterialTheme.typography
                                .titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        modifier =
                            Modifier.padding(
                                top = 8.dp,
                                bottom = 2.dp
                            )
                    )
                }

                // ====================================================
                // AUSGABEN
                // ====================================================

                items(
                    items = costs,
                    key = { it.id }
                ) { cost ->

                    CostItem(
                        cost = cost,

                        onDeleteCost = {

                            costsViewModel.deleteCosts(
                                cost
                            )

                            selectedCategory?.let { category ->

                                categoryViewModel.updateCategory(
                                    category.copy(
                                        totalSum =
                                            if (
                                                category.totalSum -
                                                cost.amount < 0f
                                            ) {
                                                0f
                                            } else {
                                                category.totalSum -
                                                        cost.amount
                                            }
                                    )
                                )
                            }

                            refreshKey++
                        }
                    )
                }

                item {

                    Spacer(
                        modifier =
                            Modifier.height(90.dp)
                    )
                }
            }
        }

        // ============================================================
        // FLOATING ACTION BUTTON
        // ============================================================

        FloatingActionButton(
            onClick = {
                showDialog = true
            },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp),

            shape =
                RoundedCornerShape(18.dp),

            containerColor =
                TravelGreen,

            contentColor =
                Color.White
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Add,

                contentDescription =
                    "Kosten hinzufügen"
            )
        }
    }

    // ================================================================
    // ADD COST DIALOG
    // ================================================================

    if (showDialog) {

        AddCostDialog(

            onDismiss = {
                showDialog = false
            },

            onAddCost = { name, amount ->

                costsViewModel.addCosts(
                    Costs(
                        description = name,
                        amount = amount,
                        categoryId =
                            selectedCategoryId
                    )
                )

                selectedCategory?.let { category ->

                    categoryViewModel.updateCategory(
                        category.copy(
                            totalSum =
                                category.totalSum +
                                        amount
                        )
                    )
                }

                refreshKey++
                showDialog = false
            }
        )
    }
}


// ================================================================
// ADD COST DIALOG
// ================================================================

@Composable
fun AddCostDialog(
    onDismiss: () -> Unit,
    onAddCost: (String, Float) -> Unit
) {

    var name by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    var amount by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    Dialog(
        onDismissRequest = onDismiss,

        properties =
            DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),

            shape =
                RoundedCornerShape(26.dp),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(24.dp)
            ) {

                // ----------------------------------------------------
                // ICON
                // ----------------------------------------------------

                Surface(
                    modifier =
                        Modifier.size(52.dp),

                    shape =
                        CircleShape,

                    color =
                        TravelGreenLight
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.ReceiptLong,

                            contentDescription =
                                null,

                            tint =
                                TravelGreen,

                            modifier =
                                Modifier.size(27.dp)
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                // ----------------------------------------------------
                // TITLE
                // ----------------------------------------------------

                Text(
                    text =
                        "Neue Ausgabe",

                    style =
                        MaterialTheme.typography
                            .headlineSmall,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Was hast du auf deiner Reise ausgegeben?",

                    style =
                        MaterialTheme.typography
                            .bodyMedium,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                // ----------------------------------------------------
                // DESCRIPTION
                // ----------------------------------------------------

                Text(
                    text = "Beschreibung",

                    style =
                        MaterialTheme.typography
                            .labelLarge,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Card(
                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        CardDefaults.cardColors(
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

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),

                        singleLine = true,

                        textStyle =
                            TextStyle(
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurface
                            ),

                        decorationBox = {
                                innerTextField ->

                            Box {

                                if (
                                    name.text.isEmpty()
                                ) {

                                    Text(
                                        text =
                                            "z. B. Restaurant",

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
                    modifier =
                        Modifier.height(14.dp)
                )

                // ----------------------------------------------------
                // AMOUNT
                // ----------------------------------------------------

                Text(
                    text = "Betrag",

                    style =
                        MaterialTheme.typography
                            .labelLarge,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Card(
                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme
                                    .surfaceVariant
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

                        BasicTextField(
                            value = amount,

                            onValueChange = { newValue ->

                                val regex =
                                    Regex(
                                        """^\d*([.,]\d{0,2})?$"""
                                    )

                                if (
                                    newValue.text
                                        .matches(regex)
                                ) {

                                    amount =
                                        newValue
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

                            textStyle =
                                TextStyle(
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
                                        amount.text.isEmpty()
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
                            modifier =
                                Modifier.width(8.dp)
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
                    modifier =
                        Modifier.height(24.dp)
                )

                // ----------------------------------------------------
                // BUTTONS
                // ----------------------------------------------------

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

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
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Button(
                        onClick = {

                            val value =
                                amount.text
                                    .replace(
                                        ",",
                                        "."
                                    )
                                    .toFloatOrNull()

                            if (
                                name.text.isNotBlank() &&
                                value != null
                            ) {

                                onAddCost(
                                    name.text.trim(),
                                    value
                                )
                            }
                        },

                        enabled =
                            name.text.isNotBlank() &&
                                    amount.text
                                        .trim()
                                        .isNotEmpty(),

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

                            contentDescription =
                                null
                        )

                        Spacer(
                            modifier =
                                Modifier.size(5.dp)
                        )

                        Text(
                            text = "Hinzufügen"
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// COST ITEM
// ================================================================

@Composable
fun CostItem(
    cost: Costs,
    onDeleteCost: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // --------------------------------------------------------
            // ICON
            // --------------------------------------------------------

            Surface(
                modifier =
                    Modifier.size(50.dp),

                shape =
                    RoundedCornerShape(15.dp),

                color =
                    ExpenseRedLight
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.ReceiptLong,

                        contentDescription =
                            null,

                        tint =
                            ExpenseRed,

                        modifier =
                            Modifier.size(25.dp)
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.width(14.dp)
            )

            // --------------------------------------------------------
            // DESCRIPTION
            // --------------------------------------------------------

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        cost.description,

                    style =
                        MaterialTheme.typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text = "Ausgabe",

                    style =
                        MaterialTheme.typography
                            .labelMedium,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }

            // --------------------------------------------------------
            // AMOUNT
            // --------------------------------------------------------

            Text(
                text =
                    String.format(
                        "%.2f €",
                        cost.amount
                    ),

                style =
                    MaterialTheme.typography
                        .titleMedium,

                fontWeight =
                    FontWeight.Bold,

                color =
                    ExpenseRed
            )

            Spacer(
                modifier =
                    Modifier.width(4.dp)
            )

            // --------------------------------------------------------
            // DELETE
            // --------------------------------------------------------

            Icon(
                imageVector =
                    Icons.Filled.Delete,

                contentDescription =
                    "Löschen",

                tint =
                    MaterialTheme.colorScheme
                        .error
                        .copy(alpha = 0.70f),

                modifier =
                    Modifier
                        .size(42.dp)
                        .clickable {
                            onDeleteCost()
                        }
                        .padding(10.dp)
            )
        }
    }
}