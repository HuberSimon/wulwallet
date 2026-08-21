package com.example.wulwallet.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wulwallet.data.local.Reminder
import com.example.wulwallet.data.local.Category
import com.example.wulwallet.ui.CategoryViewModel
import com.example.wulwallet.ui.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderScreen(categoryViewModel: CategoryViewModel, reminderViewModel: ReminderViewModel) {
    val allCategories by categoryViewModel.allCategories.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    val reminderCount by reminderViewModel.reminderCount.collectAsState(initial = 0)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (reminderCount!! == 0) {
                item {
                    Text(
                        text = "Noch keine wichtigen Termine für Personen erstellt",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(25.dp)) }

            items(allCategories) { category ->
                ReminderList(
                    category = category,
                    reminderViewModel
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
            Icon(Icons.Filled.Add, contentDescription = "Wichtigen Termin hinzufügen", tint = Color.White)
        }
    }

    if (showDialog) {
        AddReminderDialog(
            allCategories = allCategories,
            onDismiss = { showDialog = false },
            reminderViewModel = reminderViewModel
        )
    }
}

@Composable
fun ReminderList(
    category: Category,
    reminderViewModel: ReminderViewModel
) {
    var reminders by remember { mutableStateOf<List<Reminder>>(emptyList()) }
    reminderViewModel.getRemindersForUser(category.id) { fetchedReminders ->
        reminders = fetchedReminders
    }
    if(reminders.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                for (reminder in reminders) {
                    ReminderListItem(reminder = reminder)
                }

            }
        }
    }
}

@Composable
fun ReminderListItem(reminder: Reminder) {
    val daysUntil = getDaysUntil(reminder.importantDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(reminder.importantDate),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = reminder.description,
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            text = "${daysUntil} Tage",
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

fun getDaysUntil(date: Date): Long {
    val currentCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val targetCalendar = Calendar.getInstance().apply {
        time = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val diffInMillis = targetCalendar.timeInMillis - currentCalendar.timeInMillis
    return diffInMillis / (1000 * 60 * 60 * 24)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    allCategories: List<Category>,
    reminderViewModel: ReminderViewModel,
    onDismiss: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Wählen Sie einen Benutzer:", style = MaterialTheme.typography.bodyMedium)

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {expanded = !expanded}
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Benutzer wählen") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        for(person in allCategories){
                            DropdownMenuItem(
                                text = { Text(text = person.name) },
                                onClick = {
                                    selectedCategory = person
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                DatePickerDialog(
                    selectedDate = date,
                    onDateSelected = { date = it }
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (description.isEmpty()) {
                            Text("Wichtiger Tag z.B. Geburtstag", color = Color.Gray)
                        }
                        innerTextField()
                    },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Abbrechen")
                    }
                    Button(
                        onClick = {
                            if (selectedCategory == null) {
                                Toast.makeText(context, "Bitte einen Benutzer auswählen!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (date.isEmpty()) {
                                Toast.makeText(context, "Bitte ein Datum eingeben!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (description.isEmpty()) {
                                Toast.makeText(context, "Bitte eine Beschreibung eingeben!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val parsedDate = try {
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
                            } catch (e: Exception) {
                                null
                            }

                            if (parsedDate == null) {
                                Toast.makeText(context, "Ungültiges Datum!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            reminderViewModel.addReminder(
                                userId = selectedCategory!!.id,
                                importantDate = parsedDate,
                                description = description
                            )

                            onDismiss()
                        }
                    ) {
                        Text("OK")
                    }
                }   
            }
        }
    }
}



@Composable
fun DatePickerDialog(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val cal = Calendar.getInstance()

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Text(
        text = if (selectedDate.isNotEmpty()) selectedDate else "Wählen Sie ein Datum",
        modifier = Modifier.clickable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selected = Calendar.getInstance()
                        selected.set(year, month, dayOfMonth)
                        val formattedDate = dateFormatter.format(selected.time)
                        onDateSelected(formattedDate)
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}
