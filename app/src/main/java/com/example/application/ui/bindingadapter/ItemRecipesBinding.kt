package com.example.application.ui.bindingadapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.application.R

class ItemRecipesBinding {
    companion object {
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

        @SuppressLint("ResourceAsColor")
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: TextView, vegan: Boolean) {
            if (vegan) {
                view.apply {
                    setTextColor(ContextCompat.getColor(view.context, R.color.green))
//                    compoundDrawables[0].setTint(R.color.green)
                }
            }
        }
    }
}