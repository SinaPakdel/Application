package com.example.application.ui.main.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.application.R
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.data.local.database.entities.FoodJokeEntity
import com.example.application.data.local.database.entities.RecipeEntity
import com.example.application.data.models.FoodJoke
import com.example.application.data.models.FoodRecipes
import com.example.application.data.repository.Repository
import com.example.application.utils.safeapi.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    /** DATA BASE */
    private val _readRecipes = MutableLiveData<List<RecipeEntity>>()
    val readRecipes: LiveData<List<RecipeEntity>> get() = _readRecipes


    private val _readFavoriteRecipes = MutableLiveData<List<FavoritesEntity>>()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> get() = _readFavoriteRecipes


    private val _foodJokeLists = MutableLiveData<List<FoodJokeEntity>>()
    val foodJokeLists: LiveData<List<FoodJokeEntity>> get() = _foodJokeLists


    fun getRecipe() = viewModelScope.launch {
        repository.local.readRecipes().collect {
            _readRecipes.postValue(it)
        }
    }

    fun getFavoriteRecipes() = viewModelScope.launch {
        repository.local.readFavoriteRecipes().collect {
            _readFavoriteRecipes.postValue(it)
        }
    }

    fun getFoodJokeLocal() = viewModelScope.launch {
        repository.local.readFoodJoke().collect {
            _foodJokeLists.postValue(it)
        }
    }

    private fun insertRecipes(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipeEntity)
    }

    fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) { repository.local.insertFoodJoke(foodJokeEntity) }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    /** RETROFIT */
    var recipeResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    var searchedRecipeResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queryMap: Map<String, String>) = viewModelScope.launch { getRecipesSafeCall(queryMap) }
    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch { searchRecipeSafeCall(searchQuery) }
    fun getFoodJoke() = viewModelScope.launch { getFoodJokeSafeCall() }


    private suspend fun searchRecipeSafeCall(searchQuery: Map<String, String>) {
        searchedRecipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipeResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchedRecipeResponse.value = NetworkResult.Error(application.applicationContext.getString(R.string.recipes_not_found))
            }
        } else {
            searchedRecipeResponse.value = NetworkResult.Error(application.applicationContext.getString(R.string.no_internet_connection))
        }
    }

    private suspend fun getFoodJokeSafeCall() {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke()
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke != null) {
                    offlineCacheFoodJoke(foodJoke)
                }

            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error(application.applicationContext.getString(R.string.joke_not_found))
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error(application.applicationContext.getString(R.string.no_internet_connection))
        }
    }

    private fun handleFoodJokeResponse(foodJokeResponse: Response<FoodJoke>): NetworkResult<FoodJoke> {
        return when {
            foodJokeResponse.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }

            foodJokeResponse.code() == 402 -> {
                NetworkResult.Error("Api Key Limited")
            }

            foodJokeResponse.isSuccessful -> {
                val foodJoke = foodJokeResponse.body()
                NetworkResult.Success(foodJoke)
            }

            else -> {
                NetworkResult.Error(foodJokeResponse.message())
            }
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

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
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