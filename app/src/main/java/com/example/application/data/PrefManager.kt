package com.example.application.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PrefManager"

enum class SortOrder { BY_NAME, BY_DATE }

data class FilterPrefs(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PrefManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore("USER_PREF")

    val preFlow: Flow<FilterPrefs> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            val sortOrder: SortOrder = SortOrder.valueOf(it[PrefKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name)
            val hideCompleted: Boolean = it[PrefKeys.HIDE_COMPLETED] ?: false

            FilterPrefs(sortOrder, hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit {
            it[PrefKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit {
            it[PrefKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PrefKeys {
        val SORT_ORDER: Preferences.Key<String> = preferencesKey<String>("SORT_ORDER")
        val HIDE_COMPLETED: Preferences.Key<Boolean> = preferencesKey<Boolean>("HIDE_COMPLETED")
    }
}