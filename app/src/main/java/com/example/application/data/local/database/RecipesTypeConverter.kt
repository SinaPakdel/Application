package com.example.application.data.local.database

import androidx.room.TypeConverter
import com.example.application.data.models.FoodRecipes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipes: FoodRecipes): String {
        return gson.toJson(foodRecipes)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipes {
        val listType = object : TypeToken<FoodRecipes>() {}.type
        return gson.fromJson(data, listType)
    }
}
