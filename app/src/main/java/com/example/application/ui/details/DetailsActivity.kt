package com.example.application.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.application.R
import com.example.application.databinding.ActivityDetailsBinding
import com.example.application.ui.details.adapter.PagerAdapter
import com.example.application.utils.consts.Constants.Companion.INGREDIENTS
import com.example.application.utils.consts.Constants.Companion.INSTRUCTIONS
import com.example.application.utils.consts.Constants.Companion.OVERVIEW
import com.example.application.utils.consts.Constants.Companion.RECIPE_BUNDLE_KEY
import com.example.application.utils.view.onQueryTextSubmit
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter
    private val args by navArgs<DetailsActivityArgs>()
    private val list = arrayListOf(OVERVIEW, INGREDIENTS, INSTRUCTIONS)

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
        menuInflater.inflate(R.menu.menu_details,menu);return true
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
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}