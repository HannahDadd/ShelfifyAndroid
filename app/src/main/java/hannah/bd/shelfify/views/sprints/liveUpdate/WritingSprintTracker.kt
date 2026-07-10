package hannah.bd.shelfify.views.sprints.liveUpdate

import android.os.Handler
import android.os.Looper

class WritingSprintTracker(
    private val delayLength: String,
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

        if (delayLength == "20mins") {
            handler.postDelayed({
                onStateChanged(state)
                currentOrderState++
                scheduleNextUpdate()
            }, state.delayTwenty)
        }

        if (delayLength == "40mins") {
            handler.postDelayed({
                onStateChanged(state)
                currentOrderState++
                scheduleNextUpdate()
            }, state.delayForty)
        }

        if (delayLength == "60mins") {
            handler.postDelayed({
                onStateChanged(state)
                currentOrderState++
                scheduleNextUpdate()
            }, state.delaySixty)
        }
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
    val statusText: String,
    val orderStatus: String,
    val delayTwenty: Long,
    val delayForty: Long,
    val delaySixty: Long
) {
    INITIALIZING(0, "Start", "Get ready to write...", 2000, 2000, 2000),
    SPRINT_ZERO(2, "Start", "Let's get writing!", 2000, 2000, 2000),
    SPRINT_ONE(10, "Start", "Let's get writing!", 120000, 240000, 360000),
    SPRINT(20, "Sprint", "Get those words written", 120000, 240000, 360000),
    STAGE_TWO(30, "Sprint", "Keep going!", 120000, 240000, 360000),
    STAGE_THREE(40, "Sprint", "Keep writing!", 120000, 240000, 360000),
    STAGE_FOUR(50, "Sprint", "Get those words written", 120000, 240000, 360000),
    STAGE_FIVE(60, "Sprint", "That's over half the writing sprint finished", 120000, 240000, 360000),
    STAGE_SIX(70, "Sprint", "Keep writing!", 120000, 240000, 360000),
    STAGE_SEVEN(80, "Sprint", "Keep going, the writing sprint is nearly done", 120000, 240000, 360000),
    STAGE_EIGHT(90, "Sprint", "Final writing push!", 120000, 240000, 360000),
    ORDER_COMPLETE(100, "Done", "Writing Sprint Finished - Fancy Another Sprint?", 120000, 240000, 360000)
}