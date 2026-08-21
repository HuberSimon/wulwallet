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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wulwallet.data.local.Todo
import com.example.wulwallet.ui.TodoViewModel
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CheckboxDefaults

@Composable
fun TodoScreen(
    todoViewModel: TodoViewModel
) {

    val allTodos by todoViewModel.allTodos.collectAsState(
        initial = emptyList()
    )

    var showDialog by remember {
        mutableStateOf(false)
    }

    var todoToDelete by remember {
        mutableStateOf<Todo?>(null)
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            if (allTodos.isEmpty()) {

                item {

                    Text(
                        text = "Keine Aufgaben vorhanden",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

            } else {

                item {

                    Text(
                        text = "Aufgaben",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 16.dp,
                            bottom = 8.dp
                        )
                    )
                }


                items(
                    items = allTodos,
                    key = { it.id }
                ) { todo ->

                    TodoItem(
                        todo = todo,

                        onCheckedChange = { checked ->

                            todoViewModel.updateTodo(
                                todo.copy(
                                    completed = checked
                                )
                            )
                        },

                        onDelete = {

                            todoToDelete = todo
                        }
                    )
                }
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
                contentDescription = "Aufgabe hinzufügen",
                tint = Color.White
            )
        }
    }


    if (showDialog) {

        AddTodoDialog(

            onDismiss = {
                showDialog = false
            },

            onAddTodo = { description ->

                todoViewModel.addTodo(description)

                showDialog = false
            }
        )
    }


    if (todoToDelete != null) {

        AlertDialog(

            onDismissRequest = {
                todoToDelete = null
            },

            title = {
                Text("Aufgabe löschen?")
            },

            text = {
                Text(
                    "Möchtest du die Aufgabe " +
                            "\"${todoToDelete!!.description}\" wirklich löschen?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        todoToDelete?.let { todo ->
                            todoViewModel.deleteTodo(todo)
                        }

                        todoToDelete = null
                    }
                ) {

                    Text(
                        "Löschen",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        todoToDelete = null
                    }
                ) {

                    Text("Abbrechen")
                }
            }
        )
    }
}


@Composable
fun TodoItem(
    todo: Todo,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // LINKS: Checkbox
            Checkbox(
                checked = todo.completed,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Green,
                    checkmarkColor = Color.White
                )
            )


            // MITTE: Beschreibung
            Text(
                text = todo.description,

                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),

                style = MaterialTheme.typography.bodyLarge,

                fontWeight = if (todo.completed) {
                    FontWeight.Normal
                } else {
                    FontWeight.Medium
                },

                color = if (todo.completed) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )


            // RECHTS: Mülleimer
            Icon(
                imageVector = Icons.Filled.Delete,

                contentDescription = "Aufgabe löschen",

                tint = MaterialTheme.colorScheme.error,

                modifier = Modifier
                    .clickable {
                        onDelete()
                    }
                    .padding(8.dp)
            )
        }
    }
}


@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onAddTodo: (String) -> Unit
) {

    var description by remember {
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
                    text = "Aufgabe hinzufügen",

                    style = MaterialTheme.typography.titleMedium
                )


                BasicTextField(

                    value = description,

                    onValueChange = {
                        description = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),

                    decorationBox = { innerTextField ->

                        Box(
                            modifier = Modifier.padding(8.dp)
                        ) {

                            if (description.text.isEmpty()) {

                                Text(
                                    text = "Aufgabe eingeben",

                                    color = Color.Gray
                                )
                            }

                            innerTextField()
                        }
                    }
                )


                Spacer(
                    modifier = Modifier.height(4.dp)
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

                            val text = description.text.trim()

                            if (text.isNotEmpty()) {

                                onAddTodo(text)

                                onDismiss()
                            }
                        }
                    ) {

                        Text(
                            text = "OK",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}