package com.example.application.di

import android.app.Application
import androidx.room.Room
import com.example.application.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


/**
 * ApplicationComponent is renamed to SingletonComponent
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Drop the table and make new one with fallbackToDestructiveMigration()
     *
     * with .addCallback() we could add some dummy data or mock data to present some data for user
     * its bcz we don't want user see empty data when app launched
     *
     * remember that callback() impl after dataBase created not with that
     */
    @Provides
    @Singleton
    fun provideDatabase(application: Application, addCallback: TaskDatabase.Callback) = Room.databaseBuilder(application, TaskDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(addCallback)
        .build()

    /**
     * TaskDao is automatically singleton bcz of taskDatabase under the hood
     */
    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase) = taskDatabase.taskDao()

    /**
     * this CoroutineScope(SupervisorJob()) tell if other threads goes down this method stay alive
     */
    @APPLICATION_SCOPE
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}


/**
 * its seems Qualifier annotations not work out of  @Retention(AnnotationRetention.RUNTIME)
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class APPLICATION_SCOPE