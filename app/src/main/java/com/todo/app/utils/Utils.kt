package com.todo.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.todo.networkmodule.api.Api.Companion.FORECAST_ICON
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.loadImage(url: String) {
    val imageUrl = FORECAST_ICON + url
    Glide
        .with(this)
        .load(imageUrl)
        .centerCrop() // or other transformation: fitCenter(), circleCrop(), etc
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(android.R.drawable.arrow_up_float)
        .transform(RoundedCorners(1))
        .into(this)
}

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

/**
 * Return date in specified format.
 * @param milliSeconds Date in milliseconds
 * @return String representing date in specified format
 */
fun getDate(milliSeconds: Long): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat("EEEE, dd/MM/YYYY", Locale.UK)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds * 1000
    return formatter.format(calendar.time)
}