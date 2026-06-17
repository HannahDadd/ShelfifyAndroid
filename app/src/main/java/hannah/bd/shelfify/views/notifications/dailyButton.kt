package hannah.bd.shelfify.views.notifications

import android.app.TimePickerDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun DailyReminderButton() {

    val context = LocalContext.current

    Button(
        onClick = {

            val now = Calendar.getInstance()

            TimePickerDialog(
                context,
                { _, hour, minute ->

                    DailyNotificationScheduler.scheduleDailyNotification(
                        context = context,
                        hour = hour,
                        minute = minute
                    )

                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()

        }
    ) {
        Text("Set Daily Writing Reminder")
    }
}