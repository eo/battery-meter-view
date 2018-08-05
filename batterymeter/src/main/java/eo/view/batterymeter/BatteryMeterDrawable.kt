package eo.view.batterymeter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.ColorUtils

class BatteryMeterDrawable(context: Context) : Drawable() {
    companion object {
        const val MINIMUM_CHARGE_LEVEL = 0
        const val MAXIMUM_CHARGE_LEVEL = 100
    }

    private val width = context.resources.getDimensionPixelSize(R.dimen.battery_meter_width)
    private val height = context.resources.getDimensionPixelSize(R.dimen.battery_meter_height)
    private val aspectRatio = width.toFloat() / height

    private val padding = Rect()

    private val buttonHeightRatio = context.resources.getFraction(
        R.fraction.battery_meter_button_height_ratio, 1, 1
    )

    private val buttonWidthRatio = context.resources.getFraction(
        R.fraction.battery_meter_button_width_ratio, 1, 1
    )

    private val framePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ColorUtils.setAlphaComponent(Color.BLACK, (0xFF * 0.3f).toInt())
    }

    private val chargePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val frameRect = Rect()
    private val buttonRect = RectF()
    private val bodyRect = RectF()
    private val framePath = Path()
    private val chargeClipRect = RectF()
    private val chargeClipPath = Path()
    private val chargePath = Path()


    var chargeLevel: Int? = null
        set(value) {
            field = value?.coerceIn(MINIMUM_CHARGE_LEVEL, MAXIMUM_CHARGE_LEVEL)
            updateChargePath()
            invalidateSelf()
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
            (frameRect.width() * buttonWidthRatio).toInt().toFloat(),
            (frameRect.height() * buttonHeightRatio).toInt().toFloat()
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
}
