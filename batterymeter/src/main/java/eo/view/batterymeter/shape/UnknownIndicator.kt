package eo.view.batterymeter.shape

import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import eo.view.batterymeter.R
import eo.view.batterymeter.util.computeShapeBounds
import eo.view.batterymeter.util.scaledPoints

class UnknownIndicator(context: Context) {

    companion object {
        // question mark shape points, 3 points (x and y) for each cubicTo call
        private val shapePoints = scaledPoints(
            5.3f, 4.69f, 4.92f, 5.11f, 4.63f, 5.4f,
            4.15f, 5.88f, 3.8f, 6.55f, 3.8f, 7f,
            3.8f, 7f, 2.2f, 7f, 2.2f, 7f,
            2.2f, 6.17f, 2.66f, 5.48f, 3.13f, 5f,
            3.13f, 5f, 4.06f, 4.06f, 4.06f, 4.06f,
            4.33f, 3.79f, 4.5f, 3.41f, 4.5f, 3f,
            4.5f, 2.17f, 3.83f, 1.5f, 3f, 1.5f,
            2.17f, 1.5f, 1.5f, 2.17f, 1.5f, 3f,
            1.5f, 3f, 0f, 3f, 0f, 3f,
            0f, 1.34f, 1.34f, 0f, 3f, 0f,
            4.66f, 0f, 6f, 1.34f, 6f, 3f,
            6f, 3.66f, 5.73f, 4.26f, 5.3f, 4.69f
        )
    }

    private val shapeWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_unknown_indicator_width_ratio, 1, 1
    )

    private val shapeHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_unknown_indicator_height_ratio, 1, 1
    )

    private val shapeVerticalOffsetRatio = context.resources.getFraction(
        R.fraction.battery_meter_unknown_indicator_vertical_offset_ratio, 1, 1
    )

    private val upperPartHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_unknown_indicator_upper_part_height_ratio, 1, 1
    )

    private val lowerPartWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_unknown_indicator_lower_part_width_ratio, 1, 1
    )

    private val lowerPartHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_unknown_indicator_lower_part_height_ratio, 1, 1
    )

    private val shapeRect = Rect()
    private val upperPartRect = RectF()
    private val lowerPartRect = RectF()

    fun computePath(bounds: Rect, path: Path) {
        updateShapeRect(bounds)
        updatePath(path)
    }

    private fun updateShapeRect(bounds: Rect) {
        computeShapeBounds(
            bounds,
            shapeWidthRatio,
            shapeHeightRatio,
            shapeVerticalOffsetRatio,
            shapeRect
        )

        val upperPartRectHeight = bounds.height() * upperPartHeightRatio
        upperPartRect.set(
            shapeRect.left.toFloat(),
            shapeRect.top.toFloat(),
            shapeRect.right.toFloat(),
            shapeRect.top + upperPartRectHeight
        )

        val lowerPartRectWidth = bounds.width() * lowerPartWidthRatio
        val lowerPartRectHeight = bounds.height() * lowerPartHeightRatio
        val lowerPartLeft = bounds.left + (bounds.width() - lowerPartRectWidth) / 2
        lowerPartRect.set(
            lowerPartLeft,
            shapeRect.bottom - lowerPartRectHeight,
            lowerPartLeft + lowerPartRectWidth,
            shapeRect.bottom.toFloat()
        )
    }

    private fun updatePath(path: Path) {
        path.reset()
        path.addRect(lowerPartRect, Path.Direction.CW)

        path.moveTo(
            upperPartRect.left + shapePoints[0] * upperPartRect.width(),
            upperPartRect.top + shapePoints[1] * upperPartRect.height()
        )

        for (index in 0..(shapePoints.size - 6) step 6) {
            path.cubicTo(
                upperPartRect.left + shapePoints[index] * upperPartRect.width(),
                upperPartRect.top + shapePoints[index + 1] * upperPartRect.height(),
                upperPartRect.left + shapePoints[index + 2] * upperPartRect.width(),
                upperPartRect.top + shapePoints[index + 3] * upperPartRect.height(),
                upperPartRect.left + shapePoints[index + 4] * upperPartRect.width(),
                upperPartRect.top + shapePoints[index + 5] * upperPartRect.height()
            )
        }
    }
}