package com.example.application.data

import com.example.application.data.local.database.entities.RecipeEntity
import com.example.application.data.local.database.RecipesDao
import com.example.application.data.local.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    fun readRecipes(): Flow<List<RecipeEntity>> = recipesDao.readRecipes()

    suspend fun insertRecipes(recipeEntity: RecipeEntity) = recipesDao.insertRecipes(recipeEntity)


    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> = recipesDao.readFavoriteRecipes()

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) =
        recipesDao.insertFavoriteFood(favoritesEntity)

    suspend fun deleteFavoriteRecipes(favoritesEntity: FavoritesEntity) =
        recipesDao.deleteFavoriteRecipe(favoritesEntity)

    suspend fun deleteAllFavoriteRecipes() = recipesDao.deleteAllFavoriteRecipes()


}