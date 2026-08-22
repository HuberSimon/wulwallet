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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Star
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
import com.example.wulwallet.data.local.Todo
import com.example.wulwallet.ui.TodoViewModel

private val TodoGreen = Color(0xFF4CAF50)
private val TravelGreenDark = Color(0xFF388E3C)
private val TravelBlue = Color(0xFF1976D2)
private val TravelOrange = Color(0xFFFF9800)

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

    val completedTodos = allTodos.count { it.completed }
    val totalTodos = allTodos.size

    val progress = if (totalTodos > 0) {
        completedTodos.toFloat() / totalTodos.toFloat()
    } else {
        0f
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---------------------------------------------------------
            // HEADER
            // ---------------------------------------------------------

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
                            text = "Reise-Checkliste",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Alles vorbereitet für dein Abenteuer?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = TravelBlue.copy(alpha = 0.12f)
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Luggage,
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

                // -----------------------------------------------------
                // FORTSCHRITTSKARTE
                // -----------------------------------------------------

                if (totalTodos > 0) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = if (
                                            completedTodos == totalTodos
                                        ) {
                                            "Alles erledigt! ✈️"
                                        } else {
                                            "Reisevorbereitung"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF1F1F1)
                                    )

                                    Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )

                                    Text(
                                        text = "$completedTodos von $totalTodos Aufgaben erledigt",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFDCDCDC)
                                    )
                                }

                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF1F1F1)
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            // Fortschrittsbalken Hintergrund
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    )
                                    .background(
                                        Color(0xFFBDBDBD)
                                            .copy(alpha = 0.35f)
                                    )
                            ) {

                                // Fortschritt
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress)
                                        .height(7.dp)
                                        .clip(
                                            RoundedCornerShape(10.dp)
                                        )
                                        .background(
                                            Color(0xFFF1F1F1)
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Meine Aufgaben",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // ---------------------------------------------------------
            // KEINE TODOS
            // ---------------------------------------------------------

            if (allTodos.isEmpty()) {

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceVariant
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
                                color = TravelOrange.copy(
                                    alpha = 0.12f
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
                                        tint = TravelOrange,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            Text(
                                text = "Noch nichts geplant",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(5.dp)
                            )

                            Text(
                                text =
                                    "Erstelle deine erste Aufgabe für die Reise.",
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Button(
                                onClick = {
                                    showDialog = true
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor =
                                            TravelGreenDark
                                    )
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier.size(6.dp)
                                )

                                Text(
                                    text = "Aufgabe hinzufügen"
                                )
                            }
                        }
                    }
                }

            } else {

                // -----------------------------------------------------
                // TODO LISTE
                // -----------------------------------------------------

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

                item {

                    Spacer(
                        modifier = Modifier.height(90.dp)
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // FLOATING ACTION BUTTON
        // -------------------------------------------------------------

        FloatingActionButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp),
            shape = RoundedCornerShape(18.dp),
            containerColor = TodoGreen,
            contentColor = Color.White
        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Aufgabe hinzufügen"
            )
        }
    }

    // -------------------------------------------------------------
    // ADD DIALOG
    // -------------------------------------------------------------

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

    // -------------------------------------------------------------
    // DELETE DIALOG
    // -------------------------------------------------------------

    if (todoToDelete != null) {

        AlertDialog(
            onDismissRequest = {
                todoToDelete = null
            },

            title = {
                Text(
                    text = "Aufgabe löschen?"
                )
            },

            text = {
                Text(
                    "Möchtest du \"${todoToDelete!!.description}\" wirklich löschen?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        todoToDelete?.let {
                            todoViewModel.deleteTodo(it)
                        }

                        todoToDelete = null
                    }
                ) {

                    Text(
                        text = "Löschen",
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

                    Text(
                        text = "Abbrechen"
                    )
                }
            }
        )
    }
}


// =====================================================================
// TODO ITEM
// =====================================================================

@Composable
fun TodoItem(
    todo: Todo,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation =
                if (todo.completed) {
                    0.dp
                } else {
                    2.dp
                }
        ),

        colors = CardDefaults.cardColors(
            containerColor =
                if (todo.completed) {

                    MaterialTheme.colorScheme.surfaceVariant
                        .copy(alpha = 0.65f)

                } else {

                    MaterialTheme.colorScheme.surface
                }
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ---------------------------------------------------------
            // CHECK BUTTON
            // ---------------------------------------------------------

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (todo.completed) {

                            TodoGreen

                        } else {

                            MaterialTheme.colorScheme
                                .surfaceVariant
                        }
                    )
                    .clickable {
                        onCheckedChange(!todo.completed)
                    },

                contentAlignment = Alignment.Center
            ) {

                if (todo.completed) {

                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Erledigt",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                } else {

                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // ---------------------------------------------------------
            // TODO TEXT
            // ---------------------------------------------------------

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp)
            ) {

                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyLarge,

                    fontWeight =
                        if (todo.completed) {
                            FontWeight.Normal
                        } else {
                            FontWeight.SemiBold
                        },

                    color =
                        if (todo.completed) {

                            MaterialTheme.colorScheme
                                .onSurfaceVariant

                        } else {

                            MaterialTheme.colorScheme
                                .onSurface
                        }
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                if (todo.completed) {

                    Text(
                        text = "Erledigt ✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = TodoGreen,
                        fontWeight = FontWeight.Medium
                    )

                } else {

                    Text(
                        text = "Noch offen",
                        style = MaterialTheme.typography.labelSmall,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }
            }

            // ---------------------------------------------------------
            // DELETE
            // ---------------------------------------------------------

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Aufgabe löschen",
                tint =
                    MaterialTheme.colorScheme.error
                        .copy(alpha = 0.75f),

                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        onDelete()
                    }
                    .padding(10.dp)
            )
        }
    }
}


// =====================================================================
// ADD TODO DIALOG
// =====================================================================

@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onAddTodo: (String) -> Unit
) {

    var description by remember {
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
                // DIALOG ICON
                // -----------------------------------------------------

                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = TravelBlue.copy(alpha = 0.12f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Luggage,
                            contentDescription = null,
                            tint = TravelBlue,
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
                    text = "Neue Reiseaufgabe",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Was möchtest du vor oder während deiner Reise erledigen?",
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
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
                        value = description,

                        onValueChange = {
                            description = it
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

                        decorationBox = { innerTextField ->

                            Box {

                                if (description.text.isEmpty()) {

                                    Text(
                                        text =
                                            "z. B. Reisepass einpacken",

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
                                description.text.trim()

                            if (text.isNotEmpty()) {

                                onAddTodo(text)
                            }
                        },

                        enabled =
                            description.text
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
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.size(5.dp)
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