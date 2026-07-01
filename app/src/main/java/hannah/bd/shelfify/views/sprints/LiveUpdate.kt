//package hannah.bd.shelfify.views.sprints
//
//import android.app.Activity
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.NotificationManager.IMPORTANCE_DEFAULT
//import android.app.Notification.ProgressStyle.Point
//import android.app.Notification.ProgressStyle.Segment
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.os.Build
//import android.provider.Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT
//import androidx.annotation.RequiresApi
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableLongStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.core.graphics.drawable.IconCompat
//import androidx.core.net.toUri
//import hannah.bd.shelfify.R
//import kotlinx.coroutines.delay
//
//object LiveUpdatesNotificationManager {
//    private lateinit var notificationManager: NotificationManager
//    private lateinit var appContext: Context
//    const val CHANNEL_ID = "live_updates_16_channel_id"
//    private const val CHANNEL_NAME = "live_updates_16_channel_name"
//    private const val NOTIFICATION_ID = 4321
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun initialize(context: Context, notifManager: NotificationManager) {
//        notificationManager = notifManager
//        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
//        appContext = context
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
//    fun checkInitialization(context: Context) {
//        val canPostLiveUpdates = notificationManager.canPostPromotedNotifications()
//        if (!canPostLiveUpdates) {
//            val intent = Intent(ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).setData(
//                "package:${appContext.packageName}".toUri(),
//            )
//            (context as Activity).startActivityForResult(intent, 0)
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun buildBaseNotification(
//        appContext: Context,
//    ): Notification.Builder {
//        return Notification.Builder(appContext, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setOngoing(true)
//            .setColorized(true)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
//    fun buildBaseProgressStyle(orderState: OrderState): Notification.ProgressStyle {
//        val progressStyle = Notification.ProgressStyle()
//            .setProgressPoints(
//                listOf(
//                    Point(25).setColor(Color.MAGENTA),
//                    Point(50).setColor(Color.RED),
//                    Point(75).setColor(Color.LTGRAY),
//                    Point(100).setColor(Color.GRAY)
//                )
//            ).setProgressSegments(
//                listOf(
//                    Segment(25).setColor(Color.MAGENTA),
//                    Segment(25).setColor(Color.RED),
//                    Segment(25).setColor(Color.LTGRAY),
//                    Segment(25).setColor(Color.GRAY)
//
//                )
//            )
//        when (orderState) {
//
//            OrderState.Writing -> {
//                progressStyle.setProgressPoints(
//                    listOf(
//                        Point(25).setColor(Color.MAGENTA),
//                        Point(50).setColor(Color.RED),
//                    ),
//                )
//            }
//
//            OrderState.Finished -> {
//                progressStyle.setProgressPoints(
//                    listOf(
//                        Point(25).setColor(Color.MAGENTA),
//                        Point(50).setColor(Color.RED),
//                        Point(75).setColor(Color.LTGRAY),
//                    ),
//                )
//            }
//
//
//        }
//        return progressStyle
//    }
//
//    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
//    fun updateNotificationStatus(orderState: OrderState) {
//        val notificationBuilder = when (orderState) {
//
//            OrderState.Writing -> {
//                buildBaseNotification(appContext)
//                    .setContentTitle("Your order is on its way")
//                    .setContentText("Enroute to destination")
//                    .setStyle(
//                        buildBaseProgressStyle(orderState)
//                            .setProgressTrackerIcon(
//                                IconCompat.createWithResource(
//                                    appContext, R.drawable.sid,
//                                ).toIcon(appContext),
//                            )
//                            .setProgress(50),
//                    )
//                    .setLargeIcon(
//                        IconCompat.createWithResource(
//                            appContext,
//                            R.drawable.sid,
//                        )
//                            .toIcon(appContext),
//                    )
//            }
//
//            OrderState.Finished -> {
//                buildBaseNotification(appContext)
//                    .setContentTitle("Your order is complete.")
//                    .setContentText("Thank you for using our app.")
//                    .setStyle(
//                        buildBaseProgressStyle(orderState)
//                            .setProgressTrackerIcon(
//                                IconCompat.createWithResource(
//                                    appContext, R.drawable.sid,
//                                ).toIcon(appContext),
//                            )
//                            .setProgress(100),
//                    )
//                    .setLargeIcon(
//                        IconCompat.createWithResource(
//                            appContext, R.drawable.sid,
//                        ).toIcon(appContext),
//                    )
//
//            }
//        }
//        val notification = notificationBuilder.build()
//        val isPromotable = notification.hasPromotableCharacteristics()
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }
//
//    private const val TAG = "LiveUpdatesNotificationManager"
//}
//
////sealed class OrderState {
////    data object Writing : OrderState()
////    data object Finished : OrderState()
////}
//
//@Composable
//fun CountdownText(
//    endTime: Long
//) {
//    var remaining by remember {
//        mutableLongStateOf(
//            remainingMillis(endTime)
//        )
//    }
//
//    LaunchedEffect(endTime) {
//        while (remaining > 0) {
//            remaining = remainingMillis(endTime)
//            delay(1000)
//        }
//    }
//
//    Text(formatTime(remaining))
//}
//
//fun remainingMillis(endTime: Long): Long {
//    return (endTime - System.currentTimeMillis())
//        .coerceAtLeast(0)
//}
//
//fun formatTime(ms: Long): String {
//    val totalSeconds = ms / 1000
//
//    val minutes = totalSeconds / 60
//    val seconds = totalSeconds % 60
//
//    return "%02d:%02d".format(minutes, seconds)
//}