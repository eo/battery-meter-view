package eo.view.batterymeter.shape

import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import eo.view.batterymeter.R

class BatteryShape(context: Context) {

    private val buttonWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_button_width_ratio, 1, 1
    )

    private val buttonHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_button_height_ratio, 1, 1
    )

    private val buttonRect = RectF()
    private val bodyRect = RectF()

    val path = Path()

    var bounds = Rect()
        get() = Rect(field)
        set(value) {
            if (field != value) {
                field.set(value)
                updatePath()
            }
        }

    private fun updatePath() {
        val buttonWidth = bounds.width() * buttonWidthRatio
        val buttonHeight = bounds.height() * buttonHeightRatio
        val buttonLeft = bounds.left + (bounds.width() - buttonWidth) / 2
        val buttonTop = bounds.top.toFloat()

        buttonRect.set(buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight)
        bodyRect.set(
            bounds.left.toFloat(),
            buttonRect.bottom,
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )

        with(path) {
            reset()
            addRect(bodyRect, Path.Direction.CW)
            addRect(buttonRect, Path.Direction.CW)
        }
    }
}