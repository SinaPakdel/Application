package com.example.application.utils.view

import android.content.Context
import android.widget.ImageView
import coil.load
import com.bumptech.glide.Glide

enum class ImageHandler{
    GLIDE,COIL
}
class ImageLoader(private val context: Context) {

    fun loadImage(view: ImageView, url: String?, loader: ImageHandler) {
        when (loader) {
            ImageHandler.GLIDE -> Glide.with(context).load(url).into(view)
            ImageHandler.COIL -> view.load(url)
        }
    }
}