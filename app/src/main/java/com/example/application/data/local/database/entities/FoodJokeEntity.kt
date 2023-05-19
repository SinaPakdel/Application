package com.example.application.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.application.data.models.FoodJoke
import com.example.application.utils.consts.Constants.Companion.FOOD_JOKE_TABLE
import dagger.Provides

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}