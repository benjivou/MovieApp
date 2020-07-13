package com.example.movieapp.frontUtil

import android.app.Activity
import android.util.DisplayMetrics
import kotlin.math.sqrt


/**
 * Created by Benjamin Vouillon on 13,July,2020
 */
fun isTablet(activity: Activity): Boolean {

    return getScreenSizeInches(activity) > 7
}

fun getScreenSizeInches(activity: Activity): Double {
    val windowManager = activity.windowManager
    val display = windowManager.defaultDisplay
    val displayMetrics = DisplayMetrics()
    display.getMetrics(displayMetrics)

    val dm = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(dm)
    val x = Math.pow(displayMetrics.widthPixels / dm.xdpi.toDouble(), 2.0)
    val y = Math.pow(displayMetrics.heightPixels / dm.ydpi.toDouble(), 2.0)
    return sqrt(x + y)
}