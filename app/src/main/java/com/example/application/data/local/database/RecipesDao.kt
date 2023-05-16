package com.example.application.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.application.data.local.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipeEntity: RecipeEntity)

    @Query("select * from recipe_table order by id asc")
    fun readRecipes(): Flow<List<RecipeEntity>>
}