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
import com.example.application.ui.main.viewmodels.MainViewModel
import com.example.application.utils.diffutil.RecipesDiffUtil
import com.example.application.utils.view.makeSnack
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>(), ActionMode.Callback {
    private lateinit var currentFavoriteFood: FavoritesEntity
    private var favoriteFoodResults = emptyList<FavoritesEntity>()

    private var multiSelection = false
    private val selectedRecipes = arrayListOf<FavoritesEntity>()

    private val myViewHolders = arrayListOf<ViewHolder>()
    private lateinit var actionMode: ActionMode
    private lateinit var currentBinding: ItemFaovriteRecipesBinding

    inner class ViewHolder(val binding: ItemFaovriteRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            currentBinding = binding
            binding.root.apply {
                setOnClickListener {
                    if (multiSelection) {
                        applySelection(binding, currentFavoriteFood)
                    } else {
                        findNavController()
                            .navigate(
                                FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                                    favoriteFoodResults[absoluteAdapterPosition].result
                                )
                            )
                    }
                }

                setOnLongClickListener {
                    if (!multiSelection) {
                        multiSelection = true
                        requireActivity.startActionMode(this@FavoriteRecipeAdapter)
                        applySelection(binding, currentFavoriteFood)
                        true
                    } else {
                        multiSelection = false
                        false
                    }

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
        myViewHolders.add(holder)
        currentFavoriteFood = favoriteFoodResults[position]
        holder.bind(currentFavoriteFood)
    }

    fun setData(favoritesEntityList: List<FavoritesEntity>) {
        val diffUtil = RecipesDiffUtil(favoriteFoodResults, favoritesEntityList)
        val calculateDiff = DiffUtil.calculateDiff(diffUtil)
        favoriteFoodResults = favoritesEntityList
        calculateDiff.dispatchUpdatesTo(this)
    }


    private fun applySelection(
        binding: ItemFaovriteRecipesBinding,
        currentRecipes: FavoritesEntity
    ) {
        if (selectedRecipes.contains(currentRecipes)) {
            selectedRecipes.remove(currentRecipes)
            changeRecipeStyle(binding, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipes)
            changeRecipeStyle(binding, R.color.cardBackgroundColorLight, R.color.purple_500)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(
        binding: ItemFaovriteRecipesBinding,
        backGround: Int,
        strokeColor: Int
    ) {
        binding.apply {
            favoriteRecipesItemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    backGround
                )
            )
            cardViewFavorite.strokeColor = ContextCompat.getColor(root.context, strokeColor)
        }

    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                actionMode.finish()
            }

            1 -> {
                actionMode.title = "${selectedRecipes.size} item selected"
            }

            else -> {
                actionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_contextual_favorite, menu)
        mode?.let { actionMode = it }
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_deltete) {
            selectedRecipes.forEach { mainViewModel.deleteFavoriteRecipe(it) }
            Snackbar.make(currentBinding.root, "${selectedRecipes.size} Recipe's removed", Snackbar.LENGTH_SHORT).show()
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { viewHolder ->
            changeRecipeStyle(viewHolder.binding, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }


}
