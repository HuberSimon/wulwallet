// ================================================================
// FILE: ui/CustomTopAppBarWithTabs.kt
// ================================================================

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.google.accompanist.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wulwallet.data.local.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CustomTopAppBarWithTabs(
    user: User,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    navController: NavController
) {

    val titles =
        listOf(
            "Cashflow",
            "Split",
            "Reisebuddy",
            "ToDo"
        )

    Column {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                Modifier.weight(1f),
                horizontalArrangement =
                    Arrangement.spacedBy(2.dp)
            ) {

                titles.forEachIndexed {
                        index,
                        title ->

                    Text(
                        text = title,

                        modifier =
                            Modifier
                                .clip(
                                    RoundedCornerShape(
                                        12.dp
                                    )
                                )
                                .clickable {

                                    coroutineScope
                                        .launch {

                                            navController
                                                .navigate(
                                                    "horizontal_pager"
                                                )

                                            pagerState
                                                .animateScrollToPage(
                                                    index
                                                )
                                        }
                                }
                                .padding(
                                    horizontal = 9.dp,
                                    vertical = 8.dp
                                ),

                        style =
                            MaterialTheme
                                .typography
                                .labelLarge.copy(
                                    fontWeight =
                                        if (
                                            pagerState
                                                .currentPage ==
                                            index
                                        )
                                            FontWeight.Bold
                                        else
                                            FontWeight.Normal
                                ),

                        color =
                            if (
                                pagerState
                                    .currentPage ==
                                index
                            )
                                MaterialTheme
                                    .colorScheme
                                    .primary
                            else
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                    )
                }
            }

            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme
                            .colorScheme
                            .primary
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        user.name
                            .firstOrNull()
                            ?.uppercase()
                            ?: "?",
                    color =
                        Color.White,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
    }
