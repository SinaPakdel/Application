package com.example.application.di.modules

import android.app.Application
import androidx.room.Room
import com.example.application.data.LocalDataSource
import com.example.application.data.local.database.RecipesDao
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
    fun provideMovieDao(movieDatabase: RecipesDatabase): RecipesDao = movieDatabase.recipeDao()


    @Provides
    @Singleton
    fun provideLocalDataSource(movieDao: RecipesDao): LocalDataSource = LocalDataSource(movieDao)
}