package com.example.application.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.data.models.FoodRecipes
import com.example.application.data.models.FoodResult
import com.example.application.databinding.ItemFaovriteRecipesBinding
import com.example.application.ui.main.fragments.favorite.FavoriteRecipesFragmentDirections
import com.example.application.utils.diffutil.RecipesDiffUtil

class FavoriteRecipeAdapter : RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>() {
    private var favoriteFoodResults = emptyList<FavoritesEntity>()

    inner class ViewHolder(private val binding: ItemFaovriteRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.root.findNavController()
                    .navigate(
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                            favoriteFoodResults[absoluteAdapterPosition].result
                        )
                    )
            }
        }

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoriteEntity = favoritesEntity
            /**
             * this will basically update all our views
             */
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemFaovriteRecipesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = favoriteFoodResults.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFavoriteFoodResult = favoriteFoodResults[position]
        holder.bind(currentFavoriteFoodResult)
    }

    fun setData(favoritesEntityList: List<FavoritesEntity>) {
        val diffUtil = RecipesDiffUtil(favoriteFoodResults, favoritesEntityList)
        val calculateDiff = DiffUtil.calculateDiff(diffUtil)
        favoriteFoodResults = favoritesEntityList
        calculateDiff.dispatchUpdatesTo(this)
    }
}
