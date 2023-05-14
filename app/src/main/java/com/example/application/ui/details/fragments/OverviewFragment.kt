package com.example.application.ui.details.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.application.R
import com.example.application.data.models.FoodResult
import com.example.application.databinding.FragmentOverviewBinding
import com.example.application.databinding.FragmentRecipesBinding
import com.example.application.utils.consts.Constants.Companion.RECIPE_BUNDLE_KEY
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {
    private val TAG = "OverviewFragment"
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val recipeBundle: FoodResult? = args?.getParcelable(RECIPE_BUNDLE_KEY)

        with(binding) {
            val color = root.context.getColor(R.color.green)
            recipeBundle?.also {
                tvTitle.text = it.title
                imgOverview.load(it.image)
                tvLikes.text = it.aggregateLikes.toString()
                tvTime.text = it.readyInMinutes.toString()
                tvSummary.text = Jsoup.parse(it.summary).text()

                if (it.vegetarian) {
                    tvCheckVegetarian.apply {
                        setTextColor(color);compoundDrawableTintList = ColorStateList.valueOf(color)
                    }
                }
                if (it.glutenFree) {
                    tvCheckGlutenFree.apply {
                        setTextColor(color)
                        compoundDrawableTintList = ColorStateList.valueOf(color)
                    }
                }
                if (it.veryHealthy) {
                    tvCheckHealthy.apply {
                        setTextColor(color)
                        compoundDrawableTintList = ColorStateList.valueOf(color)
                    }
                }
                if (it.vegan) {
                    tvCheckVegan.apply {
                        setTextColor(color)
                        compoundDrawableTintList = ColorStateList.valueOf(color)
                    }
                }
                if (it.dairyFree) {
                    tvDairyFree.apply {
                        setTextColor(color)
                        compoundDrawableTintList = ColorStateList.valueOf(color)
                    }
                }
                if (it.cheap) {
                    tvCheap.apply {
                        setTextColor(color)
                        compoundDrawableTintList = ColorStateList.valueOf(color)
                    }
                }
            }
        }
    }

}