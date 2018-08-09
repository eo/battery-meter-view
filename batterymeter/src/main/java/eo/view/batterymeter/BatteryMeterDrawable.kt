package eo.view.batterymeter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.ColorUtils
import eo.view.batterymeter.shape.BatteryShape
import eo.view.batterymeter.shape.ChargingIndicator

class BatteryMeterDrawable(context: Context) : Drawable() {

    companion object {
        const val MINIMUM_CHARGE_LEVEL = 0
        const val MAXIMUM_CHARGE_LEVEL = 100
    }

    private val width = context.resources.getDimensionPixelSize(R.dimen.battery_meter_width)
    private val height = context.resources.getDimensionPixelSize(R.dimen.battery_meter_height)
    private val aspectRatio = width.toFloat() / height

    private val batteryShapeBounds = Rect()

    private val batteryShape = BatteryShape(context)
    private val chargingIndicator = ChargingIndicator(context)

    private val batteryPath = Path()
    private val chargeLevelPath = Path()
    private val chargeLevelClipRect = RectF()
    private val chargeLevelClipPath = Path()

    private val padding = Rect()

    private val batteryPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ColorUtils.setAlphaComponent(Color.BLACK, (0xFF * 0.3f).toInt())
    }

    private val chargeLevelPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val indicatorPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    var chargeLevel: Int? = null
        set(value) {
            val newChargeLevel = value?.coerceIn(MINIMUM_CHARGE_LEVEL, MAXIMUM_CHARGE_LEVEL)
            if (newChargeLevel != field) {
                field = newChargeLevel
                updateChargeLevelPath()
                invalidateSelf()
            }
        }

    var isCharging: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                updateBatteryPath()
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
        updateBatteryShapeBounds()
    }

    override fun onBoundsChange(bounds: Rect) {
        updateBatteryShapeBounds()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(batteryPath, batteryPaint)
        canvas.drawPath(chargeLevelPath, chargeLevelPaint)

        if (isCharging) {
            canvas.drawPath(chargingIndicator.path, indicatorPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter) {
        batteryPaint.colorFilter = colorFilter
        chargeLevelPaint.colorFilter = colorFilter
    }

    private fun updateBatteryShapeBounds() {
        if (bounds.isEmpty) return

        val availableWidth = bounds.width() - padding.left - padding.right
        val availableHeight = bounds.height() - padding.top - padding.bottom
        val availableAspectRatio = availableWidth.toFloat() / availableHeight

        if (availableAspectRatio > aspectRatio) {
            batteryShapeBounds.set(0, 0, (availableHeight * aspectRatio).toInt(), availableHeight)
        } else {
            batteryShapeBounds.set(0, 0, availableWidth, (availableWidth / aspectRatio).toInt())
        }

        batteryShapeBounds.offset(
            (availableWidth - batteryShapeBounds.width()) / 2,
            (availableHeight - batteryShapeBounds.height()) / 2
        )

        updateBatteryPath()
    }

    private fun updateBatteryPath() {
        batteryShape.bounds = batteryShapeBounds
        batteryPath.set(batteryShape.path)

        if (isCharging) {
            chargingIndicator.bounds = batteryShapeBounds
            batteryPath.op(chargingIndicator.path, Path.Op.DIFFERENCE)
        }

        updateChargeLevelPath()
    }

    private fun updateChargeLevelPath() {
        val level = chargeLevel ?: MINIMUM_CHARGE_LEVEL
        chargeLevelClipRect.set(batteryShapeBounds)
        chargeLevelClipRect.top +=
                chargeLevelClipRect.height() * (1f - level.toFloat() / MAXIMUM_CHARGE_LEVEL)

        chargeLevelClipPath.reset()
        chargeLevelClipPath.addRect(chargeLevelClipRect, Path.Direction.CW)

        chargeLevelPath.set(batteryPath)
        chargeLevelPath.op(chargeLevelClipPath, Path.Op.INTERSECT)
    }

}
