package com.jakode.covid19.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter

data class Slider(
    val icon: Int,
    val title: String,
    val description: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("setImage")
        fun setImage(imageView: ImageView, src: Int) {
            imageView.setImageResource(src)
        }
    }
}