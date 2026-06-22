package hannah.bd.shelfify.modals

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesKeys {
    val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
}

class UserPreferences(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("settings")

    val hasSeenOnboarding: Flow<Boolean> =
        context.dataStore.data.map {
            it[PreferencesKeys.HAS_SEEN_ONBOARDING] ?: false
        }

    suspend fun completeOnboarding() {
        context.dataStore.edit {
            it[PreferencesKeys.HAS_SEEN_ONBOARDING] = true
        }
    }
}