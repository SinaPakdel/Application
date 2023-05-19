package com.example.application.data

import com.example.application.data.local.database.dao.FavoriteRecipeDao
import com.example.application.data.local.database.dao.FoodJokeDao
import com.example.application.data.local.database.entities.RecipeEntity
import com.example.application.data.local.database.dao.RecipesDao
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.data.local.database.entities.FoodJokeEntity
import com.example.application.data.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao,
    private val favoriteRecipeDao: FavoriteRecipeDao,
    private val foodJokeDao: FoodJokeDao
) {

    fun readRecipes(): Flow<List<RecipeEntity>> = recipesDao.readRecipes()

    suspend fun insertRecipes(recipeEntity: RecipeEntity) = recipesDao.insertRecipes(recipeEntity)


    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> = favoriteRecipeDao.readFavoriteRecipes()

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) =
        favoriteRecipeDao.insertFavoriteFood(favoritesEntity)

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        favoriteRecipeDao.deleteFavoriteRecipe(favoritesEntity)

    suspend fun deleteAllFavoriteRecipes() = favoriteRecipeDao.deleteAllFavoriteRecipes()


    fun readFoodJoke(): Flow<List<FoodJokeEntity>> = foodJokeDao.readFoodJoke()

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = foodJokeDao.insertFoodJoke(foodJokeEntity)


}