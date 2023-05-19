package com.example.application.di.modules

import android.app.Application
import androidx.room.Room
import com.example.application.data.LocalDataSource
import com.example.application.data.local.database.dao.FavoriteRecipeDao
import com.example.application.data.local.database.dao.FoodJokeDao
import com.example.application.data.local.database.dao.RecipesDao
import com.example.application.data.local.database.RecipesDatabase
import com.example.application.di.qualifier.DatabaseName
import com.example.application.utils.consts.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    @DatabaseName
    fun provideDatabaseName(): String = DATABASE_NAME

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        @DatabaseName dataBase: String
    ): RecipesDatabase =
        Room.databaseBuilder(application, RecipesDatabase::class.java, dataBase)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMovieDao(dataBase: RecipesDatabase): RecipesDao = dataBase.recipeDao()

    @Provides
    @Singleton
    fun provideFavoriteRecipeDao(dataBase: RecipesDatabase): FavoriteRecipeDao = dataBase.favoriteRecipeDao()

    @Provides
    @Singleton
    fun provideFoodJokeDao(dataBase: RecipesDatabase): FoodJokeDao = dataBase.foodJokeDao()


    @Provides
    @Singleton
    fun provideLocalDataSource(recipesDao: RecipesDao, favoriteRecipeDao: FavoriteRecipeDao, foodJokeDao: FoodJokeDao): LocalDataSource =
        LocalDataSource(recipesDao, favoriteRecipeDao, foodJokeDao)
}