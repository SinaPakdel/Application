package com.example.application.data

import com.example.application.data.models.FoodRecipes
import com.example.application.data.remote.FoodService
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodService: FoodService
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipes> = foodService.getRecipes(queries)
}