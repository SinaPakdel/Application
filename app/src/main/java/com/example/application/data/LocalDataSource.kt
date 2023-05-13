package com.example.application.data

import com.example.application.data.local.database.RecipeEntity
import com.example.application.data.local.database.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    suspend fun readDatabase(): Flow<List<RecipeEntity>> = recipesDao.readRecipes()

    suspend fun insertRecipes(recipeEntity: RecipeEntity) = recipesDao.insertRecipes(recipeEntity)

}