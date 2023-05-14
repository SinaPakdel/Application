package com.example.application.ui.main.bindingadapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.application.R
import com.example.application.data.models.FoodResult
import com.example.application.ui.main.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

class ItemRecipesBinding {
    companion object {
        @BindingAdapter("onRecipesItemClicked")
        @JvmStatic
        fun onRecipesItemClicked(recipesItem: ConstraintLayout, foodResult: FoodResult) {
            recipesItem.setOnClickListener {
                try {
                    recipesItem.findNavController().navigate(
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(foodResult)
                    )
                } catch (e: Exception) {
                    Log.d("TAG", "onRecipesItemClicked: ${e.toString()}")
                }
            }

        }


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {

            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(texView: TextView, likes: Int) {
            texView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutess(texView: TextView, minutes: Int) {
            texView.text = minutes.toString()
        }

        @SuppressLint("ResourceAsColor", "UseCompatTextViewDrawableApis")
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: TextView, vegan: Boolean) {
            val color = view.context.getColor(R.color.green)
            if (vegan) {
                view.apply {
                    setTextColor(color)
                    compoundDrawableTintList = ColorStateList.valueOf(color)
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(texView: TextView, description: String?) {
            if (description != null) {
                val text = Jsoup.parse(description).text()
                texView.text = text
            }
        }

    }
}