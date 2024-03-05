package com.vijaydhoni.quicknotes.util

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun setStatusBarColour(activity: AppCompatActivity?, colourId: Int) {
    activity?.window?.statusBarColor = ContextCompat.getColor(
        activity!!,
        colourId
    )
}