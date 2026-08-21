package com.example.wulwallet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wulwallet.data.local.User
import com.example.wulwallet.ui.UserViewModel

@Composable
fun UserScreen(userViewModel: UserViewModel) {
    val user by userViewModel.user.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "${user?.name}'s Einstellungen",
            modifier = Modifier
                .padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        Divider(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "User ändern",
            modifier = Modifier
                .clickable {
                    if (user != null && user!!.name.isNotEmpty()) {
                        userViewModel.updateUser(
                            User(
                                id = user!!.id,
                                name = ""
                            )
                        )
                    }
                }
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

    }

}

