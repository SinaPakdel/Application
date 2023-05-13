package com.example.application.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.application.data.models.FoodRecipes
import com.example.application.utils.consts.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipeEntity(
    var foodRecipes: FoodRecipes
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}