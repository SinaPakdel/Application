package com.example.application.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.application.utils.consts.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_DIET_TYPE_POSITION
import com.example.application.utils.consts.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_MEAL_TYPE_POSITION
import com.example.application.utils.consts.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.application.utils.consts.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.application.utils.consts.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.application.utils.consts.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.application.utils.consts.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKey {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.selectedMealType] = mealType
            preferences[PreferencesKey.selectedMealTypeId] = mealTypeId
            preferences[PreferencesKey.selectedDietType] = dietType
            preferences[PreferencesKey.selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception

        }.map { preferences ->
            val selectedMealType = preferences[PreferencesKey.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId =
                preferences[PreferencesKey.selectedMealTypeId] ?: DEFAULT_MEAL_TYPE_POSITION
            val selectedDietType = preferences[PreferencesKey.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId =
                preferences[PreferencesKey.selectedDietTypeId] ?: DEFAULT_DIET_TYPE_POSITION
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)