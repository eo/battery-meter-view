package eo.view.batterymeter.sample.util

import android.content.Context
import android.util.DisplayMetrics


fun Context.dpToPx(dp: Float): Float {
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.pxToDp(px: Float): Float {
    return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}