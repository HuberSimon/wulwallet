import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wulidee.data.local.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CustomTopAppBarWithTabs(
    user: User,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    navController: NavController
) {
    var titles = listOf("Lieblingsmenschen","Benutzer")
    if (user?.reminderEnabled == true){
        titles = listOf("Lieblingsmenschen", "Erinnerung", "Benutzer")
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                titles.forEachIndexed { index, title ->
                    if (title != "Benutzer") {
                        Text(
                            text = title,
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        navController.navigate("horizontal_pager")
                                        pagerState.scrollToPage(index)
                                    }
                                }
                                .padding(end = 16.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        navController.navigate("horizontal_pager")
                        pagerState.scrollToPage(titles.indexOf("Benutzer"))
                    }
                }
            ) {
                CircleWithLetter(letter = user.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?")
            }
        }

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Composable
fun CircleWithLetter(letter: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = letter,
            color = Color.White,
        )
    }
}