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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wulwallet.data.local.User
import com.example.wulwallet.ui.UserViewModel

private val SettingsGreen = Color(0xFF4CAF50)
private val SettingsGreenDark = Color(0xFF388E3C)
private val SettingsLightGray = Color(0xFFF3F4F6)
private val SettingsTextGray = Color(0xFF6B7280)

@Composable
fun UserScreen(
    userViewModel: UserViewModel
) {
    val user by userViewModel.user.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Einstellungen",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Verwalte deine persönlichen Einstellungen.",
            style = MaterialTheme.typography.bodyMedium,
            color = SettingsTextGray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Profilkarte
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = SettingsLightGray
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .then(
                            Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    ) {
                        // Hintergrund der Profilfläche
                    }

                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = SettingsGreenDark,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Dein Profil",
                        style = MaterialTheme.typography.labelMedium,
                        color = SettingsTextGray
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = user?.name?.takeIf {
                            it.isNotBlank()
                        } ?: "Kein Name festgelegt",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Konto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // User ändern
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (user != null && user!!.name.isNotEmpty()) {
                        userViewModel.updateUser(
                            User(
                                id = user!!.id,
                                name = ""
                            )
                        )
                    }
                },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(13.dp)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = SettingsGreenDark,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "User ändern",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Deinen Namen zurücksetzen",
                        style = MaterialTheme.typography.bodySmall,
                        color = SettingsTextGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info
        Text(
            text = "Deine persönlichen Daten werden lokal in der App gespeichert.",
            style = MaterialTheme.typography.bodySmall,
            color = SettingsTextGray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}