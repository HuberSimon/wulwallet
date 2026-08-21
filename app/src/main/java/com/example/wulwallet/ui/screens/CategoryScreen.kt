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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.wulwallet.data.local.Person
import com.example.wulwallet.ui.PersonViewModel

@Composable
fun PersonScreen(personViewModel: PersonViewModel, navController: NavController) {
    val allPersons by personViewModel.allPersons.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (allPersons.isEmpty()) {
                item {
                    Text(
                        text = "Erstelle Personen, die beschenkt werden sollen",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            items(allPersons) { person ->
                PersonItem(
                    person,
                    onDeletePerson = {
                        personViewModel.deletePerson(person)
                    },
                    onClick = {
                        val personId = person.id
                        navController.navigate("idea_screen/$personId")
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
            Icon(Icons.Filled.Add, contentDescription = "Person hinzufügen", tint = Color.White)
        }
    }

    if (showDialog) {
        AddPersonDialog(
            onDismiss = { showDialog = false },
            onAddPerson = { name ->
                personViewModel.addPerson(Person(name = name, pin = "", pinLock = false))
                showDialog = false
            }
        )
    }
}


@Composable
fun AddPersonDialog(onDismiss: () -> Unit, onAddPerson: (String) -> Unit) {
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
                Text(text = "Person hinzufügen", style = MaterialTheme.typography.titleMedium)

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter) {
                                if (name.text.isNotEmpty()) {
                                    onAddPerson(name.text.removeSuffix("\n"))
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
                            if (name.text.isEmpty()) Text("Bitte Namen eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Abbrechen")
                    }
                    Button(onClick = {
                        if (name.text.isNotEmpty()) {
                            onAddPerson(name.text)
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
fun PersonItem(
    person: Person,
    onDeletePerson: () -> Unit,
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
                        text = person.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .clickable {
                                onDeletePerson()
                            }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
