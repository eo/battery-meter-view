package eo.view.batterymeter.shape

import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import eo.view.batterymeter.R

class ChargingIndicator(context: Context) {

    companion object {
        // bolt shape points (x1, y1, x2, y2, ...)
        private val shapePoints = scaledPoints(0, 15, 4, 15, 4, 26, 12, 11, 8, 11, 8, 0)
    }

    private val shapeToBoundsWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_charging_indicator_to_bounds_width_ratio, 1, 1
    )

    private val shapeToBoundsHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_charging_indicator_to_bounds_height_ratio, 1, 1
    )

    private val shapeToBoundsVerticalOffsetRatio = context.resources.getFraction(
        R.fraction.battery_meter_charging_indicator_to_bounds_vertical_offset_ratio, 1, 1
    )

    private val shapeRect = Rect()

    val path = Path()

    var bounds = Rect()
        get() = Rect(field)
        set(value) {
            if (field != value) {
                field.set(value)
                updateShapeRect()
            }
        }

    private fun updateShapeRect() {
        shapeRect.set(
            0,
            0,
            (bounds.width() * shapeToBoundsWidthRatio).toInt(),
            (bounds.height() * shapeToBoundsHeightRatio).toInt()
        )
        shapeRect.offset(
            (bounds.left + (bounds.width() - shapeRect.width()) / 2),
            (bounds.top + bounds.height() * shapeToBoundsVerticalOffsetRatio).toInt()
        )

        updatePath()
    }

    private fun updatePath() {
        path.reset()
        path.moveTo(
            shapeRect.left + shapePoints[0] * shapeRect.width(),
            shapeRect.top + shapePoints[1] * shapeRect.height()
        )
        for (index in 2..(shapePoints.size - 2) step 2) {
            path.lineTo(
                shapeRect.left + shapePoints[index] * shapeRect.width(),
                shapeRect.top + shapePoints[index + 1] * shapeRect.height()
            )
        }
        path.close()
    }
}