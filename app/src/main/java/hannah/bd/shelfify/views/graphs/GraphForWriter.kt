package hannah.bd.shelfify.views.graphs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import hannah.bd.shelfify.R
import hannah.bd.shelfify.modals.AppDatabase
import hannah.bd.shelfify.modals.Stat
import hannah.bd.shelfify.modals.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphForWriter(
    navigateBack: () -> Unit,
    db: AppDatabase?,
    userPreferences: UserPreferences,
) {
    var stats by remember { mutableStateOf(listOf<Stat>()) }
    val wordsWritten by userPreferences
        .wordsWritten
        .collectAsState(initial = 0)

    LaunchedEffect(Unit) {

        db?.let {
            stats = db.statDao().getAll()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "️📊 See your writing progress 📊",
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
                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
                        Text("Library completes at 100k words.",
                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)),)
                    }
                }
                item {
                    if (stats.size < 2) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Not enough statistics to show yet! Head over to grow your writing and get those words written.",
                                fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
                        }
                    } else {
                        val totalWords = stats.sumOf { it.wordsWritten }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "You've partaken in ${stats.size} writing sprints.",
                                fontFamily = FontFamily(Font(R.font.bellefairregularfont))
                            )

                            Box(Modifier.fillMaxSize()) {
                                BarChart(
                                    chartParameters = listOf(
                                        BarParameters(
                                            dataName = "Words Written",
                                            data = stats.map { it.wordsWritten.toDouble() },
                                            barColor = MaterialTheme.colorScheme.primary
                                        ),
                                    ),
                                    gridColor = Color.DarkGray,
                                    xAxisData = stats.map { it.date.toString() },
                                    isShowGrid = true,
                                    animateChart = true,
                                    showGridWithSpacer = true,
                                    yAxisStyle = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.DarkGray,
                                    ),
                                    xAxisStyle = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.DarkGray,
                                        fontWeight = FontWeight.W400
                                    ),
                                    yAxisRange = 15,
                                    barWidth = 20.dp
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Box(Modifier.fillMaxSize()) {
                                LineChart(
                                    modifier = Modifier.fillMaxSize()
                                        .defaultMinSize(minHeight = 200.dp),
                                    linesParameters = listOf(
                                        LineParameters(
                                            label = "Words Written",
                                            data = stats.map { it.wordsWritten.toDouble() },
                                            lineColor = MaterialTheme.colorScheme.primary,
                                            lineType = LineType.CURVED_LINE,
                                            lineShadow = true,
                                        ),
                                    ),
                                    isGrid = true,
                                    gridColor = Color.Blue,
                                    xAxisData = stats.map { it.date.toString() },
                                    animateChart = true,
                                    showGridWithSpacer = true,
                                    yAxisStyle = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                    ),
                                    xAxisStyle = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.W400
                                    ),
                                    yAxisRange = 14,
                                    oneLineChart = false,
                                    gridOrientation = GridOrientation.VERTICAL
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
