package com.example.application.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.application.R
import com.example.application.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navHost: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Application)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
//        setupToolbar()
        setupBtmNavView()

    }

    private fun setupToolbar() {
//        setSupportActionBar(binding.customToolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        appBarConfig =
//            AppBarConfiguration(setOf(R.id.recipesFragment, R.id.favoriteRecipesFragment, R.id.foodJokeFragment))
//        setupActionBarWithNavController(navController, appBarConfig)
    }


    private fun setupNavController() {
        navHost =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        navController = navHost.navController
    }

    private fun setupBtmNavView() = binding.btmNavView.setupWithNavController(navController)


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}