package eo.view.batterymeter.shape

import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import eo.view.batterymeter.R
import eo.view.batterymeter.util.computeShapeBounds
import eo.view.batterymeter.util.scaledPoints

class ChargingIndicator(context: Context) {

    companion object {
        // bolt shape points (x1, y1, x2, y2, ...)
        private val shapePoints = scaledPoints(
            0f, 7.5f, 2f, 7.5f, 2f, 13f, 6f, 5.5f, 4f, 5.5f, 4f, 0f
        )
    }

    private val shapeWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_charging_indicator_width_ratio, 1, 1
    )

    private val shapeHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_charging_indicator_height_ratio, 1, 1
    )

    private val shapeVerticalOffsetRatio = context.resources.getFraction(
        R.fraction.battery_meter_charging_indicator_vertical_offset_ratio, 1, 1
    )

    private val shapeRect = Rect()

    fun computePath(bounds: Rect, path: Path) {
        computeShapeBounds(
            bounds,
            shapeWidthRatio,
            shapeHeightRatio,
            shapeVerticalOffsetRatio,
            shapeRect
        )
        updatePath(path)
    }

    private fun updatePath(path: Path) {
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