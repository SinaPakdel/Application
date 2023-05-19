package com.example.application.ui.main.fragments.foodjoke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.application.R
import com.example.application.databinding.FragmentFoodJokeBinding
import com.example.application.databinding.FragmentRecipesBinding
import com.example.application.ui.main.viewmodels.MainViewModel
import com.example.application.utils.safeapi.NetworkResult
import com.example.application.utils.view.makeSnack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment(R.layout.fragment_food_joke) {
    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getFoodJoke()
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    makeSnack(response.message.toString(), binding.root)
                    binding.prgBarFoodJoke.isVisible = false
                    loadFoodJokeFromCache()
                }

                is NetworkResult.Loading -> binding.prgBarFoodJoke.isVisible = true
                is NetworkResult.Success -> {
                    binding.prgBarFoodJoke.isVisible = false
                    binding.tvTitleFoodJoke.text = response.data?.text
                }

            }
        }
    }


    private fun loadFoodJokeFromCache() {
        mainViewModel.getFoodJokeLocal()
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.foodJokeLists.observe(viewLifecycleOwner) {
                if (it.isNotEmpty() && it != null) {
                    binding.tvTitleFoodJoke.text = it[0].foodJoke.text
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}