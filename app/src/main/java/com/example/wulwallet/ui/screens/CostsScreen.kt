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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
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


@Composable
fun CostsScreen(
    categoryViewModel: CategoryViewModel,
    costsViewModel: CostsViewModel,
    selectedCategoryId: Int
) {

    val selectedCategory by categoryViewModel.selectedCategory.collectAsState()
    val costs by costsViewModel.categoryCosts.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var refreshKey by remember { mutableStateOf(0) }


    LaunchedEffect(refreshKey) {
        categoryViewModel.getCategoryById(selectedCategoryId)
        costsViewModel.allCostsByCategory(selectedCategoryId)
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(
                    modifier = Modifier.height(25.dp)
                )
                Text(
                    text = "Kosten für ${selectedCategory?.name}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(
                    modifier = Modifier.height(25.dp)
                )
            }


            items(costs) { cost ->
                CostItem(
                    cost,
                    onDeleteCost = {
                        costsViewModel.deleteCosts(cost)
                        selectedCategory?.let { category ->
                            categoryViewModel.updateCategory(
                                category.copy(
                                    totalSum = if (category.totalSum - cost.amount < 0f) {
                                        0f
                                    } else {
                                        category.totalSum - cost.amount
                                    }
                                )
                            )
                        }
                        refreshKey++
                    }
                )
            }
        }


        FloatingActionButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Kosten hinzufügen",
                tint = Color.White
            )
        }
    }
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
                        categoryId = selectedCategoryId
                    )
                )
                selectedCategory?.let { category ->
                    categoryViewModel.updateCategory(
                        category.copy(
                            totalSum = category.totalSum + amount
                        )
                    )
                }
                refreshKey++
                showDialog = false
            }
        )
    }
}




@Composable
fun AddCostDialog(
    onDismiss: () -> Unit,
    onAddCost: (String, Float) -> Unit
) {
    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var amount by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {

        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Kosten hinzufügen",
                    style = MaterialTheme.typography.titleMedium
                )

                BasicTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { event ->
                            if (event.key == Key.Enter) {
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    decorationBox = { inner ->
                        Box(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            if (name.text.isEmpty()) {
                                Text(
                                    "Kostenbeschreibung eingeben",
                                    color = Color.Gray
                                )
                            }
                            inner()
                        }
                    }
                )

                BasicTextField(
                    value = amount,
                    onValueChange = { newValue ->
                        val text = newValue.text
                        val regex = Regex("""^\d*([.,]\d{0,2})?$""")

                        if (text.matches(regex)) {
                            amount = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    decorationBox = { inner ->
                        Box(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            if (amount.text.isEmpty()) {
                                Text(
                                    "Betrag eingeben",
                                    color = Color.Gray
                                )
                            }
                            inner()
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Abbrechen", color = Color.White)
                    }

                    Button(
                        onClick = {
                            val value = amount.text
                                .replace(",", ".")
                                .toFloatOrNull()

                            if (
                                name.text.isNotBlank()
                                && value != null
                            ) {
                                onAddCost(
                                    name.text.trim(),
                                    value
                                )
                                onDismiss()
                            }
                        }
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CostItem(
    cost: Costs,
    onDeleteCost: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = cost.description,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${String.format("%.2f", cost.amount)} €",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Löschen",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clickable {
                        onDeleteCost()
                    }
            )
        }
    }
}