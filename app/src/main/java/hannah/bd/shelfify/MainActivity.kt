package hannah.bd.shelfify

import android.Manifest
import android.R.attr.name
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.hannah.shelfify.views.ghosts.ghostView
import hannah.bd.shelfify.modals.UserPreferences
import hannah.bd.shelfify.modals.onboardingPages
import hannah.bd.shelfify.views.homepage.LibraryView
import hannah.bd.shelfify.views.homepage.MenuView
import hannah.bd.shelfify.views.homepage.NavigationStack
import hannah.bd.shelfify.views.homepage.Screen
import hannah.bd.shelfify.views.homepage.backGroundView
import hannah.bd.shelfify.views.homepage.growYourLibraryHomepage
import hannah.bd.shelfify.views.notifications.NotificationHelper
import hannah.bd.shelfify.views.onboarding.OnboardingOverlay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
//    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NotificationHelper.createChannel(this)
        super.onCreate(savedInstanceState?: Bundle())
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationStack(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppMainPage(navController: NavController, userPreferences: UserPreferences) {

    val scope = rememberCoroutineScope()
    val hasSeenOnboarding by userPreferences
        .hasSeenOnboarding
        .collectAsState(initial = false)

    val wordsWritten by userPreferences
        .wordsWritten
        .collectAsState(initial = 0)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        backGroundView()
        LibraryView(wordsWritten)
        MenuView({ navController.navigate(Screen.Grow.route)}, navController = navController)
        ghostView()

        if (!hasSeenOnboarding) {
            OnboardingOverlay(
                pages = onboardingPages,
                onFinished = {
                    scope.launch {
                        userPreferences.completeOnboarding()
                    }
                }
            )
        }
    }
}

@Composable
fun NotificationPermissionRequest() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        val launcher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                // Optional: handle result
            }

        LaunchedEffect(Unit) {
            launcher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }
}