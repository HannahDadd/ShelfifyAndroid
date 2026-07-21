package hannah.bd.shelfify.views.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hannah.bd.shelfify.R
import hannah.bd.shelfify.modals.AppDatabase
import hannah.bd.shelfify.modals.Stat
import hannah.bd.shelfify.views.settings.settingsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuView(
    growAction: () -> Unit,
    statsAction: () -> Unit,
    db: AppDatabase?,
    navController: NavController
) {
    var stats by remember { mutableStateOf(listOf<Stat>()) }

    var showButtons by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    val buttonSize = 75.dp

    LaunchedEffect(Unit) {

        db?.let {
            stats = db.statDao().getAll()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (showButtons) {

                Image(
                    painter = painterResource(R.drawable.grow_btn),
                    contentDescription = "Grow your library",
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            growAction()
                        }
                )

                if (stats.size > 2) {
                    Image(
                        painter = painterResource(R.drawable.graph_btn),
                        contentDescription = "Statistics",
                        modifier = Modifier
                            .size(buttonSize)
                            .clickable {
                                statsAction()
                            }
                    )
                }

                Image(
                    painter = painterResource(R.drawable.broom_btn),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            showSettings = true
                        }
                )
            }

            Row {

                Image(
                    painter = painterResource(R.drawable.menu_btn),
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            showButtons = !showButtons
                        }
                )

                Spacer(modifier = Modifier.weight(1.0f))

                Image(
                    painter = painterResource(R.drawable.crystal_btn),
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                        }
                )

            }
        }
    }

    if (showSettings) {

        ModalBottomSheet(
            onDismissRequest = {
                showSettings = false
            }
        ) {
            settingsView(navController)
        }
    }
}