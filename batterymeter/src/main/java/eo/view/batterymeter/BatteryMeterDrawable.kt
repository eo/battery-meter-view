package eo.view.batterymeter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.core.graphics.ColorUtils
import kotlin.math.floor
import kotlin.math.max

class BatteryMeterDrawable(context: Context) : Drawable() {
    companion object {
        const val MINIMUM_CHARGE_LEVEL = 0
        const val MAXIMUM_CHARGE_LEVEL = 100
    }

    private val width = context.resources.getDimensionPixelSize(R.dimen.battery_meter_width)
    private val height = context.resources.getDimensionPixelSize(R.dimen.battery_meter_height)
    private val aspectRatio = width.toFloat() / height

    private val buttonHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_button_height_ratio, 1, 1
    )

    private val buttonWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_button_width_ratio, 1, 1
    )

    private val boltBodyWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_bolt_body_width_ratio, 1, 1
    )

    private val boltBodyHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_bolt_body_height_ratio, 1, 1
    )

    private val boltBodyVerticalOffsetRatio = context.resources.getFraction(
        R.fraction.battery_meter_bolt_body_vertical_offset_ratio, 1, 1
    )

    private val buttonRect = RectF()
    private val bodyRect = RectF()

    private val frameRect = Rect()
    private val framePath = Path()
    private val framePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ColorUtils.setAlphaComponent(Color.BLACK, (0xFF * 0.3f).toInt())
    }

    private val chargeClipRect = RectF()
    private val chargeClipPath = Path()
    private val chargePath = Path()
    private val chargePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val boltScaledPoints = getScaledPointsArray(context, R.array.battery_meter_bolt_points)
    private val boltRect = Rect()
    private val boltPath = Path()
    private val boltPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val padding = Rect()

    var chargeLevel: Int? = null
        set(value) {
            val newChargeLevel = value?.coerceIn(MINIMUM_CHARGE_LEVEL, MAXIMUM_CHARGE_LEVEL)
            if (newChargeLevel != field) {
                field = newChargeLevel
                updateChargePath()
                invalidateSelf()
            }
        }

    var isCharging: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                updateFramePath()
                invalidateSelf()
            }
        }

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    override fun getPadding(padding: Rect): Boolean {
        if (padding.left == 0 && padding.top == 0 && padding.right == 0 && padding.bottom == 0) {
            return super.getPadding(padding)
        }

        padding.set(this.padding)
        return true
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        padding.set(left, top, right, bottom)
        updateSize()
    }

    override fun onBoundsChange(bounds: Rect) {
        updateSize()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(framePath, framePaint)
        canvas.drawPath(chargePath, chargePaint)

        if (isCharging && boltPaint.color != Color.TRANSPARENT) {
            canvas.drawPath(boltPath, boltPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter) {
        framePaint.colorFilter = colorFilter
        chargePaint.colorFilter = colorFilter
    }

    private fun updateSize() {
        if (bounds.isEmpty) return

        val availableWidth = bounds.width() - padding.left - padding.right
        val availableHeight = bounds.height() - padding.top - padding.bottom
        val availableAspectRatio = availableWidth.toFloat() / availableHeight

        if (availableAspectRatio > aspectRatio) {
            frameRect.set(0, 0, (availableHeight * aspectRatio).toInt(), availableHeight)
        } else {
            frameRect.set(0, 0, availableWidth, (availableWidth / aspectRatio).toInt())
        }

        frameRect.offset(
            (availableWidth - frameRect.width()) / 2,
            (availableHeight - frameRect.height()) / 2
        )

        updateFramePath()
    }

    private fun updateFramePath() {
        buttonRect.set(
            0f,
            0f,
            floor(frameRect.width() * buttonWidthRatio),
            floor(frameRect.height() * buttonHeightRatio)
        )
        buttonRect.offset(
            frameRect.left + (frameRect.width() - buttonRect.width()) / 2,
            frameRect.top.toFloat()
        )

        bodyRect.set(
            frameRect.left.toFloat(),
            buttonRect.bottom,
            frameRect.right.toFloat(),
            frameRect.bottom.toFloat()
        )

        framePath.reset()
        framePath.addRect(buttonRect, Path.Direction.CW)
        framePath.addRect(bodyRect, Path.Direction.CW)

        if (isCharging) {
            updateBoltPath()
            framePath.op(boltPath, Path.Op.DIFFERENCE)
        }

        updateChargePath()
    }

    private fun updateChargePath() {
        val level = chargeLevel ?: MINIMUM_CHARGE_LEVEL
        chargeClipRect.set(frameRect)
        chargeClipRect.top += chargeClipRect.height() * (1f - level.toFloat() / MAXIMUM_CHARGE_LEVEL)

        chargeClipPath.reset()
        chargeClipPath.addRect(chargeClipRect, Path.Direction.CW)

        chargePath.set(framePath)
        chargePath.op(chargeClipPath, Path.Op.INTERSECT)
    }

    private fun updateBoltPath() {
        boltRect.set(
            0,
            0,
            (bodyRect.width() * boltBodyWidthRatio).toInt(),
            (bodyRect.height() * boltBodyHeightRatio).toInt()
        )
        boltRect.offset(
            (bodyRect.left + (bodyRect.width() - boltRect.width()) / 2).toInt(),
            (bodyRect.top + bodyRect.height() * boltBodyVerticalOffsetRatio).toInt()
        )

        boltPath.reset()
        boltPath.moveTo(
            boltRect.left + boltScaledPoints[0] * boltRect.width(),
            boltRect.top + boltScaledPoints[1] * boltRect.height()
        )
        for (index in 2..(boltScaledPoints.size - 2) step 2) {
            boltPath.lineTo(
                boltRect.left + boltScaledPoints[index] * boltRect.width(),
                boltRect.top + boltScaledPoints[index + 1] * boltRect.height()
            )
        }
        boltPath.close()
    }

    private fun getScaledPointsArray(context: Context, @ArrayRes arrayRes: Int): FloatArray {
        val pointsArray = context.resources.getIntArray(arrayRes)
        return scalePointsArray(pointsArray)
    }

    private fun scalePointsArray(pointsArray: IntArray): FloatArray {
        val scaledArray = FloatArray(pointsArray.size)

        var maxX = pointsArray[0]
        var maxY = pointsArray[1]

        for (index in 2..(pointsArray.size - 2) step 2) {
            maxX = max(maxX, pointsArray[index])
            maxY = max(maxY, pointsArray[index + 1])
        }

        for (index in 0..(pointsArray.size - 2) step 2) {
            scaledArray[index] = pointsArray[index] / maxX.toFloat()
            scaledArray[index + 1] = pointsArray[index + 1] / maxY.toFloat()
        }

        return scaledArray
    }
}
