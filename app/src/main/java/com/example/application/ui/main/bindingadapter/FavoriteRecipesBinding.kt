package com.example.application.ui.main.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.ui.main.adapter.FavoriteRecipeAdapter

class FavoriteRecipesBinding {

    companion object {
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            favoriteRecipeAdapter: FavoriteRecipeAdapter?
        ) {
            when (view) {
                is ImageView -> view.isVisible = favoritesEntity.isNullOrEmpty()
                is TextView -> view.isVisible = favoritesEntity.isNullOrEmpty()
                is RecyclerView -> {
                    if (favoritesEntity.isNullOrEmpty()) {
                        view.isVisible = favoritesEntity.isNullOrEmpty()
                        favoritesEntity?.let { favoriteRecipeAdapter?.setData(it) }
                    }
                }
            }
        }
    }
}