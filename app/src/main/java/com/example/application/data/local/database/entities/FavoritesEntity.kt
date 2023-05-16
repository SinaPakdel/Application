package com.example.application.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.application.data.models.FoodResult
import com.example.application.utils.consts.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(@PrimaryKey(autoGenerate = true) var id: Int = 0, var result: FoodResult)