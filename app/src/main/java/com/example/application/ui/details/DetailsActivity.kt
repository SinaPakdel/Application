package com.example.application.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.application.R
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
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter
    private val args by navArgs<DetailsActivityArgs>()
    private val list = arrayListOf(OVERVIEW, INGREDIENTS, INSTRUCTIONS)
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_BUNDLE_KEY, args.result)

        pagerAdapter = PagerAdapter(resultBundle, supportFragmentManager, lifecycle)
        implementViewPager(binding)

        binding.materialToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu);return true
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_add_to_favorite) {
            saveButtonClicked(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveButtonClicked(item: MenuItem) {
        mainViewModel.insertFavoriteRecipes(FavoritesEntity(args.result, 0))
        R.color.yellow.changeMenuItemColor(item)
        makeSnack(getString(R.string.recipe_saved), binding.root)
        Snackbar.make(binding.root, "make", Snackbar.LENGTH_LONG).setAction("OKAY") {}.show()
    }

    private fun Int.changeMenuItemColor(item: MenuItem) {
        item.icon?.setTint(ContextCompat.getColor(this@DetailsActivity, this))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}