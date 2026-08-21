package com.example.wulwallet.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
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

@Composable
fun PlanningScreen(
    categoryViewModel: CategoryViewModel,
) {

    val allCategories by categoryViewModel.allCategories.collectAsState(initial = emptyList())

    val plannedTotal = allCategories.sumOf { it.plannedSum.toDouble() }.toFloat()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Geplante Gesamtkosten: ${
                        String.format("%.2f", plannedTotal)
                    } €",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (allCategories.isEmpty()) {

                item {
                    Text(
                        "Es wurden noch keine Kategorien erstellt."
                    )
                }

            } else {

                items(allCategories) { category ->

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
            }
        }
    }
}

@Composable
fun PlanningCategoryItem(
    category: Category,
    onSave: (Float) -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Geplant: ${
                        String.format("%.2f", category.plannedSum)
                    } €",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFF6B6B)
                )
            }

            OutlinedButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Text("Bearbeiten", color = Color.White)
            }
        }
    }

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

@Composable
fun EditPlannedSumDialog(
    currentValue: Float,
    onDismiss: () -> Unit,
    onSave: (Float) -> Unit
) {

    var value by remember {
        mutableStateOf(TextFieldValue(currentValue.toString()))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {

        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Geplante Reisekosten",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                BasicTextField(
                    value = value,
                    onValueChange = { newValue ->
                        val text = newValue.text
                        val regex = Regex("""^\d*([.,]\d{0,2})?$""")

                        if (text.matches(regex)) {
                            value = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    decorationBox = { innerTextField ->

                        Box(
                            modifier = Modifier.padding(8.dp)
                        ) {

                            if (value.text.isEmpty()) {
                                Text(
                                    "Betrag eingeben",
                                    color = Color.Gray
                                )
                            }

                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

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

                            value.text
                                .replace(",", ".")
                                .toFloatOrNull()
                                ?.let {
                                    onSave(it)
                                }
                        }
                    ) {
                        Text("Speichern", color = Color.White)
                    }
                }
            }
        }
    }
}