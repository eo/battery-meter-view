package eo.view.batterymeter.util

import android.content.Context
import android.graphics.Color
import kotlin.math.roundToInt


internal fun Context.getColorAttr(attr: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(attr))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()

    return color
}

fun Int.colorWithAlpha(alpha: Float): Int {
    require (alpha >= 0f || alpha <= 1f) {
        "alpha must be between 0 and 1."
    }

    val alphaComponent = (alpha * Color.alpha(this)).roundToInt()
    return (this and 0x00FFFFFF) or (alphaComponent shl 24)
}