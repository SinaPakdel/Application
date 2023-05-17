package com.example.application.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.application.R
import com.example.application.R.id.action_add_to_favorite
import com.example.application.data.local.database.entities.FavoritesEntity
import com.example.application.databinding.ActivityDetailsBinding
import com.example.application.ui.details.adapter.PagerAdapter
import com.example.application.ui.main.viewmodels.MainViewModel
import com.example.application.utils.consts.Constants.Companion.INGREDIENTS
import com.example.application.utils.consts.Constants.Companion.INSTRUCTIONS
import com.example.application.utils.consts.Constants.Companion.OVERVIEW
import com.example.application.utils.consts.Constants.Companion.RECIPE_BUNDLE_KEY
import com.example.application.utils.view.makeSnack
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val TAG = "DetailsActivity"
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter
    private val args by navArgs<DetailsActivityArgs>()
    private val list = arrayListOf(OVERVIEW, INGREDIENTS, INSTRUCTIONS)
    private val mainViewModel: MainViewModel by viewModels()

    private var recipesSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.materialToolbar)

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_BUNDLE_KEY, args.result)

        pagerAdapter = PagerAdapter(resultBundle, supportFragmentManager, lifecycle)
        implementViewPager(binding)

//        binding.materialToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun implementViewPager(binding: ActivityDetailsBinding) {
        with(binding) {
            detailsViewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, detailsViewPager) { tab, position ->
                tab.text = list[position]
            }.attach()
            detailsViewPager.currentItem = 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        val actionSaved = menu?.findItem(action_add_to_favorite)
        Log.e(TAG, "onCreateOptionsMenu: ")
        mainViewModel.getFavoriteRecipes()

        checkSavedRecipes(actionSaved!!)
        ;return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) {
            try {
                it.forEach { favoriteEntity ->
                    if (favoriteEntity.result.id == args.result.id) {
                        R.color.yellow.changeMenuItemColor(menuItem)
                        savedRecipeId = favoriteEntity.id
                        recipesSaved = true
                    } else R.color.white.changeMenuItemColor(menuItem)
                }
            } catch (e: Exception) {
                Log.e("checkSavedRecipes: ", e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == action_add_to_favorite && !recipesSaved) {
            saveButtonClicked(item)
        } else if (item.itemId == action_add_to_favorite && recipesSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveButtonClicked(item: MenuItem) {
        mainViewModel.insertFavoriteRecipes(FavoritesEntity(0, args.result))
        R.color.yellow.changeMenuItemColor(item)
        makeSnack(getString(R.string.recipe_saved), binding.root)
        Snackbar.make(binding.root, "make", Snackbar.LENGTH_LONG).setAction("OKAY") {}.show()
        recipesSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        R.color.white.changeMenuItemColor(item)
        Snackbar.make(binding.root, "Food removed!, ", Snackbar.LENGTH_LONG).setAction("Undo") {
            saveButtonClicked(item)
        }.show()
        recipesSaved = false
    }

    private fun Int.changeMenuItemColor(item: MenuItem) {
        item.icon?.setTint(ContextCompat.getColor(this@DetailsActivity, this))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}