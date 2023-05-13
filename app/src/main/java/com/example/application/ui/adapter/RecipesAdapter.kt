package com.example.application.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.models.FoodRecipes
import com.example.application.data.models.FoodResult
import com.example.application.databinding.ItemRecepiesBinding
import com.example.application.utils.diffutil.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    private var foodResults = emptyList<FoodResult>()

    inner class ViewHolder(private val binding: ItemRecepiesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: FoodResult) {
            binding.result = result
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRecepiesBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = foodResults.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFoodResult = foodResults[position]
        holder.bind(currentFoodResult)
    }

    fun setData(newData: FoodRecipes) {
        val diffUtil = RecipesDiffUtil(foodResults, newData.foodResults)
        val calculateDiff = DiffUtil.calculateDiff(diffUtil)
        foodResults = newData.foodResults
        calculateDiff.dispatchUpdatesTo(this)
    }
}

