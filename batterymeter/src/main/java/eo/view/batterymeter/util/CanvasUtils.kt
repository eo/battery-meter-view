package eo.view.batterymeter.util

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Region
import android.os.Build

/**
 * Wrap the specified [block] in calls to [Canvas.save]
 * and [Canvas.restoreToCount].
 */
inline fun Canvas.withSave(block: Canvas.() -> Unit) {
    val checkpoint = save()
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}

internal fun Canvas.clipOutPathCompat(path: Path) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        clipOutPath(path)
    } else {
        @Suppress("DEPRECATION")
        clipPath(path, Region.Op.DIFFERENCE)
    }
}