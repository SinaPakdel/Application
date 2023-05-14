package com.example.application.ui.main.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.application.data.local.database.RecipeEntity
import com.example.application.data.models.FoodRecipes
import com.example.application.data.repository.Repository
import com.example.application.utils.safeapi.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    /** DATA BASE */
    private val _readRecipes = MutableLiveData<List<RecipeEntity>>()
    val readRecipes: LiveData<List<RecipeEntity>> get() = _readRecipes

    fun getRecipe() = viewModelScope.launch {
        repository.local.readDatabase().collect {
            _readRecipes.postValue(it)
        }
    }

    private fun insertRecipes(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipeEntity)
    }

    /** RETROFIT */
    var recipeResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    var searchedRecipeResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    fun getRecipes(queryMap: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queryMap)
    }

    fun searchRecipes(searchQuery: Map<String, String>) =
        viewModelScope.launch { searchRecipeSafeCall(searchQuery) }

    private suspend fun searchRecipeSafeCall(searchQuery: Map<String, String>) {
        searchedRecipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipeResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchedRecipeResponse.value = NetworkResult.Error("Recipes Not Found.")
            }
        } else {
            searchedRecipeResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private suspend fun getRecipesSafeCall(queryMap: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queryMap)
                recipeResponse.value = handleFoodRecipesResponse(response)
                val foodRecipes = recipeResponse.value!!.data
                if (foodRecipes != null) {
                    offlineCacheRecipes(foodRecipes)
                }
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes Not Found.")
            }
        } else {
            recipeResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipes: FoodRecipes) {
        val recipeEntity = RecipeEntity(foodRecipes)
        insertRecipes(recipeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipes>): NetworkResult<FoodRecipes> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited")
            }

            response.body()!!.foodResults.isEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }

            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}