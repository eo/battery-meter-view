package eo.view.batterymeter

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class BatteryMeterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), BatteryMeter {

    private val batteryMeterDrawable: BatteryMeterDrawable

    override var theme: BatteryMeter.Theme
        get() = batteryMeterDrawable.theme
        set(value) {
            if (value != theme) {
                batteryMeterDrawable.theme = value
                invalidate()
            }
        }

    override var chargeLevel: Int?
        get() = batteryMeterDrawable.chargeLevel
        set(value) {
            if (value != chargeLevel) {
                batteryMeterDrawable.chargeLevel = value
                invalidate()
            }
        }

    override var criticalChargeLevel: Int?
        get() = batteryMeterDrawable.criticalChargeLevel
        set(value) {
            if (value != criticalChargeLevel) {
                batteryMeterDrawable.criticalChargeLevel = value
                invalidate()
            }
        }

    override var isCharging: Boolean
        get() = batteryMeterDrawable.isCharging
        set(value) {
            if (value != isCharging) {
                batteryMeterDrawable.isCharging = value
                invalidate()
            }
        }

    override var color: Int
        get() = batteryMeterDrawable.color
        set(value) {
            if (value != color) {
                batteryMeterDrawable.color = value
                invalidate()
            }
        }

    override var indicatorColor: Int
        get() = batteryMeterDrawable.indicatorColor
        set(value) {
            if (value != indicatorColor) {
                batteryMeterDrawable.indicatorColor = value
                invalidate()
            }
        }

    override var chargingColor: Int?
        get() = batteryMeterDrawable.chargingColor
        set(value) {
            if (value != chargingColor) {
                batteryMeterDrawable.chargingColor = value
                invalidate()
            }
        }

    override var criticalColor: Int?
        get() = batteryMeterDrawable.criticalColor
        set(value) {
            if (value != criticalColor) {
                batteryMeterDrawable.criticalColor = value
                invalidate()
            }
        }

    override var unknownColor: Int?
        get() = batteryMeterDrawable.unknownColor
        set(value) {
            if (value != unknownColor) {
                batteryMeterDrawable.unknownColor = value
                invalidate()
            }
        }


    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BatteryMeterView,
            defStyleAttr,
            0
        )

        val themes = BatteryMeter.Theme.values()
        val themeIndex = typedArray.getInt(
            R.styleable.BatteryMeterView_batteryTheme,
            BatteryMeter.Theme.SHARP.ordinal
        ).coerceIn(0, themes.lastIndex)

        batteryMeterDrawable = BatteryMeterDrawable(context, themes[themeIndex])

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryChargeLevel)) {
            chargeLevel = typedArray.getInt(R.styleable.BatteryMeterView_batteryChargeLevel, 0)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryCriticalChargeLevel)) {
            criticalChargeLevel = typedArray.getInt(
                R.styleable.BatteryMeterView_batteryCriticalChargeLevel, 0
            )
        }

        isCharging =
                typedArray.getBoolean(R.styleable.BatteryMeterView_batteryIsCharging, isCharging)

        color = typedArray.getColor(R.styleable.BatteryMeterView_batteryColor, color)
        indicatorColor = typedArray.getColor(
            R.styleable.BatteryMeterView_batteryIndicatorColor,
            indicatorColor
        )

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryChargingColor)) {
            chargingColor =
                    typedArray.getColor(R.styleable.BatteryMeterView_batteryChargingColor, color)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryCriticalColor)) {
            criticalColor =
                    typedArray.getColor(R.styleable.BatteryMeterView_batteryCriticalColor, color)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryUnknownColor)) {
            unknownColor =
                    typedArray.getColor(R.styleable.BatteryMeterView_batteryUnknownColor, color)
        }

        typedArray.recycle()

        batteryMeterDrawable.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        batteryMeterDrawable.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        batteryMeterDrawable.setBounds(left, top, right, bottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)

        batteryMeterDrawable.setPadding(left, top, right, bottom)
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)

        when (layoutDirection) {
            LAYOUT_DIRECTION_RTL -> batteryMeterDrawable.setPadding(end, top, start, bottom)
            else -> batteryMeterDrawable.setPadding(start, top, end, bottom)
        }
    }
}