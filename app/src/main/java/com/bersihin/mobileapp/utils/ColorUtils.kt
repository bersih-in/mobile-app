package com.bersihin.mobileapp.utils

import android.graphics.Color
import android.util.Log

fun getColorFromString(name: String): Int {
    var sum = 0

    for (i in name.indices) {
        sum += name[i].code * 10000
    }

    val hex = sum.toString(16).padStart(6, '0')

    Log.i("getColorFromString", "hex: $hex")
    return Color.parseColor("#FF$hex")
}