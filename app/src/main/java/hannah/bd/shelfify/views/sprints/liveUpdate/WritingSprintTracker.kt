package hannah.bd.shelfify.views.sprints.liveUpdate

import android.os.Handler
import android.os.Looper

class WritingSprintTracker(
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
    INITIALIZING(0, "Start", "Let's get ready to write...", 3000),
    SPRINT(2, "Sprint", "Let's get those words written", 2000),
    STAGE_TWO(10, "Sprint", "Super excited to see what you write", 5000),
    STAGE_THREE(25, "Sprint", "Keep writing! I believe in you!", 8000),           // 7 chars ✅ (shortened)
    STAGE_FOUR(50, "Sprint", "Get those words written", 10000),         // 5 chars ✅
    STAGE_FIVE(75, "Sprint", "Keep going, the writing sprint is nearly done", 12000),               // 6 chars ✅
    STAGE_SIX(90, "Sprint", "Final writing push!", 8000),                    // 4 chars ✅ (shortened)
    ORDER_COMPLETE(100, "Done", "Writing Sprint Finished - Fancy Another Sprint?", 20000)
}