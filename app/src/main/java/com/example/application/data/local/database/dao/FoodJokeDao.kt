package com.example.application.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.application.data.local.database.entities.FoodJokeEntity
import com.example.application.data.models.FoodJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodJokeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("select * from food_joke_table order by id asc")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>
}