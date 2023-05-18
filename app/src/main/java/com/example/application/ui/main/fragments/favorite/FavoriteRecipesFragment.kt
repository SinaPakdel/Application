package com.example.application.ui.main.fragments.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentFavoriteRecipesBinding
import com.example.application.databinding.FragmentRecipesBinding
import com.example.application.ui.main.adapter.FavoriteRecipeAdapter
import com.example.application.ui.main.viewmodels.MainViewModel
import com.example.application.utils.view.makeSnack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {
    private val TAG = "FavoriteRecipesFragment"
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    private val fMainViewModel: MainViewModel by viewModels()
    private val favoriteRecipeAdapter by lazy { FavoriteRecipeAdapter(requireActivity(), fMainViewModel) }
    private lateinit var menuHost: MenuHost

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = fMainViewModel
        binding.favoriteAdapter = favoriteRecipeAdapter

        menuHost = requireActivity()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvFavoriteRecipe.apply {
                adapter = favoriteRecipeAdapter
                layoutManager = LinearLayoutManager(binding.root.context)
            }
        }

        fMainViewModel.getFavoriteRecipes()
        fMainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            Log.e(TAG, "onViewCreated: ${it.size}")
            favoriteRecipeAdapter.setData(it)
        }
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_favorite_recipe, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionDeleteAll -> {
                        fMainViewModel.deleteAllFavoriteRecipes()
                        makeSnack("Items Deleted",binding.root)
                        ;true
                    }

                    else -> false
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        favoriteRecipeAdapter.clearContextualActionMode()
    }
}