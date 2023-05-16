package com.example.application.data.local.database

import androidx.room.TypeConverter
import com.example.application.data.models.FoodRecipes
import com.example.application.data.models.FoodResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipes: FoodRecipes): String {
        return gson.toJson(foodRecipes)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipes {
        val listType = object : TypeToken<FoodRecipes>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun foodResultToString(result: FoodResult): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToFoodResult(data: String): FoodResult {
        val listType = object : TypeToken<FoodResult>() {}.type
        return gson.fromJson(data, listType)
    }
}
