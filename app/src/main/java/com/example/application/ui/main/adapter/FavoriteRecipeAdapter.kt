package com.example.application.ui.main.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ActionMode
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.databinding.ItemFaovriteRecipesBinding
import com.example.application.ui.main.fragments.favorite.FavoriteRecipesFragmentDirections
import com.example.application.utils.diffutil.RecipesDiffUtil

class FavoriteRecipeAdapter(private val requireActivity: FragmentActivity) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>(), ActionMode.Callback {
    private var favoriteFoodResults = emptyList<FavoritesEntity>()

    inner class ViewHolder(private val binding: ItemFaovriteRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                setOnClickListener {
                    findNavController()
                        .navigate(
                            FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                                favoriteFoodResults[absoluteAdapterPosition].result
                            )
                        )
                }

                setOnLongClickListener {
                    requireActivity.startActionMode(this@FavoriteRecipeAdapter)
                    true
                }
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

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_contextual_favorite, menu)
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

}
