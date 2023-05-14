package com.example.application.utils.consts

class Constants {
    companion object {
        const val API_KEY = "416414fd5361418c85b07ec5146fca0c"
        const val BASE_URL = "https://api.spoonacular.com/"

        const val API_KEY_VALUE = "apiKey"
        const val QUERY_NUM = "number"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        const val DATABASE_NAME = "recipe_database"
        const val RECIPES_TABLE = "recipe_table"

        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_MEAL_TYPE_POSITION = 0
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_DIET_TYPE_POSITION =0
        const val DEFAULT_RECIPES_NUMBER = "50"

        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"

        const val PREFERENCES_NAME="food_pref"
    }
}