package com.example.application.data.models


import com.google.gson.annotations.SerializedName

data class FoodRecipes(
    @SerializedName("results")
    val foodResults: List<FoodResult>
)