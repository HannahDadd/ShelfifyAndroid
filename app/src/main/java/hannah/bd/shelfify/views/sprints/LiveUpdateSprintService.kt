package hannah.bd.shelfify.views.sprints

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission

class LiveUpdateSprintService : Service() {

    private lateinit var notificationManager: LiveUpdateNotificationManager
    private lateinit var deliveryHandler: WritingSprintServiceHandler
    private var isRunning = false

    companion object {
        private const val TAG = "WritingSprintService"
        const val ACTION_START = "hannah.bd.shelfify.livenotificationupdate.writingsprint.START"
        const val ACTION_STOP = "hannah.bd.shelfify.livenotoficationupdate.writingsprint.STOP"
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize notification manager
        notificationManager = LiveUpdateNotificationManager(this)

        // Initialize delivery handler with completion callback
        deliveryHandler = WritingSprintServiceHandler(
            service = this,
            notificationManager = notificationManager,
            onComplete = {
                stopSprint()
            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action}")

        when (intent?.action) {
            ACTION_START -> startDelivery()
            ACTION_STOP -> stopSprint()
            else -> Log.w(TAG, "Unknown action: ${intent?.action}")
        }

        return START_NOT_STICKY  // Don't restart if killed
    }

    /**
     * Start food delivery tracking
     */
    private fun startDelivery() {
        if (isRunning) {
            return
        }

        // Check if Live Updates are enabled (for debugging status chips)
        val canPostPromoted = notificationManager.canPostPromotedNotifications()
        Log.d(TAG, "Can post promoted notifications: $canPostPromoted")

        isRunning = true

        // Get initial notification from handler
        val notification = deliveryHandler.start()

        // Check if notification has promotable characteristics (Android 16+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            val hasPromotable = notification.hasPromotableCharacteristics()
            Log.d(TAG, "Initial notification hasPromotableCharacteristics: $hasPromotable")
        }

        // Start as foreground service (REQUIRED for continuous tracking)
        startForeground(
            LiveUpdateNotificationManager.NOTIFICATION_ID_WRITING_SPRINT,
            notification
        )

    }

    private fun stopSprint() {
        if (!isRunning) {
            return
        }

        isRunning = false

        // Stop the handler
        deliveryHandler.stop()

        // Stop foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }

        // Stop the service
        stopSelf()

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()

        // Ensure handler is stopped
        if (isRunning) {
            deliveryHandler.stop()
        }
    }
}

/**
 * Service handler for food delivery tracking
 * Manages the lifecycle of food delivery Live Update notifications
 *
 * This class encapsulates all food delivery service logic, making it
 * fully modular and reusable in other projects.
 */
class WritingSprintServiceHandler(
    private val service: Service,
    private val notificationManager: LiveUpdateNotificationManager,
    private val onComplete: () -> Unit = {}  // Callback when delivery completes
) {
    private var foodDeliveryTracker: FoodDeliveryTracker? = null
    private var isActive = false
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val TAG = "FoodDeliveryHandler"
        private const val COMPLETION_DISPLAY_DURATION = 3000L // Show completion for 3 seconds
    }

    /**
     * Start food delivery tracking
     * @return Initial notification to start foreground service with
     */
    fun start(): Notification {
        Log.d(TAG, "Starting food delivery tracking")
        isActive = true

        // Initialize the tracker with callbacks
        foodDeliveryTracker = FoodDeliveryTracker(
            onStateChanged = { state -> updateNotification(state) },
            onCompleted = { complete() }
        )

        // Create initial notification
        val firstState = OrderState.entries[0]
        val notification = createNotification(firstState)

        // Start the tracker
        foodDeliveryTracker?.startTracking()

        return notification
    }

    /**
     * Stop food delivery tracking
     */
    fun stop() {
        isActive = false
        foodDeliveryTracker?.stopTracking()
        foodDeliveryTracker = null
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Update notification with new state
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun updateNotification(state: OrderState) {
        if (!isActive) return

        val notification = createNotification(state)
        notificationManager.notify(
            LiveUpdateNotificationManager.NOTIFICATION_ID_WRITING_SPRINT,
            notification
        )
    }

    /**
     * Create notification for given order state
     * Now uses LiveUpdateNotificationManager (combined from helper)
     */
    private fun createNotification(state: OrderState): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            notificationManager.createSprintDeliveryNotification(
                state.orderStatus,
                state.statusText,
                state.progress
            )
        } else {
            notificationManager.createWritingSprintNotificationCompat(
                state.orderStatus,
                state.statusText,
                state.progress
            )
        }
    }

    /**
     * Handle completion callback
     * Shows the completion state briefly, then dismisses notification and stops service
     */
    private fun complete() {
        Log.d(TAG, "Food delivery tracking completed - will dismiss notification after ${COMPLETION_DISPLAY_DURATION}ms")

        // Stop tracker but keep notification visible for a moment
        isActive = false
        foodDeliveryTracker?.stopTracking()
        foodDeliveryTracker = null

        // Schedule notification dismissal and service stop
        handler.postDelayed({
            Log.d(TAG, "Dismissing food delivery notification")
            // Cancel the notification
            notificationManager.cancel(LiveUpdateNotificationManager.NOTIFICATION_ID_WRITING_SPRINT)
            // Notify service to stop
            onComplete()
        }, COMPLETION_DISPLAY_DURATION)
    }
}

class FoodDeliveryTracker(
    private val onStateChanged: (OrderState) -> Unit,
    private val onCompleted: () -> Unit
) {
    private val handler = Handler(Looper.getMainLooper())
    private var currentOrderState = 0
    private var isTracking = false

    fun startTracking() {
        isTracking = true
        currentOrderState = 0
        scheduleNextUpdate()
    }

    fun stopTracking() {
        isTracking = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun scheduleNextUpdate() {
        if (!isTracking) return

        val orderStates = OrderState.entries
        if (currentOrderState >= orderStates.size) {
            // Order complete - immediately notify completion
            // (Handler will manage display duration before dismissal)
            onCompleted()
            return
        }

        val state = orderStates[currentOrderState]

        handler.postDelayed({
            onStateChanged(state)
            currentOrderState++
            scheduleNextUpdate()
        }, state.delay)
    }

    fun getCurrentState(): OrderState? {
        val orderStates = OrderState.entries
        return if (currentOrderState > 0 && currentOrderState <= orderStates.size) {
            orderStates[currentOrderState - 1]
        } else {
            null
        }
    }
}

enum class OrderState(
    val progress: Int,
    val statusText: String,        // Status chip text (keep ≤7 chars for best display)
    val orderStatus: String,        // Full notification content text
    val delay: Long                 // Demo timing delay in milliseconds
) {
    INITIALIZING(0, "Placing", "Placing your order...", 3000),                      // 7 chars ✅
    ORDER_CONFIRMED(10, "Confirm", "Order confirmed - Restaurant preparing", 5000),  // 7 chars ✅ (shortened)
    FOOD_PREPARATION(25, "Cooking", "Your food is being prepared", 8000),           // 7 chars ✅ (shortened)
    READY_FOR_PICKUP(50, "Ready", "Food ready - Driver picking up", 10000),         // 5 chars ✅
    FOOD_ENROUTE(75, "On way", "Driver is on the way to you", 12000),               // 6 chars ✅
    FOOD_ARRIVING(90, "Near", "Driver arriving in 2 min", 8000),                    // 4 chars ✅ (shortened)
    ORDER_COMPLETE(100, "Arrived", "Order delivered - Enjoy your meal!", 5000)      // 7 chars ✅
}