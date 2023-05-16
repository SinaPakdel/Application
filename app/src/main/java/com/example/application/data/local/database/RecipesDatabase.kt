package com.example.application.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.application.data.local.database.entities.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase :RoomDatabase(){
    abstract fun recipeDao(): RecipesDao
}