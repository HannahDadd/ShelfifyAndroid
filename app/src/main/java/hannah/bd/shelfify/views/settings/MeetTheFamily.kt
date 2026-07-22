package hannah.bd.shelfify.views.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import hannah.bd.shelfify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetTheFamily(
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "📚 Meet the Family 📚",
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
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.family_photo),
                            contentDescription = null,
                            modifier = Modifier.size(500.dp)
                        )
                    }
                }
                item {
                    Column {
                        Text(
                            "Gilly",
                            fontFamily = FontFamily(Font(R.font.dynapuff))
                        )
                        Text("""
                            Get it Write will gamify your creative writing and get those words written.
                        """.trimIndent(),
                                fontFamily = FontFamily(Font(hannah.bd.shelfify.R.font.bellefairregularfont)),
                        )
                    }
                }
            }
        }
    }
}