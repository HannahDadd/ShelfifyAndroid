package hannah.bd.shelfify.views.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hannah.bd.getitwrite.views.sprints.SprintCarousel
import hannah.bd.getitwrite.views.sprints.SprintDurations
import hannah.bd.shelfify.R
import hannah.bd.shelfify.modals.UserPreferences
import hannah.bd.shelfify.views.notifications.DailyReminderButton
import hannah.bd.shelfify.views.sprints.LiveUpdateCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun growYourLibraryHomepage(
    navController: NavController,
    navigateBack: () -> Unit,
    userPreferences: UserPreferences,
    hasPermission: Boolean,
    canPostPromoted: Boolean,
    onOpenSettings: () -> Unit,
    onStartFoodDelivery: () -> Unit,
) {
    val wordsWritten by userPreferences
        .wordsWritten
        .collectAsState(initial = 0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🪴 Grow your Library 🪴",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Words written in the app: ${ wordsWritten }",
                                fontFamily = FontFamily(Font(R.font.bellefairregularfont)),)
                    }
                }
                item {
                    SprintCarousel { sprintDurations ->
                        when (sprintDurations) {
                            SprintDurations.TWENTY_MINS -> navController.navigate("sprint20")
                            SprintDurations.FORTY_MINS -> navController.navigate("sprint40")
                            SprintDurations.ONE_HOUR -> navController.navigate("sprint60")
                        }
                    }
                }
                item {
                    LiveUpdateCard(
                        title = "🍕 Food Delivery Tracking",
                        description = "Track your order with ProgressStyle milestones (Android 16+)",
                        icon = Icons.Default.ShoppingCart,
                        onClick = onStartFoodDelivery,
                        enabled = hasPermission,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        isNew = true
                    )
                }
                item {
                    Column(modifier = Modifier.padding(8.dp)) {
                        DailyReminderButton()
                    }
                }
            }
        }
    }
}