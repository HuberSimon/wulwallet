package com.example.wulwallet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wulwallet.data.local.User
import com.example.wulwallet.ui.CostSplitData
import com.example.wulwallet.ui.CostsViewModel
import com.example.wulwallet.ui.UserViewModel
import kotlin.math.abs

private val Green = Color(0xFF43A047)
private val Red = Color(0xFFE85D5D)
private val Blue = Color(0xFF1976D2)

data class BalanceResult(
    val user: User,
    val paid: Float,
    val share: Float
) {
    val balance: Float
        get() = paid - share
}

data class SettlementResult(
    val from: User,
    val to: User,
    val amount: Float
)

@Composable
fun SplitScreen(
    userViewModel: UserViewModel,
    costsViewModel: CostsViewModel
) {

    // ============================================================
    // USERS
    // ============================================================

    val users by
    userViewModel
        .allUsers
        .collectAsState()

    // ============================================================
    // COSTS + SPLITS
    // ============================================================

    val splitData by
    costsViewModel
        .splitData
        .collectAsState()

    var showStatistics by
    remember {
        mutableStateOf(false)
    }

    // ============================================================
    // DATEN LADEN
    // ============================================================

    LaunchedEffect(Unit) {
        costsViewModel.loadAllCosts()
    }

    // ============================================================
    // BILANZEN BERECHNEN
    // ============================================================

    val balances =
        remember(
            users,
            splitData
        ) {
            calculateBalances(
                users = users,
                splitData = splitData
            )
        }

    // ============================================================
    // ZAHLUNGEN BERECHNEN
    // ============================================================

    val settlements =
        remember(balances) {
            calculateSettlements(
                balances = balances
            )
        }

    // ============================================================
    // SCREEN
    // ============================================================

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        // ========================================================
        // HEADER
        // ========================================================

        item {

            Spacer(
                Modifier.height(18.dp)
            )

            Text(
                text = "Split",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                Modifier.height(18.dp)
            )

            // =====================================================
            // GESAMTE REISEKOSTEN
            // =====================================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(24.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Blue
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

                    Surface(
                        modifier =
                            Modifier.size(54.dp),

                        shape =
                            CircleShape,

                        color =
                            Color.White.copy(
                                alpha = .15f
                            )
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.AccountBalanceWallet,

                                contentDescription =
                                    null,

                                tint =
                                    Color.White
                            )
                        }
                    }

                    Spacer(
                        Modifier.width(14.dp)
                    )

                    Column {

                        Text(
                            text = "Reisebilanz",

                            color =
                                Color.White.copy(
                                    alpha = .8f
                                )
                        )

                        Text(
                            text =
                                String.format(
                                    "%.2f €",
                                    splitData.sumOf {
                                        it.cost.amount.toDouble()
                                    }
                                ),

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineMedium,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                Color.White
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(20.dp)
            )

            Text(
                text = "Persönliche Bilanz",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge,

                fontWeight =
                    FontWeight.Bold
            )
        }

        // ========================================================
        // BILANZEN
        // ========================================================

        items(
            items = balances,

            key = {
                it.user.id
            }
        ) { balance ->

            BalanceCard(
                balance = balance
            )
        }

        // ========================================================
        // SETTLEMENT BUTTON
        // ========================================================

        item {

            Spacer(
                Modifier.height(8.dp)
            )

            Button(
                onClick = {
                    showStatistics = true
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Green
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Calculate,

                    contentDescription =
                        null
                )

                Spacer(
                    Modifier.width(8.dp)
                )

                Text(
                    text =
                        "Wer muss wem was zahlen?"
                )
            }

            Spacer(
                Modifier.height(80.dp)
            )
        }
    }

    // ============================================================
    // SETTLEMENT DIALOG
    // ============================================================

    if (showStatistics) {

        SettlementDialog(
            settlements =
                settlements,

            onDismiss = {
                showStatistics = false
            }
        )
    }
}


// =================================================================
// BALANCES
// =================================================================

private fun calculateBalances(
    users: List<User>,
    splitData: List<CostSplitData>
): List<BalanceResult> {

    return users.map { user ->

        var paid = 0f
        var share = 0f

        splitData.forEach { split ->

            // ====================================================
            // WAS HAT DER USER BEZAHLT?
            // ====================================================
            //
            // share ist jetzt KEIN PROZENTWERT mehr.
            //
            // Beispiel:
            //
            // Rechnung: 100 €
            //
            // Max bezahlt: 70 €
            // Anna bezahlt: 30 €
            //
            // CostPayer:
            // Max  -> share = 70f
            // Anna -> share = 30f
            //
            // Daher KEINE Multiplikation mit cost.amount mehr.
            // ====================================================

            split.payers
                .filter {
                    it.userId == user.id
                }
                .forEach {

                    paid += it.share
                }

            // ====================================================
            // WAS IST DER EIGENE ANTEIL?
            // ====================================================
            //
            // Auch hier ist share bereits ein Euro-Betrag.
            //
            // Beispiel:
            //
            // Rechnung: 100 €
            //
            // Max Anteil: 40 €
            // Anna Anteil: 60 €
            //
            // CostParticipant:
            // Max  -> share = 40f
            // Anna -> share = 60f
            // ====================================================

            split.participants
                .filter {
                    it.userId == user.id
                }
                .forEach {

                    share += it.share
                }
        }

        BalanceResult(
            user = user,
            paid = paid,
            share = share
        )
    }
}


// =================================================================
// SETTLEMENTS
// =================================================================

private fun calculateSettlements(
    balances: List<BalanceResult>
): List<SettlementResult> {

    // =============================================================
    // GLÄUBIGER
    // =============================================================
    //
    // balance > 0:
    //
    // User hat mehr bezahlt als sein eigener Anteil.
    //
    // Er bekommt Geld zurück.
    // =============================================================

    val creditors =
        balances
            .filter {
                it.balance > 0.01f
            }
            .map {
                it.user to it.balance
            }
            .toMutableList()

    // =============================================================
    // SCHULDNER
    // =============================================================
    //
    // balance < 0:
    //
    // User hat weniger bezahlt als sein eigener Anteil.
    //
    // Er muss noch Geld zahlen.
    // =============================================================

    val debtors =
        balances
            .filter {
                it.balance < -0.01f
            }
            .map {
                it.user to -it.balance
            }
            .toMutableList()

    val settlements =
        mutableListOf<SettlementResult>()

    var creditorIndex = 0
    var debtorIndex = 0

    // =============================================================
    // ZAHLUNGEN VERTEILEN
    // =============================================================

    while (
        creditorIndex < creditors.size &&
        debtorIndex < debtors.size
    ) {

        val creditor =
            creditors[creditorIndex]

        val debtor =
            debtors[debtorIndex]

        val amount =
            minOf(
                creditor.second,
                debtor.second
            )

        if (amount > 0.01f) {

            settlements.add(
                SettlementResult(
                    from =
                        debtor.first,

                    to =
                        creditor.first,

                    amount =
                        amount
                )
            )
        }

        // ---------------------------------------------------------
        // Restbetrag Gläubiger
        // ---------------------------------------------------------

        creditors[creditorIndex] =
            creditor.first to
                    (
                            creditor.second -
                                    amount
                            )

        // ---------------------------------------------------------
        // Restbetrag Schuldner
        // ---------------------------------------------------------

        debtors[debtorIndex] =
            debtor.first to
                    (
                            debtor.second -
                                    amount
                            )

        // ---------------------------------------------------------
        // Gläubiger erledigt?
        // ---------------------------------------------------------

        if (
            creditors[creditorIndex]
                .second <= 0.01f
        ) {

            creditorIndex++
        }

        // ---------------------------------------------------------
        // Schuldner erledigt?
        // ---------------------------------------------------------

        if (
            debtors[debtorIndex]
                .second <= 0.01f
        ) {

            debtorIndex++
        }
    }

    return settlements
}


// =================================================================
// BALANCE CARD
// =================================================================

@Composable
private fun BalanceCard(
    balance: BalanceResult
) {

    val positive =
        balance.balance >= 0f

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =====================================================
            // ICON
            // =====================================================

            Surface(
                modifier =
                    Modifier.size(50.dp),

                shape =
                    CircleShape,

                color =
                    if (positive)
                        Color(0xFFE8F5E9)
                    else
                        Color(0xFFFDECEC)
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            if (positive)
                                Icons.Filled.ArrowUpward
                            else
                                Icons.Filled.ArrowDownward,

                        contentDescription =
                            null,

                        tint =
                            if (positive)
                                Green
                            else
                                Red
                    )
                }
            }

            Spacer(
                Modifier.width(14.dp)
            )

            // =====================================================
            // USER INFORMATION
            // =====================================================

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        balance.user.name,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        if (positive)
                            "bekommt Geld zurück"
                        else
                            "muss noch zahlen",

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
                    Modifier.height(3.dp)
                )

                Text(
                    text =
                        "Bezahlt: ${
                            String.format(
                                "%.2f €",
                                balance.paid
                            )
                        }  •  Anteil: ${
                            String.format(
                                "%.2f €",
                                balance.share
                            )
                        }",

                    style =
                        MaterialTheme
                            .typography
                            .labelSmall
                )
            }

            // =====================================================
            // BALANCE
            // =====================================================

            Text(
                text =
                    String.format(
                        "%s%.2f €",

                        if (positive)
                            "+"
                        else
                            "-",

                        abs(
                            balance.balance
                                .toDouble()
                        )
                    ),

                fontWeight =
                    FontWeight.Bold,

                color =
                    if (positive)
                        Green
                    else
                        Red
            )
        }
    }
}


// =================================================================
// SETTLEMENT DIALOG
// =================================================================

@Composable
private fun SettlementDialog(
    settlements:
    List<SettlementResult>,

    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Zahlungsstatistik",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            if (settlements.isEmpty()) {

                Text(
                    text =
                        "🎉 Alles ausgeglichen!\n\n" +
                                "Niemand muss jemand anderem noch etwas zahlen."
                )

            } else {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    settlements.forEach { settlement ->

                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(16.dp),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .surfaceVariant
                                )
                        ) {

                            Row(
                                modifier =
                                    Modifier.padding(14.dp),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier =
                                        Modifier.weight(1f)
                                ) {

                                    Text(
                                        text =
                                            settlement
                                                .from
                                                .name,

                                        fontWeight =
                                            FontWeight.Bold
                                    )

                                    Text(
                                        text =
                                            "zahlt an"
                                    )

                                    Text(
                                        text =
                                            settlement
                                                .to
                                                .name,

                                        fontWeight =
                                            FontWeight.Bold,

                                        color =
                                            Green
                                    )
                                }

                                Text(
                                    text =
                                        String.format(
                                            "%.2f €",
                                            settlement.amount
                                        ),

                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleMedium,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        Red
                                )
                            }
                        }
                    }
                }
            }
        },

        confirmButton = {

            TextButton(
                onClick =
                    onDismiss
            ) {

                Text(
                    text =
                        "Schließen"
                )
            }
        }
    )
}
