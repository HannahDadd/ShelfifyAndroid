package hannah.bd.shelfify.views.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    speed: Long = 30L
) {
    var visibleText by remember(text) { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""

        text.forEachIndexed { index, _ ->
            visibleText = text.take(index + 1)
            delay(speed)
        }
    }

    Text(
        text = visibleText,
        modifier = modifier
    )
}