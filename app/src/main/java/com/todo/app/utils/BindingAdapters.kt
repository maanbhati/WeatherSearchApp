package com.todo.app.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("weatherImage")
    fun loadImage(view: AppCompatImageView, url: String) = view.loadImage(url)
}