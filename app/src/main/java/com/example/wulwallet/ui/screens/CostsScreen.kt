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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wulwallet.data.local.Idea
import com.example.wulwallet.ui.IdeaViewModel
import com.example.wulwallet.ui.CategoryViewModel

@Composable
fun IdeaScreen(categoryViewModel: CategoryViewModel, ideaViewModel: IdeaViewModel, selectedUserId: Int) {
    val selectedCategory by categoryViewModel.selectedCategory.collectAsState()
    val ideas by ideaViewModel.userIdeas.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var refreshKey by remember { mutableStateOf(0) }

    LaunchedEffect(refreshKey) {
        categoryViewModel.getCategoryById(selectedUserId)
        ideaViewModel.allIdeasByUser(selectedUserId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            item { Spacer(modifier = Modifier.height(25.dp)) }
            item {
                Text(text = "Geschenkideen für ${selectedCategory?.name}", style = MaterialTheme.typography.titleLarge)
            }

            item { Spacer(modifier = Modifier.height(25.dp)) }

            items(ideas) { idea ->
                IdeaItem(
                    idea,
                    onDeleteIdea = {
                        ideaViewModel.deleteIdea(idea)
                        refreshKey += 1
                    },
                    onClick = {

                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Geschenkidee hinzufügen", tint = Color.White)
        }
    }

    if (showDialog) {
        AddIdeaDialog(
            onDismiss = { showDialog = false },
            onAddIdea = { name ->
                ideaViewModel.addIdea(Idea(description = name, userId = selectedUserId))
                refreshKey += 1
                showDialog = false
            }
        )
    }
}



@Composable
fun AddIdeaDialog(onDismiss: () -> Unit, onAddIdea: (String) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Geschenkidee hinzufügen", style = MaterialTheme.typography.titleMedium)

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter) {
                                if (name.text.isNotEmpty()) {
                                    onAddIdea(name.text.removeSuffix("\n"))
                                    onDismiss()
                                }
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(8.dp)) {
                            if (name.text.isEmpty()) Text("Geschenkidee eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Abbrechen")
                        Text("Abbrechen")
                    }
                    Button(onClick = {
                        if (name.text.isNotEmpty()) {
                            onAddIdea(name.text)
                            onDismiss()
                        }
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}


@Composable
fun IdeaItem(
    idea: Idea,
    onDeleteIdea: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = idea.description,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .clickable {
                                onDeleteIdea()
                            }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
