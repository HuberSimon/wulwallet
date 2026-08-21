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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.wulwallet.data.local.Category
import com.example.wulwallet.ui.CategoryViewModel

@Composable
fun CategoryScreen(
    categoryViewModel: CategoryViewModel,
    navController: NavController
) {

    val allCategories by categoryViewModel.allCategories.collectAsState(initial = emptyList())

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
                .padding(horizontal = 16.dp)
        ) {

            if (allCategories.isEmpty()) {

                item {
                    Text(
                        text = "Erstelle Kategorien für Ausgaben auf der Reise",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            } else {

                item {

                    Text(
                        text = "Reisekosten: ${
                            String.format("%.2f", totalTravelCosts)
                        } €",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }


            items(allCategories) { category ->

                CategoryItem(
                    category = category,

                    onDeleteCategory = {
                        categoryToDelete = category
                    },

                    onClick = {

                        val categoryId = category.id

                        navController.navigate(
                            "costs_screen/$categoryId"
                        )
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
                imageVector = Icons.Filled.Add,
                contentDescription = "Kategorie hinzufügen",
                tint = Color.White
            )
        }
    }


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

    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                categoryToDelete = null
            },

            title = {
                Text("Kategorie löschen?")
            },

            text = {
                Text(
                    "Möchtest du die Kategorie " +
                            "\"${categoryToDelete!!.name}\" wirklich löschen?"
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        categoryToDelete?.let { category ->
                            categoryViewModel.deleteCategory(category)
                        }

                        categoryToDelete = null
                    }
                ) {
                    Text(
                        "Löschen",
                        color = Color.White
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        categoryToDelete = null
                    }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }
}



@Composable
fun CategoryItem(
    category: Category,
    onDeleteCategory: () -> Unit,
    onClick: () -> Unit
) {


    val expenseColor = if (
        category.plannedSum > 0f &&
        category.totalSum > category.plannedSum
    ) {

        Color(0xFFFF6B6B)

    } else {

        Color(0xFF66BB6A)

    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },

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


            Column(
                modifier = Modifier.weight(1f)
            ) {


                Text(
                    text = category.name,

                    style = MaterialTheme.typography.titleLarge,

                    fontWeight = FontWeight.Bold
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text = "Geplant: ${
                        String.format("%.2f", category.plannedSum)
                    } €",

                    style = MaterialTheme.typography.bodyMedium,

                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


                Spacer(
                    modifier = Modifier.height(4.dp)
                )


                Text(
                    text = "Ausgegeben: ${
                        String.format("%.2f", category.totalSum)
                    } €",

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.SemiBold,

                    color = expenseColor
                )
            }



            Icon(
                imageVector = Icons.Filled.Delete,

                contentDescription = "Kategorie löschen",

                tint = MaterialTheme.colorScheme.error,

                modifier = Modifier
                    .clickable {
                        onDeleteCategory()
                    }
                    .padding(8.dp)
            )
        }
    }
}



@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onAddCategory: (String) -> Unit
) {


    var name by remember {
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
                    text = "Kategorie hinzufügen",

                    style = MaterialTheme.typography.titleMedium
                )


                BasicTextField(

                    value = name,

                    onValueChange = {
                        name = it
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { keyEvent ->

                            if (keyEvent.key == Key.Enter) {

                                if (name.text.isNotEmpty()) {

                                    onAddCategory(
                                        name.text.removeSuffix("\n")
                                    )

                                    onDismiss()
                                }

                                true

                            } else {

                                false
                            }
                        },


                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),


                    decorationBox = { innerTextField ->

                        Box(
                            modifier = Modifier.padding(8.dp)
                        ) {

                            if (name.text.isEmpty()) {

                                Text(
                                    text = "Bitte Namen eingeben",

                                    color = Color.Gray
                                )
                            }

                            innerTextField()
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

                        Text("Abbrechen")
                    }



                    Button(
                        onClick = {

                            if (name.text.isNotEmpty()) {

                                onAddCategory(name.text)

                                onDismiss()
                            }
                        }
                    ) {

                        Text(text = "OK", color = Color.White)
                    }
                }
            }
        }
    }
}