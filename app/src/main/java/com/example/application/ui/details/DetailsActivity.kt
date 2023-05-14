package com.example.application.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.example.application.R
import com.example.application.databinding.ActivityDetailsBinding
import com.example.application.ui.details.adapter.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter
    private val list: ArrayList<String> = arrayListOf(" Todo", "Doing", "Done")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        implementViewPager(binding)

        with(binding) {
        }
        binding.materialToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun implementViewPager(binding: ActivityDetailsBinding) {
        with(binding) {
            detailsViewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, detailsViewPager) { tab, positon ->
                tab.text = list[positon]
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