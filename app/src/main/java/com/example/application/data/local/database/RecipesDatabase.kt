package com.example.application.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.application.data.local.database.dao.FavoriteRecipeDao
import com.example.application.data.local.database.dao.FoodJokeDao
import com.example.application.data.local.database.dao.RecipesDao
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.data.local.database.entities.FoodJokeEntity
import com.example.application.data.local.database.entities.RecipeEntity

@Database(entities = [RecipeEntity::class, FavoritesEntity::class, FoodJokeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipesDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    abstract fun foodJokeDao(): FoodJokeDao
}