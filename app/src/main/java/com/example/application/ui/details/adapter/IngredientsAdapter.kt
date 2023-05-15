package com.example.application.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.*
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.application.R
import com.example.application.data.models.ExtendedIngredient
import com.example.application.databinding.ItemIngredientBinding
import com.example.application.utils.consts.Constants.Companion.BASE_IMAGE_URL
import com.example.application.utils.diffutil.RecipesDiffUtil
import java.util.Locale

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private var ingredientsList = emptyList<ExtendedIngredient>()

    inner class ViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount(): Int = ingredientsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ingredientsList[position].also {
            holder.binding.apply {
                imgIngredient.load(BASE_IMAGE_URL + it.image){crossfade(600)
                error(R.drawable.ic_error_placeholder)}
                tvIngredientName.text = it.name.capitalize(Locale.ROOT)
                tvIngredientAmount.text = it.amount.toString()
                tvIngredientUnit.text = it.unit
                tvIngredientConsistency.text = it.consistency
                tvOrigin.text = it.original
            }
        }
    }

    fun setData(ingredient: List<ExtendedIngredient>) {
        val ingredientRecipesDiffUtil = RecipesDiffUtil(ingredientsList, ingredient)
        val diffResult = calculateDiff(ingredientRecipesDiffUtil)
        ingredientsList = ingredient
        diffResult.dispatchUpdatesTo(this)
    }
}