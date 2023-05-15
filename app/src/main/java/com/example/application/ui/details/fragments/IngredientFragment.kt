package com.example.application.ui.details.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.data.models.FoodResult
import com.example.application.databinding.FragmentIngredientBinding
import com.example.application.ui.details.adapter.IngredientsAdapter
import com.example.application.utils.consts.Constants


class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private lateinit var ingredientsAdapter: IngredientsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientsAdapter=IngredientsAdapter()
        val args = arguments
        val recipeBundle: FoodResult? = args?.getParcelable(Constants.RECIPE_BUNDLE_KEY)


        with(binding) {
            rvIngredients.adapter = ingredientsAdapter
            rvIngredients.layoutManager = LinearLayoutManager(binding.root.context)
        }
        recipeBundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }

    }
}