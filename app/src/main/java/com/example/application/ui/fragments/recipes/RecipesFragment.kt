package com.example.application.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentRecipesBinding
import com.example.application.ui.adapter.RecipesAdapter
import com.example.application.ui.viewmodels.MainViewModel
import com.example.application.ui.viewmodels.RecipesViewModel
import com.example.application.utils.observeOnce
import com.example.application.utils.safeapi.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val recipesAdapter by lazy { RecipesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        readDatabase()
        with(binding) {
            floatingActionButton.setOnClickListener {
                findNavController().navigate(
                    RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheet()
                )
            }
        }
    }

    private fun readDatabase() {
        mainViewModel.getRecipe()
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) {
            if (it.toMutableList().isNotEmpty()) {
                recipesAdapter.setData(it[0].foodRecipes);hideShimmerEffect()
            } else requestApiData()
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCach()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { recipesAdapter.setData(it) }
                }
            }
        }
    }

    private fun loadDataFromCach() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) recipesAdapter.setData(it[0].foodRecipes)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRecipes.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(this.context)
            showShimmerEffect()
        }
    }

    private fun showShimmerEffect() {
        binding.rvRecipes.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.rvRecipes.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}