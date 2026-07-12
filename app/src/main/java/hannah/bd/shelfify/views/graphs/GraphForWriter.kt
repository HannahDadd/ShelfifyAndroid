package hannah.bd.shelfify.views.graphs

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.core.entry.FloatEntry
import com.patrykandpatryk.vico.core.entry.entryModelOf
import hannah.bd.shelfify.R
import hannah.bd.shelfify.modals.AppDatabase
import hannah.bd.shelfify.modals.Stat
import hannah.bd.shelfify.modals.UserPreferences
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
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
                        "See your writing progress",
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                item {
                    Text("Words written in the app: ${ wordsWritten }",
                        fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
                    Text(
                        "Library completes at 100k words.",
                        fontFamily = FontFamily(Font(R.font.bellefairregularfont)),
                    )
                }
                item {
                    Text(
                        "You've finished ${stats.size} writing sprints.",
                        fontFamily = FontFamily(Font(R.font.bellefairregularfont))
                    )
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {

                        val entryModel = entryModelOf(stats.mapIndexed { index, stat ->
                            FloatEntry(index.toFloat(), stat.wordsWritten.toFloat())
                        })
                        Text("Words written across all your sprints",
                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
                        Chart(
                            chart = lineChart(),
                            model = entryModel,
                            startAxis = startAxis(),
                            bottomAxis = bottomAxis(),
                        )
                    }
                }
//                item {
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        val cal: Calendar = Calendar.getInstance()
//                        cal.add(Calendar.MONTH, -1)
//                        val result: Date = cal.getTime()
//
//                        val statsLastMonth = stats
//                            .filter { it.date.after(cal.getTime()) }
//
//                        val bestStat = statsLastMonth.maxBy { it.wordsWritten }
//
//                        val entryModel = entryModelOf(statsLastMonth
//                            .mapIndexed { index, stat ->
//                            FloatEntry(index.toFloat(), stat.wordsWritten.toFloat())
//                        })
//                        Text("Sprints finished in the last month: ${ statsLastMonth.size }",
//                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
//                        Chart(
//                            chart = columnChart(),
//                            model = entryModel,
//                            startAxis = startAxis(),
//                            bottomAxis = bottomAxis(),
//                        )
//                        Text("Your best sprint in the last month was on ${ bestStat.date.toString() }! You wrote ${bestStat.wordsWritten}. Congrats!",
//                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
//                    }
//                }
//                item {
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        val cal: Calendar = Calendar.getInstance()
//                        cal.add(Calendar.YEAR, -1)
//                        val result: Date = cal.getTime()
//
//                        val statsLastYear = stats
//                            .filter { it.date.after(cal.getTime()) }
//
//                        val bestStat = statsLastYear.maxBy { it.wordsWritten }
//
//                        val entryModel = entryModelOf(statsLastYear
//                            .mapIndexed { index, stat ->
//                                FloatEntry(index.toFloat(), stat.wordsWritten.toFloat())
//                            })
//                        Text("Sprints finished in the last month: ${ statsLastYear.size }",
//                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
//                        Chart(
//                            chart = lineChart(),
//                            model = entryModel,
//                            startAxis = startAxis(),
//                            bottomAxis = bottomAxis(),
//                        )
//                        Text("Your best sprint in the last year was on ${ bestStat.date.toString() }! You wrote ${bestStat.wordsWritten}.",
//                            fontFamily = FontFamily(Font(R.font.bellefairregularfont)))
//                    }
//                }
            }
        }
    }
}
