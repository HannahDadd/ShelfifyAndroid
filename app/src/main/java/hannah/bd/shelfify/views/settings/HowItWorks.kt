package hannah.bd.shelfify.views.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hannah.bd.shelfify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowItWorks(
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "How Shelfify Works",
                        fontFamily = FontFamily(Font(hannah.bd.shelfify.R.font.dynapuff)),
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
                        Text("""
Property is hard to come by in this modern era. Luckily, you've stumbled across an abandoned library and all that's missing are the books! How do you add books? Simple. You write. Grow your library by completing writing sprints and adding words to your writing projects and books magically start filling the shelves.

A few ghosts may visit your library- don't worry, they're friendly. Ish. I can't wait to see what you do with the place.

                        """.trimIndent(),
                            fontFamily = FontFamily(Font(hannah.bd.shelfify.R.font.bellefairregularfont)),
                        )
                    }
                }
            }
        }
    }
}