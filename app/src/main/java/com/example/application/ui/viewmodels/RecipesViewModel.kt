package com.example.application.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.application.utils.consts.Constants
import com.example.application.utils.consts.Constants.Companion.API_KEY_VALUE
import com.example.application.utils.consts.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.application.utils.consts.Constants.Companion.QUERY_DIET
import com.example.application.utils.consts.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.application.utils.consts.Constants.Companion.QUERY_NUM
import com.example.application.utils.consts.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(application: Application) : ViewModel() {

    fun applyQueries() = hashMapOf<String, String>().apply {
        put(QUERY_NUM, "50")
        put(API_KEY_VALUE, Constants.API_KEY)
        put(QUERY_TYPE, "snack")
        put(QUERY_DIET, "vegan")
        put(QUERY_ADD_RECIPE_INFORMATION, "true")
        put(QUERY_FILL_INGREDIENTS, "true")
    }
}