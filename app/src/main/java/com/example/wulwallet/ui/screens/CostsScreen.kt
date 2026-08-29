// ================================================================
// FILE: ui/screens/CostsScreen.kt
// ================================================================

package com.example.wulwallet.ui.screens

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wulwallet.data.local.CostPayer
import com.example.wulwallet.data.local.CostParticipant
import com.example.wulwallet.data.local.Costs
import com.example.wulwallet.data.local.User
import com.example.wulwallet.ui.CategoryViewModel
import com.example.wulwallet.ui.CostsViewModel
import com.example.wulwallet.ui.UserViewModel
import kotlin.math.abs
import java.util.Locale

private val Green = Color(0xFF43A047)
private val GreenDark = Color(0xFF2E7D32)
private val Blue = Color(0xFF1976D2)
private val Red = Color(0xFFE85D5D)

private val LightGreen = Color(0xFFE8F5E9)
private val LightBlue = Color(0xFFE3F2FD)
private val LightRed = Color(0xFFFDECEC)

private val LightOrange = Color(0xFFFFF3E0)
private val Orange = Color(0xFFE65100)


// =================================================================
// COSTS SCREEN
// =================================================================

@Composable
fun CostsScreen(
    categoryViewModel: CategoryViewModel,
    costsViewModel: CostsViewModel,
    selectedCategoryId: Int,
    userViewModel: UserViewModel
) {

    val selectedCategory by
    categoryViewModel.selectedCategory.collectAsState()

    val costs by
    costsViewModel.categoryCosts.collectAsState()

    val users by
    userViewModel.allUsers.collectAsState()

    val splitData by
    costsViewModel.splitData.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var editingCost by remember {
        mutableStateOf<Costs?>(null)
    }

    var costToDelete by remember {
        mutableStateOf<Costs?>(null)
    }

    LaunchedEffect(
        selectedCategoryId
    ) {

        categoryViewModel.getCategoryById(
            selectedCategoryId
        )

        costsViewModel.allCostsByCategory(
            selectedCategoryId
        )

        costsViewModel.loadAllCosts()
    }

    val total =
        costs.sumOf {
            it.amount.toDouble()
        }.toFloat()

    Box(
        modifier =
            Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp),

            contentPadding =
                PaddingValues(
                    top = 12.dp,
                    bottom = 100.dp
                )
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            item {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                selectedCategory?.name
                                    ?: "Kosten",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "${costs.size} Ausgaben",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    Surface(
                        modifier =
                            Modifier.size(48.dp),

                        shape =
                            CircleShape,

                        color =
                            LightBlue
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
                                    Blue
                            )
                        }
                    }
                }

                Spacer(
                    Modifier.height(16.dp)
                )

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(22.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Green
                        )
                ) {

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(20.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    "Gesamtausgaben",

                                color =
                                    Color.White.copy(
                                        alpha = 0.75f
                                    )
                            )

                            Spacer(
                                Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    formatEuro(
                                        total
                                    ) + " €",

                                color =
                                    Color.White,

                                style =
                                    MaterialTheme
                                        .typography
                                        .headlineSmall,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }

                        Surface(
                            shape =
                                CircleShape,

                            color =
                                Color.White.copy(
                                    alpha = 0.15f
                                )
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Payments,

                                contentDescription =
                                    null,

                                tint =
                                    Color.White,

                                modifier =
                                    Modifier.padding(11.dp)
                            )
                        }
                    }
                }

                Spacer(
                    Modifier.height(10.dp)
                )
            }


            // =====================================================
            // KEINE KOSTEN
            // =====================================================

            if (costs.isEmpty()) {

                item {

                    EmptyCostsCard(
                        onAdd = {

                            editingCost = null
                            showDialog = true
                        }
                    )
                }

            } else {

                item {

                    Text(
                        text =
                            "Ausgaben",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        fontWeight =
                            FontWeight.Bold
                    )
                }


                // =================================================
                // COST ITEMS
                // =================================================

                items(
                    items = costs,

                    key = {
                        it.id
                    }
                ) { cost ->

                    val split =
                        splitData.firstOrNull {
                            it.cost.id == cost.id
                        }

                    val participantIds =
                        split
                            ?.participants
                            ?.map {
                                it.userId
                            }
                            ?: emptyList()

                    val payerIds =
                        split
                            ?.payers
                            ?.map {
                                it.userId
                            }
                            ?: emptyList()

                    val participants =
                        users.filter {
                            it.id in participantIds
                        }

                    val payers =
                        users.filter {
                            it.id in payerIds
                        }

                    CostItem(
                        cost = cost,

                        participants =
                            participants,

                        payers =
                            payers,

                        onEdit = {

                            editingCost = cost
                            showDialog = true
                        },

                        onDelete = {
                            costToDelete = cost
                        }
                    )
                }
            }
        }


        // =========================================================
        // ADD BUTTON
        // =========================================================

        FloatingActionButton(
            onClick = {

                editingCost = null
                showDialog = true
            },

            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp),

            containerColor =
                Green,

            contentColor =
                Color.White,

            shape =
                RoundedCornerShape(18.dp)
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Add,

                contentDescription =
                    "Ausgabe hinzufügen"
            )
        }
    }


    // ============================================================
    // ADD / EDIT DIALOG
    // ============================================================

    if (showDialog) {

        AddCostDialog(
            users = users,

            existingCost =
                editingCost,

            existingSplit =
                editingCost?.let { cost ->

                    splitData.firstOrNull {
                        it.cost.id == cost.id
                    }
                },

            onDismiss = {

                showDialog = false
                editingCost = null
            },

            onSave = {
                    description,
                    amount,
                    payers,
                    participants ->

                val currentCost =
                    editingCost

                if (currentCost == null) {

                    // =================================================
                    // NEUE AUSGABE
                    // =================================================

                    costsViewModel.addCosts(

                        costs =
                            Costs(
                                description =
                                    description,

                                amount =
                                    amount,

                                categoryId =
                                    selectedCategoryId
                            ),

                        payers =
                            payers,

                        participants =
                            participants

                    ) {

                        showDialog = false
                        editingCost = null

                        costsViewModel
                            .allCostsByCategory(
                                selectedCategoryId
                            )

                        costsViewModel
                            .loadAllCosts()
                    }


                    // Gesamte Rechnung der Kategorie
                    selectedCategory?.let { category ->

                        categoryViewModel
                            .updateCategory(
                                category.copy(
                                    totalSum =
                                        category.totalSum +
                                                amount
                                )
                            )
                    }

                } else {

                    // =================================================
                    // AUSGABE BEARBEITEN
                    // =================================================

                    val oldAmount =
                        currentCost.amount

                    val updatedCost =
                        currentCost.copy(

                            description =
                                description,

                            amount =
                                amount,

                            categoryId =
                                selectedCategoryId
                        )

                    costsViewModel.updateCosts(

                        costs =
                            updatedCost,

                        payers =
                            payers,

                        participants =
                            participants

                    ) {

                        showDialog = false
                        editingCost = null

                        costsViewModel
                            .allCostsByCategory(
                                selectedCategoryId
                            )

                        costsViewModel
                            .loadAllCosts()
                    }


                    // Kategorie-Gesamtsumme
                    selectedCategory?.let { category ->

                        val difference =
                            amount -
                                    oldAmount

                        categoryViewModel
                            .updateCategory(
                                category.copy(
                                    totalSum =
                                        (
                                                category.totalSum +
                                                        difference
                                                ).coerceAtLeast(
                                                0f
                                            )
                                )
                            )
                    }
                }
            }
        )
    }
    if (costToDelete != null) {

        val cost = costToDelete!!

        AlertDialog(
            onDismissRequest = {
                costToDelete = null
            },

            title = {
                Text(
                    text = "Ausgabe löschen?"
                )
            },

            text = {
                Text(
                    text =
                        "Möchtest du die Ausgabe „${cost.description}“ wirklich löschen?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        costsViewModel.deleteCosts(
                            cost
                        )

                        selectedCategory?.let { category ->

                            categoryViewModel
                                .updateCategory(
                                    category.copy(
                                        totalSum =
                                            (
                                                    category.totalSum -
                                                            cost.amount
                                                    ).coerceAtLeast(
                                                    0f
                                                )
                                    )
                                )
                        }

                        costToDelete = null
                    }
                ) {

                    Text(
                        text = "Löschen",
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        costToDelete = null
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


// =================================================================
// EMPTY COSTS CARD
// =================================================================

@Composable
private fun EmptyCostsCard(
    onAdd: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant
            )
    ) {

        Column(
            modifier =
                Modifier
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
                    LightBlue
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
                            Blue,

                        modifier =
                            Modifier.size(30.dp)
                    )
                }
            }

            Spacer(
                Modifier.height(14.dp)
            )

            Text(
                text =
                    "Noch keine Ausgaben",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                Modifier.height(5.dp)
            )

            Text(
                text =
                    "Füge deine erste Ausgabe hinzu.",

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                Modifier.height(18.dp)
            )

            Button(
                onClick =
                    onAdd,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            GreenDark
                    )
            ) {

                Icon(
                    Icons.Filled.Add,
                    contentDescription =
                        null
                )

                Spacer(
                    Modifier.width(6.dp)
                )

                Text(
                    "Ausgabe hinzufügen"
                )
            }
        }
    }
}


// =================================================================
// COST ITEM
// =================================================================

@Composable
private fun CostItem(
    cost: Costs,
    participants: List<User>,
    payers: List<User>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val payerIds =
        payers
            .map {
                it.id
            }
            .toSet()

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onEdit()
                },

        shape =
            RoundedCornerShape(20.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(15.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    modifier =
                        Modifier.size(48.dp),

                    shape =
                        RoundedCornerShape(14.dp),

                    color =
                        LightRed
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            Icons.Filled.ReceiptLong,
                            contentDescription =
                                null,
                            tint =
                                Red
                        )
                    }
                }

                Spacer(
                    Modifier.width(12.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            cost.description,

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold,

                        maxLines = 1,

                        overflow =
                            TextOverflow.Ellipsis
                    )
                }

                Text(
                    text =
                        formatEuro(
                            cost.amount
                        ) + " €",

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Red
                )
            }


            if (participants.isNotEmpty()) {

                Spacer(
                    Modifier.height(8.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Row(
                        modifier =
                            Modifier.weight(1f),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        participants.forEachIndexed {
                                index,
                                user ->

                            UserInitial(
                                user =
                                    user,

                                isPayer =
                                    user.id in payerIds
                            )

                            if (
                                index <
                                participants.lastIndex
                            ) {

                                Spacer(
                                    Modifier.width(5.dp)
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick =
                            onDelete,

                        modifier =
                            Modifier.size(48.dp)
                    ) {

                        Icon(
                            Icons.Filled.Delete,

                            contentDescription =
                                "Ausgabe löschen",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            modifier =
                                Modifier.size(28.dp)
                        )
                    }
                }

            } else {

                Spacer(
                    Modifier.height(8.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.End,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick =
                            onDelete
                    ) {

                        Icon(
                            Icons.Filled.Delete,

                            contentDescription =
                                "Ausgabe löschen",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                }
            }
        }
    }
}


// =================================================================
// USER INITIAL
// =================================================================

@Composable
private fun UserInitial(
    user: User,
    isPayer: Boolean
) {

    val initials =
        getInitials(
            user.name
        )

    Surface(
        modifier =
            Modifier.size(32.dp),

        shape =
            CircleShape,

        color =
            if (isPayer)
                Green
            else
                LightBlue
    ) {

        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    initials,

                style =
                    MaterialTheme
                        .typography
                        .labelSmall,

                fontWeight =
                    FontWeight.Bold,

                color =
                    if (isPayer)
                        Color.White
                    else
                        Blue
            )
        }
    }
}


// =================================================================
// ADD / EDIT DIALOG
// =================================================================

@Composable
private fun AddCostDialog(
    users: List<User>,
    existingCost: Costs?,
    existingSplit: com.example.wulwallet.ui.CostSplitData?,
    onDismiss: () -> Unit,
    onSave: (
        String,
        Float,
        List<CostPayer>,
        List<CostParticipant>
    ) -> Unit
) {

    var description by
    remember(existingCost) {

        mutableStateOf(
            existingCost?.description
                ?: ""
        )
    }

    var amountText by
    remember(existingCost) {

        mutableStateOf(
            existingCost?.amount?.let {
                formatEuro(it)
            } ?: ""
        )
    }


    val selectedPayers =
        remember(existingCost) {

            mutableStateMapOf<Int, Float>().apply {

                existingSplit
                    ?.payers
                    ?.forEach {

                        put(
                            it.userId,
                            it.share
                        )
                    }
            }
        }


    val selectedParticipants =
        remember(existingCost) {

            mutableStateMapOf<Int, Float>().apply {

                existingSplit
                    ?.participants
                    ?.forEach {

                        put(
                            it.userId,
                            it.share
                        )
                    }
            }
        }


    val amount =
        amountText
            .replace(',', '.')
            .toFloatOrNull()


    val participantTotal =
        selectedParticipants
            .values
            .sum()


    val payerTotal =
        selectedPayers
            .values
            .sum()


    val participantDifference =
        if (amount != null)
            amount - participantTotal
        else
            0f


    val payerDifference =
        if (amount != null)
            amount - payerTotal
        else
            0f


    val participantsValid =
        amount != null &&
                selectedParticipants.isNotEmpty() &&
                abs(
                    participantDifference
                ) < 0.01f


    val payersValid =
        amount != null &&
                selectedPayers.isNotEmpty() &&
                abs(
                    payerDifference
                ) < 0.01f


    val isEdit =
        existingCost != null


    val valid =
        description.isNotBlank() &&
                amount != null &&
                amount > 0f &&
                participantsValid &&
                payersValid


    Dialog(
        onDismissRequest =
            onDismiss,

        properties =
            DialogProperties(
                dismissOnClickOutside =
                    false,

                usePlatformDefaultWidth =
                    false
            )
    ) {

        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(
                        0.92f
                    ),

            shape =
                RoundedCornerShape(28.dp),

            color =
                MaterialTheme
                    .colorScheme
                    .surfaceVariant,

            tonalElevation =
                8.dp
        ) {

            Column(
                modifier =
                    Modifier.fillMaxSize()
            ) {

                // =================================================
                // HEADER
                // =================================================

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp,
                                end = 12.dp,
                                top = 18.dp,
                                bottom = 12.dp
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                if (isEdit)
                                    "Ausgabe bearbeiten"
                                else
                                    "Neue Ausgabe",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineSmall,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                if (isEdit)
                                    "Ändere Betrag oder Aufteilung."
                                else
                                    "Wer bezahlt und wer war dabei?",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick =
                            onDismiss
                    ) {

                        Icon(
                            Icons.Filled.Close,
                            contentDescription =
                                "Schließen"
                        )
                    }
                }


                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )


                // =================================================
                // CONTENT
                // =================================================

                LazyColumn(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),

                    contentPadding =
                        PaddingValues(
                            horizontal = 20.dp,
                            vertical = 18.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            12.dp
                        )
                ) {

                    // =================================================
                    // BESCHREIBUNG
                    // =================================================

                    item {

                        OutlinedTextField(
                            value =
                                description,

                            onValueChange = {
                                description = it
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {
                                Text(
                                    "Beschreibung"
                                )
                            },

                            singleLine =
                                true,

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                )
                        )
                    }


                    // =================================================
                    // BETRAG
                    // =================================================

                    item {

                        OutlinedTextField(
                            value =
                                amountText,

                            onValueChange = {

                                amountText =
                                    sanitizeMoneyInput(
                                        it
                                    )
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {
                                Text(
                                    "Betrag"
                                )
                            },

                            suffix = {
                                Text("€")
                            },

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Decimal
                                ),

                            singleLine =
                                true,

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                )
                        )
                    }


                    // =================================================
                    // TEILNEHMER
                    // =================================================

                    item {

                        Spacer(
                            Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Wer war dabei?",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Wähle die Personen aus.",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )

                        Spacer(
                            Modifier.height(10.dp)
                        )

                        UserSelectionRow(
                            users =
                                users,

                            selected =
                                selectedParticipants.keys,

                            onToggle = { user ->

                                if (
                                    selectedParticipants
                                        .containsKey(
                                            user.id
                                        )
                                ) {

                                    selectedParticipants
                                        .remove(
                                            user.id
                                        )

                                    redistributeAfterRemoval(
                                        map =
                                            selectedParticipants,

                                        amount =
                                            amount
                                    )

                                } else {

                                    addUserWithEqualSplit(
                                        map =
                                            selectedParticipants,

                                        userId =
                                            user.id,

                                        amount =
                                            amount
                                    )
                                }
                            }
                        )
                    }


                    // =================================================
                    // AUFTEILUNG
                    // =================================================

                    if (
                        selectedParticipants.isNotEmpty()
                    ) {

                        item {

                            SelectedUsersSection(
                                title =
                                    "Aufteilung",

                                users =
                                    users.filter {
                                        selectedParticipants
                                            .containsKey(
                                                it.id
                                            )
                                    },

                                values =
                                    selectedParticipants,

                                onChanged = {
                                        user,
                                        value ->

                                    updateEuroAmount(
                                        map =
                                            selectedParticipants,

                                        userId =
                                            user.id,

                                        requestedValue =
                                            value
                                    )
                                }
                            )

                            Spacer(
                                Modifier.height(6.dp)
                            )

                            EuroSplitInfo(
                                label =
                                    "Aufgeteilt",

                                total =
                                    participantTotal,

                                target =
                                    amount
                            )
                        }
                    }


                    // =================================================
                    // ZAHLER
                    // =================================================

                    item {

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Wer hat bezahlt?",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Wähle eine oder mehrere Personen.",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )

                        Spacer(
                            Modifier.height(10.dp)
                        )

                        UserSelectionRow(
                            users =
                                users,

                            selected =
                                selectedPayers.keys,

                            onToggle = { user ->

                                if (
                                    selectedPayers
                                        .containsKey(
                                            user.id
                                        )
                                ) {

                                    selectedPayers
                                        .remove(
                                            user.id
                                        )

                                    redistributeAfterRemoval(
                                        map =
                                            selectedPayers,

                                        amount =
                                            amount
                                    )

                                } else {

                                    addUserWithEqualSplit(
                                        map =
                                            selectedPayers,

                                        userId =
                                            user.id,

                                        amount =
                                            amount
                                    )
                                }
                            }
                        )
                    }


                    // =================================================
                    // ZAHLUNGSAUFTEILUNG
                    // =================================================

                    if (
                        selectedPayers.isNotEmpty()
                    ) {

                        item {

                            SelectedUsersSection(
                                title =
                                    "Zahlungsaufteilung",

                                users =
                                    users.filter {
                                        selectedPayers
                                            .containsKey(
                                                it.id
                                            )
                                    },

                                values =
                                    selectedPayers,

                                onChanged = {
                                        user,
                                        value ->

                                    updateEuroAmount(
                                        map =
                                            selectedPayers,

                                        userId =
                                            user.id,

                                        requestedValue =
                                            value
                                    )
                                }
                            )

                            Spacer(
                                Modifier.height(6.dp)
                            )

                            EuroSplitInfo(
                                label =
                                    "Bezahlt",

                                total =
                                    payerTotal,

                                target =
                                    amount
                            )
                        }
                    }
                }


                // =================================================
                // BOTTOM
                // =================================================

                Surface(
                    modifier =
                        Modifier.fillMaxWidth(),

                    tonalElevation =
                        6.dp,

                    color =
                        MaterialTheme
                            .colorScheme
                            .surfaceVariant
                ) {

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.End,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        TextButton(
                            onClick =
                                onDismiss
                        ) {

                            Text(
                                "Abbrechen"
                            )
                        }

                        Spacer(
                            Modifier.width(8.dp)
                        )

                        Button(
                            onClick = {

                                if (
                                    amount != null
                                ) {

                                    val payers =
                                        selectedPayers
                                            .map {

                                                CostPayer(
                                                    costId = 0,

                                                    userId =
                                                        it.key,

                                                    share =
                                                        it.value
                                                )
                                            }

                                    val participants =
                                        selectedParticipants
                                            .map {

                                                CostParticipant(
                                                    costId = 0,

                                                    userId =
                                                        it.key,

                                                    share =
                                                        it.value
                                                )
                                            }

                                    onSave(
                                        description.trim(),

                                        amount,

                                        payers,

                                        participants
                                    )
                                }
                            },

                            enabled =
                                valid,

                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            GreenDark
                                    ),

                            shape =
                                RoundedCornerShape(
                                    14.dp
                                )
                        ) {

                            Icon(
                                imageVector =
                                    if (isEdit)
                                        Icons.Filled.Save
                                    else
                                        Icons.Filled.Add,

                                contentDescription =
                                    null
                            )

                            Spacer(
                                Modifier.width(6.dp)
                            )

                            Text(
                                if (isEdit)
                                    "Speichern"
                                else
                                    "Hinzufügen"
                            )
                        }
                    }
                }
            }
        }
    }
}


// =================================================================
// USER SELECTION ROW
// =================================================================

@Composable
private fun UserSelectionRow(
    users: List<User>,
    selected: Set<Int>,
    onToggle: (User) -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        users.forEach { user ->

            val isSelected =
                user.id in selected

            Column(
                modifier =
                    Modifier
                        .width(58.dp)
                        .clickable {
                            onToggle(user)
                        },

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Surface(
                    modifier =
                        Modifier.size(46.dp),

                    shape =
                        CircleShape,

                    color =
                        if (isSelected)
                            Green
                        else
                            MaterialTheme
                                .colorScheme
                                .surfaceVariant
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text =
                                getInitials(
                                    user.name
                                ),

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                if (isSelected)
                                    Color.White
                                else
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                        )
                    }
                }

                Spacer(
                    Modifier.height(4.dp)
                )

                Text(
                    text =
                        if (user.isMainUser)
                            "Du"
                        else
                            user.name
                                .split(" ")
                                .firstOrNull()
                                ?: user.name,

                    style =
                        MaterialTheme
                            .typography
                            .labelSmall,

                    maxLines =
                        1,

                    overflow =
                        TextOverflow.Ellipsis
                )
            }
        }
    }
}


// =================================================================
// SELECTED USERS
// =================================================================

@Composable
private fun SelectedUsersSection(
    title: String,
    users: List<User>,
    values: Map<Int, Float>,
    onChanged: (User, Float) -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            text =
                title,

            style =
                MaterialTheme
                    .typography
                    .labelLarge,

            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            Modifier.height(6.dp)
        )

        users.forEach { user ->

            CompactEuroRow(
                user =
                    user,

                amount =
                    values[user.id]
                        ?: 0f,

                onChanged = {
                        value ->

                    onChanged(
                        user,
                        value
                    )
                }
            )
        }
    }
}


// =================================================================
// COMPACT EURO ROW
// =================================================================

@Composable
private fun CompactEuroRow(
    user: User,
    amount: Float,
    onChanged: (Float) -> Unit
) {

    var text by remember(
        user.id,
        amount
    ) {

        mutableStateOf(
            formatEuro(
                amount
            )
        )
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 3.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Surface(
            modifier =
                Modifier.size(34.dp),

            shape =
                CircleShape,

            color =
                if (user.isMainUser)
                    LightGreen
                else
                    LightBlue
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        getInitials(
                            user.name
                        ),

                    style =
                        MaterialTheme
                            .typography
                            .labelSmall,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        if (user.isMainUser)
                            GreenDark
                        else
                            Blue
                )
            }
        }

        Spacer(
            Modifier.width(10.dp)
        )

        Text(
            text =
                if (user.isMainUser)
                    "${user.name} (Du)"
                else
                    user.name,

            modifier =
                Modifier.weight(1f),

            style =
                MaterialTheme
                    .typography
                    .bodyMedium,

            maxLines =
                1,

            overflow =
                TextOverflow.Ellipsis
        )

        OutlinedTextField(
            value =
                text,

            onValueChange = { newText ->

                val clean =
                    sanitizeMoneyInput(
                        newText
                    )

                text =
                    clean

                val value =
                    clean
                        .replace(
                            ',',
                            '.'
                        )
                        .toFloatOrNull()

                if (value != null) {

                    onChanged(
                        value.coerceAtLeast(
                            0f
                        )
                    )
                }
            },

            modifier =
                Modifier.width(
                    110.dp
                ),

            suffix = {
                Text("€")
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Decimal
                ),

            singleLine =
                true,

            shape =
                RoundedCornerShape(
                    12.dp
                )
        )
    }
}


// =================================================================
// ADD USER WITH EQUAL SPLIT
// =================================================================

private fun addUserWithEqualSplit(
    map: MutableMap<Int, Float>,
    userId: Int,
    amount: Float?
) {

    map[userId] = 0f

    if (
        amount == null ||
        amount <= 0f
    ) {
        return
    }

    distributeEqual(
        map =
            map,

        amount =
            amount
    )
}


// =================================================================
// REDISTRIBUTE AFTER REMOVAL
// =================================================================

private fun redistributeAfterRemoval(
    map: MutableMap<Int, Float>,
    amount: Float?
) {

    if (
        amount == null ||
        amount <= 0f ||
        map.isEmpty()
    ) {
        return
    }

    distributeEqual(
        map =
            map,

        amount =
            amount
    )
}


// =================================================================
// EQUAL DISTRIBUTION
// =================================================================

private fun distributeEqual(
    map: MutableMap<Int, Float>,
    amount: Float
) {

    if (map.isEmpty()) {
        return
    }

    val count =
        map.size

    val base =
        (amount / count)
            .toBigDecimal()
            .setScale(
                2,
                java.math.RoundingMode.DOWN
            )
            .toFloat()

    val roundedBase =
        base * count

    val remainder =
        amount - roundedBase

    val ids =
        map.keys.toList()

    ids.forEachIndexed {
            index,
            id ->

        map[id] =
            if (
                index ==
                ids.lastIndex
            ) {

                base + remainder

            } else {

                base
            }
    }
}


// =================================================================
// UPDATE EURO AMOUNT
// =================================================================

private fun updateEuroAmount(
    map: MutableMap<Int, Float>,
    userId: Int,
    requestedValue: Float
) {

    map[userId] =
        requestedValue.coerceAtLeast(
            0f
        )
}


// =================================================================
// EURO SPLIT INFO
// =================================================================

@Composable
private fun EuroSplitInfo(
    label: String,
    total: Float,
    target: Float?
) {

    val difference =
        if (target != null)
            target - total
        else
            0f

    val valid =
        target != null &&
                abs(difference) < 0.01f

    val missing =
        difference > 0.009f

    val over =
        difference < -0.009f

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                12.dp
            ),

        color =
            when {

                valid ->
                    LightGreen

                over ->
                    LightRed

                else ->
                    LightOrange
            }
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(11.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        label,

                    fontWeight =
                        FontWeight.Medium
                )

                Text(
                    text =
                        formatEuro(
                            total
                        ) + " €",

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        when {

                            valid ->
                                GreenDark

                            over ->
                                Red

                            else ->
                                Orange
                        }
                )
            }

            if (
                !valid &&
                target != null
            ) {

                Spacer(
                    Modifier.height(3.dp)
                )

                Text(
                    text =
                        when {

                            missing ->
                                "Es fehlen ${
                                    formatEuro(
                                        abs(
                                            difference
                                        )
                                    )
                                } €."

                            over ->
                                "Es sind ${
                                    formatEuro(
                                        abs(
                                            difference
                                        )
                                    )
                                } € zu viel."

                            else ->
                                "Die Aufteilung stimmt nicht."
                        },

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        if (over)
                            Red
                        else
                            Orange
                )
            }
        }
    }
}


// =================================================================
// FORMAT EURO
// =================================================================

private fun formatEuro(
    value: Float
): String {

    return String.format(
        Locale.GERMANY,
        "%.2f",
        value
    )
}


// =================================================================
// SANITIZE MONEY INPUT
// =================================================================

private fun sanitizeMoneyInput(
    value: String
): String {

    var result =
        value
            .replace(
                ',',
                '.'
            )
            .filter {
                it.isDigit() ||
                        it == '.'
            }

    val firstDot =
        result.indexOf('.')

    if (firstDot >= 0) {

        val before =
            result.substring(
                0,
                firstDot + 1
            )

        val after =
            result.substring(
                firstDot + 1
            )
                .replace(
                    ".",
                    ""
                )

        result =
            before + after
    }

    return result
}


// =================================================================
// INITIALS
// =================================================================

private fun getInitials(
    name: String
): String {

    return name
        .trim()
        .split(" ")
        .filter {
            it.isNotBlank()
        }
        .take(2)
        .joinToString("") {
            it.first()
                .uppercase()
        }
        .take(2)
}