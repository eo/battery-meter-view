package eo.view.batterymeter.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt


@ColorInt
fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(attr))
    val colorAccent = typedArray.getColor(0, 0)
    typedArray.recycle()

    return colorAccent
}