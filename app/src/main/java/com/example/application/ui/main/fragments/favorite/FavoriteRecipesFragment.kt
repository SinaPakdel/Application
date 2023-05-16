package com.example.application.ui.main.fragments.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentFavoriteRecipesBinding
import com.example.application.databinding.FragmentRecipesBinding
import com.example.application.ui.main.adapter.FavoriteRecipeAdapter
import com.example.application.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {
    private val TAG="FavoriteRecipesFragment"
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    private val favoriteRecipeAdapter by lazy { FavoriteRecipeAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)

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

        mainViewModel.getFavoriteRecipes()
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner){
            Log.e(TAG, "onViewCreated: ${it.size}", )
            favoriteRecipeAdapter.setData(it)
        }
    }
}