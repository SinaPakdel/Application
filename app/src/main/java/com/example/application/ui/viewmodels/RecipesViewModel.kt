package com.example.application.ui.viewmodels

import android.app.Application
import android.app.DownloadManager.Query
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import com.example.application.utils.consts.Constants.Companion.QUERY_SEARCH
import com.example.application.utils.consts.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()
    fun saveMealAndDietType(
        mealType: String,
        mealTypeInt: Int,
        dietType: String,
        dietTypeInt: Int
    ) = viewModelScope.launch {
        dataStoreRepository.saveMealAndDietType(mealType, mealTypeInt, dietType, dietTypeInt)
    }

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) { dataStoreRepository.saveBackOnline(backOnline) }

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


    fun applySearchQuery(searchQuery: String) = hashMapOf<String, String>().apply {
        put(QUERY_SEARCH, searchQuery)
        put(QUERY_NUM, DEFAULT_RECIPES_NUMBER)
        put(API_KEY_VALUE, Constants.API_KEY)
        put(QUERY_ADD_RECIPE_INFORMATION, "true")
        put(QUERY_FILL_INGREDIENTS, "true")
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(application, "No Internet conection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(application, "We'r back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}