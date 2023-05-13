package com.example.application.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.local.datastore.DataStoreRepository
import com.example.application.utils.consts.Constants
import com.example.application.utils.consts.Constants.Companion.API_KEY_VALUE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.application.utils.consts.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.application.utils.consts.Constants.Companion.QUERY_DIET
import com.example.application.utils.consts.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.application.utils.consts.Constants.Companion.QUERY_NUM
import com.example.application.utils.consts.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val application: Application,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    fun saveMealAndDietType(
        mealType: String,
        mealTypeInt: Int,
        dietType: String,
        dietTypeInt: Int
    ) = viewModelScope.launch {
        dataStoreRepository.saveMealAndDietType(mealType, mealTypeInt, dietType, dietTypeInt)
    }

    fun applyQueries() = hashMapOf<String, String>().apply {
        viewModelScope.launch {
            readMealAndDietType.collect { values ->
                mealType = values.selectedMealType
                dietType = values.selectedDietType
            }
        }
        put(QUERY_NUM, DEFAULT_RECIPES_NUMBER)
        put(API_KEY_VALUE, Constants.API_KEY)
        put(QUERY_TYPE, mealType)
        put(QUERY_DIET, dietType)
        put(QUERY_ADD_RECIPE_INFORMATION, "true")
        put(QUERY_FILL_INGREDIENTS, "true")
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(application, "", Toast.LENGTH_SHORT).show()
        }
    }
}