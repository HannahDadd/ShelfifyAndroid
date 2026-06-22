package hannah.bd.shelfify.views.homepage

import android.R.attr.type
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hannah.bd.getitwrite.views.sprints.SprintStack
import hannah.bd.shelfify.AppMainPage
import hannah.bd.shelfify.modals.UserPreferences

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Grow: Screen("grow_screen")
}

@Composable
fun NavigationStack(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            AppMainPage(navController = navController, UserPreferences(LocalContext.current))
        }
        composable(
            route = Screen.Grow.route
        ) {
            growYourLibraryHomepage(navController, { navController.popBackStack() })
        }
        composable("sprint20") {
            SprintStack(
                onFinish = { navController.popBackStack() }, 20)
        }
        composable("sprint40") {
            SprintStack(onFinish = { navController.popBackStack() }, 40)
        }
        composable("sprint60") {
            SprintStack(onFinish = { navController.popBackStack() }, 60)
        }
    }
}
