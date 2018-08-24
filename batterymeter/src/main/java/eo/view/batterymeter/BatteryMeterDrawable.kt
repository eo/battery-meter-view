package eo.view.batterymeter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.ColorUtils
import eo.view.batterymeter.util.getColorAttr
import java.io.ByteArrayInputStream
import java.io.DataInputStream


class BatteryMeterDrawable(
    private val context: Context,
    theme: BatteryMeter.Theme = BatteryMeter.Theme.SHARP
) : Drawable(), BatteryMeter {

    companion object {
        const val MINIMUM_CHARGE_LEVEL = 0
        const val MAXIMUM_CHARGE_LEVEL = 100

        const val BATTERY_COLOR_ALPHA = (0xFF * 0.3f).toInt()
        const val CRITICAL_CHARGE_LEVEL = 10
    }

    private val padding = Rect()

    private val batteryShapeBounds = Rect()
    private val batteryPath = Path()
    private val indicatorPath = Path()
    private val chargeLevelClipRect = RectF()

    private val intrinsicSize =
        context.resources.getDimensionPixelSize(R.dimen.battery_meter_intrinsic_size)

    private var aspectRatio: Float = 1f
        set(value) {
            if (value != field) {
                field = value
                updateBatteryShapeBounds()
            }
        }

    private lateinit var batteryShapeDataStream: DataInputStream
    private lateinit var alertIndicatorDataStream: DataInputStream
    private lateinit var chargingIndicatorDataStream: DataInputStream
    private lateinit var unknownIndicatorDataStream: DataInputStream

    private val batteryPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ColorUtils.setAlphaComponent(
            context.getColorAttr(android.R.attr.colorForeground),
            BATTERY_COLOR_ALPHA
        )
    }

    private val chargeLevelPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.getColorAttr(android.R.attr.colorForeground)
    }

    private val indicatorPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }

    override var theme = theme
        set(value) {
            if (value != field) {
                field = value
                loadThemeShapes()
                updateBatteryAndIndicatorPaths()
                invalidateSelf()
            }
        }

    override var chargeLevel: Int? = null
        set(value) {
            val newChargeLevel = value?.coerceIn(MINIMUM_CHARGE_LEVEL, MAXIMUM_CHARGE_LEVEL)
            if (newChargeLevel != field) {
                field = newChargeLevel
                updateBatteryAndIndicatorPaths()
                updateChargeLevelClipRect()
                invalidateSelf()
            }
        }

    override var isCharging: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                updateBatteryAndIndicatorPaths()
                invalidateSelf()
            }
        }

    override var criticalChargeLevel: Int? = CRITICAL_CHARGE_LEVEL
        set(value) {
            val newCriticalChargeLevel = value?.coerceIn(MINIMUM_CHARGE_LEVEL, MAXIMUM_CHARGE_LEVEL)
            if (newCriticalChargeLevel != field) {
                field = newCriticalChargeLevel
                updateBatteryAndIndicatorPaths()
                invalidateSelf()
            }
        }

    var batteryColor: Int
        get() = batteryPaint.color
        set(value) {
            batteryPaint.color = value
            invalidateSelf()
        }

    var chargeLevelColor: Int
        get() = chargeLevelPaint.color
        set(value) {
            chargeLevelPaint.color = value
            invalidateSelf()
        }

    var indicatorColor: Int
        get() = indicatorPaint.color
        set(value) {
            indicatorPaint.color = value
            invalidateSelf()
        }

    init {
        loadThemeShapes()
    }

    override fun getIntrinsicWidth() = if (aspectRatio < 1) {
        (intrinsicSize * aspectRatio).toInt()
    } else {
        intrinsicSize
    }

    override fun getIntrinsicHeight() = if (aspectRatio < 1) {
        intrinsicSize
    } else {
        (intrinsicSize / aspectRatio).toInt()
    }

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

        canvas.save()
        canvas.clipRect(chargeLevelClipRect)
        canvas.drawPath(batteryPath, chargeLevelPaint)
        canvas.restore()

        if (!indicatorPath.isEmpty) {
            canvas.drawPath(indicatorPath, indicatorPaint)
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
        indicatorPaint.colorFilter = colorFilter
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

        updateBatteryAndIndicatorPaths()
        updateChargeLevelClipRect()
    }

    private fun updateBatteryAndIndicatorPaths() {
        val currentLevel = chargeLevel
        val currentCriticalLevel = criticalChargeLevel

        performPathCommands(batteryShapeDataStream, batteryPath)

        indicatorPath.reset()
        if (currentLevel == null) {
            performPathCommands(unknownIndicatorDataStream, indicatorPath)
        } else if (isCharging) {
            performPathCommands(chargingIndicatorDataStream, indicatorPath)
        } else if (currentCriticalLevel != null && currentLevel <= currentCriticalLevel) {
            performPathCommands(alertIndicatorDataStream, indicatorPath)
        }

        if (!indicatorPath.isEmpty) {
            batteryPath.op(indicatorPath, Path.Op.DIFFERENCE)
        }
    }

    private fun updateChargeLevelClipRect() {
        val level = chargeLevel ?: MINIMUM_CHARGE_LEVEL
        chargeLevelClipRect.set(batteryShapeBounds)
        chargeLevelClipRect.top +=
                chargeLevelClipRect.height() * (1f - level.toFloat() / MAXIMUM_CHARGE_LEVEL)
    }

    private fun loadThemeShapes() {
        val rawResourceId = when (theme) {
            BatteryMeter.Theme.SHARP -> R.raw.battery_shapes_sharp
            BatteryMeter.Theme.ROUNDED -> R.raw.battery_shapes_rounded
        }

        val rawBytes = context.resources.openRawResource(rawResourceId).readBytes()
        DataInputStream(ByteArrayInputStream(rawBytes)).use { input ->
            val newAspectRatio = input.readFloat()

            // battery shape
            var currentShapeLength = input.readInt()
            var currentOffset = rawBytes.size - input.available()
            batteryShapeDataStream = DataInputStream(
                ByteArrayInputStream(rawBytes, currentOffset, currentShapeLength)
            )
            input.skipBytes(currentShapeLength)

            // alert indicator
            currentShapeLength = input.readInt()
            currentOffset = rawBytes.size - input.available()
            alertIndicatorDataStream = DataInputStream(
                ByteArrayInputStream(rawBytes, currentOffset, currentShapeLength)
            )
            input.skipBytes(currentShapeLength)

            // charging indicator
            currentShapeLength = input.readInt()
            currentOffset = rawBytes.size - input.available()
            chargingIndicatorDataStream = DataInputStream(
                ByteArrayInputStream(rawBytes, currentOffset, currentShapeLength)
            )
            input.skipBytes(currentShapeLength)

            // unknown indicator
            currentShapeLength = input.readInt()
            currentOffset = rawBytes.size - input.available()
            unknownIndicatorDataStream = DataInputStream(
                ByteArrayInputStream(rawBytes, currentOffset, currentShapeLength)
            )

            aspectRatio = newAspectRatio
        }
    }

    private fun performPathCommands(pathDataStream: DataInputStream, path: Path) {

        fun xRatioToCoordinate(xRatio: Float) =
            batteryShapeBounds.left + xRatio * batteryShapeBounds.width()

        fun yRatioToCoordinate(yRatio: Float) =
            batteryShapeBounds.top + yRatio * batteryShapeBounds.height()

        path.reset()
        pathDataStream.reset()

        while (pathDataStream.available() > 0) {
            val command = pathDataStream.readChar()

            when (command) {
                'C' -> {
                    path.cubicTo(
                        xRatioToCoordinate(pathDataStream.readFloat()),
                        yRatioToCoordinate(pathDataStream.readFloat()),
                        xRatioToCoordinate(pathDataStream.readFloat()),
                        yRatioToCoordinate(pathDataStream.readFloat()),
                        xRatioToCoordinate(pathDataStream.readFloat()),
                        yRatioToCoordinate(pathDataStream.readFloat())
                    )
                }
                'L' -> {
                    path.lineTo(
                        xRatioToCoordinate(pathDataStream.readFloat()),
                        yRatioToCoordinate(pathDataStream.readFloat())
                    )
                }
                'M' -> {
                    path.moveTo(
                        xRatioToCoordinate(pathDataStream.readFloat()),
                        yRatioToCoordinate(pathDataStream.readFloat())
                    )
                }
                'Z' -> path.close()
            }
        }
    }
}
