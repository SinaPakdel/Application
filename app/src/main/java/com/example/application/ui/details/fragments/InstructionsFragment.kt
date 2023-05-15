package com.example.application.ui.details.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.application.R
import com.example.application.data.models.FoodResult
import com.example.application.databinding.FragmentIngredientBinding
import com.example.application.databinding.FragmentInstructionsBinding
import com.example.application.utils.consts.Constants
import com.example.application.utils.consts.Constants.Companion.RECIPE_BUNDLE_KEY

class InstructionsFragment : Fragment() {
    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val recipesArgument: FoodResult? = args?.getParcelable(RECIPE_BUNDLE_KEY)
        with(binding) {
            wvInstruction.webViewClient =object :WebViewClient(){}
            recipesArgument?.let {
                wvInstruction.loadUrl(it.sourceUrl)
            }
        }
    }

}