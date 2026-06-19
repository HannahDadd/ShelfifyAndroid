package hannah.bd.getitwrite.views.sprints

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hannah.bd.shelfify.R
import java.util.Date
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("NewApi")
@Composable
fun SprintStack(db: AppDatabase?, onFinish: () -> Unit, initialMinute: Int) {
    var sprintState by remember { mutableStateOf(SprintState.START) }
    var selectedWip by remember { mutableStateOf<WIP?>(db?.wipDao()?.getAll()?.get(0)) }
    var startWordCount by remember { mutableStateOf(0) }
    var endWordCount by remember { mutableStateOf(0) }

    when (sprintState) {

        SprintState.SPRINT -> {
            Sprint(
                initialTime = initialMinute,
                onEnd = { sprintState = SprintState.END }
            )
        }

        SprintState.END -> {
            Column(modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sprint Finished!",
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.abrilfatfaceregular)))
                Text("Start word count: $startWordCount words", fontWeight = FontWeight.Bold)
                NumberInput(label = "End Word Count", value = endWordCount) {
                    endWordCount = it
                }

                Spacer(Modifier.weight(1f))
                Button(onClick = onFinish) {
                    Text("Back To Home Page")
                }
            }
        }
    }
}

enum class SprintState(val label: String) {
    SPRINT("Sprint"),
    END("End")
}
