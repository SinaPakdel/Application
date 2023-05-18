package com.example.application.data.remote

import com.example.application.data.models.FoodJoke
import com.example.application.data.models.FoodRecipes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodService {
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipes>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipes>

    @GET("/food/joke/random")
    suspend fun getFoodJoke(
    ): Response<FoodJoke>
}