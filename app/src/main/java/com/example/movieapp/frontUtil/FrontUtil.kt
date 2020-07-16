package com.example.movieapp.frontUtil

import android.app.Activity
import com.example.movieapp.R


/**
 * Created by Benjamin Vouillon on 13,July,2020
 */
fun isTablet(activity: Activity): Boolean {
    return activity.resources.getBoolean(R.bool.isTablet)
}

