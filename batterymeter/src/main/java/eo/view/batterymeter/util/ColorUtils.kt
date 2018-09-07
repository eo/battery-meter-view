package eo.view.batterymeter.util

import android.content.Context


internal fun Context.getColorAttr(attr: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(attr))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()

    return color
}

/**
 * Set the alpha component of a color to be `alpha`.
 */
fun Int.colorWithAlpha(alpha: Int): Int {
    require (alpha >= 0 || alpha <= 0xFF) {
        "alpha must be between 0 and 255."
    }
    return (this and 0x00FFFFFF) or (alpha shl 24)
}