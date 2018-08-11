package eo.view.batterymeter.shape

import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import eo.view.batterymeter.R

class AlertIndicator(context: Context) {

    private val shapeWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_alert_indicator_width_ratio, 1, 1
    )

    private val shapeHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_alert_indicator_height_ratio, 1, 1
    )

    private val shapeVerticalOffsetRatio = context.resources.getFraction(
        R.fraction.battery_meter_alert_indicator_vertical_offset_ratio, 1, 1
    )

    private val upperPartHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_alert_indicator_upper_part_height_ratio, 1, 1
    )

    private val lowerPartHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_alert_indicator_lower_part_height_ratio, 1, 1
    )

    private val shapeRect = Rect()
    private val upperPartRect = RectF()
    private val lowerPartRect = RectF()

    fun computePath(bounds: Rect, path: Path) {
        updateShapeRect(bounds)
        updatePath(path)
    }

    private fun updateShapeRect(bounds: Rect) {
        shapeRect.set(
            0,
            0,
            (bounds.width() * shapeWidthRatio).toInt(),
            (bounds.height() * shapeHeightRatio).toInt()
        )
        shapeRect.offset(
            (bounds.left + (bounds.width() - shapeRect.width()) / 2),
            (bounds.top + bounds.height() * shapeVerticalOffsetRatio).toInt()
        )

        val upperPartRectHeight = bounds.height() * upperPartHeightRatio
        upperPartRect.set(
            shapeRect.left.toFloat(),
            shapeRect.top.toFloat(),
            shapeRect.right.toFloat(),
            shapeRect.top + upperPartRectHeight
        )

        val lowerPartRectHeight = bounds.height() * lowerPartHeightRatio
        lowerPartRect.set(
            shapeRect.left.toFloat(),
            shapeRect.bottom - lowerPartRectHeight,
            shapeRect.right.toFloat(),
            shapeRect.bottom.toFloat()
        )

    }

    private fun updatePath(path: Path) {
        path.reset()
        path.addRect(upperPartRect, Path.Direction.CW)
        path.addRect(lowerPartRect, Path.Direction.CW)
    }

}