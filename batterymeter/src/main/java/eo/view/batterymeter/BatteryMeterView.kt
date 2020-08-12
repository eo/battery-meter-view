package eo.view.batterymeter

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class BatteryMeterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.batteryMeterStyle
) : View(context, attrs, defStyleAttr), BatteryMeter {

    private val batteryMeterDrawable: BatteryMeterDrawable

    override var theme: BatteryMeter.Theme
        get() = batteryMeterDrawable.theme
        set(value) {
            if (theme != value) {
                batteryMeterDrawable.theme = value
                invalidate()
            }
        }

    override var chargeLevel: Int?
        get() = batteryMeterDrawable.chargeLevel
        set(value) {
            if (chargeLevel != value) {
                batteryMeterDrawable.chargeLevel = value
                invalidate()
            }
        }

    override var criticalChargeLevel: Int?
        get() = batteryMeterDrawable.criticalChargeLevel
        set(value) {
            if (criticalChargeLevel != value) {
                batteryMeterDrawable.criticalChargeLevel = value
                invalidate()
            }
        }

    override var isCharging: Boolean
        get() = batteryMeterDrawable.isCharging
        set(value) {
            if (isCharging != value) {
                batteryMeterDrawable.isCharging = value
                invalidate()
            }
        }

    override var color: Int
        get() = batteryMeterDrawable.color
        set(value) {
            if (color != value) {
                batteryMeterDrawable.color = value
                invalidate()
            }
        }

    override var indicatorColor: Int
        get() = batteryMeterDrawable.indicatorColor
        set(value) {
            if (indicatorColor != value) {
                batteryMeterDrawable.indicatorColor = value
                invalidate()
            }
        }

    override var chargingColor: Int?
        get() = batteryMeterDrawable.chargingColor
        set(value) {
            if (chargingColor != value) {
                batteryMeterDrawable.chargingColor = value
                invalidate()
            }
        }

    override var criticalColor: Int?
        get() = batteryMeterDrawable.criticalColor
        set(value) {
            if (criticalColor != value) {
                batteryMeterDrawable.criticalColor = value
                invalidate()
            }
        }

    override var unknownColor: Int?
        get() = batteryMeterDrawable.unknownColor
        set(value) {
            if (unknownColor != value) {
                batteryMeterDrawable.unknownColor = value
                invalidate()
            }
        }


    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BatteryMeterView,
            defStyleAttr,
            R.style.Widget_BatteryMeter
        )

        val themes = BatteryMeter.Theme.values()
        val themeIndex = typedArray.getInt(R.styleable.BatteryMeterView_batteryMeterTheme, 0)
            .coerceIn(0, themes.lastIndex)

        batteryMeterDrawable = BatteryMeterDrawable(context, themes[themeIndex])

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryMeterChargeLevel)) {
            chargeLevel = typedArray.getInt(R.styleable.BatteryMeterView_batteryMeterChargeLevel, 0)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryMeterCriticalChargeLevel)) {
            criticalChargeLevel = typedArray.getInt(
                R.styleable.BatteryMeterView_batteryMeterCriticalChargeLevel, 0
            )
        }

        isCharging =
            typedArray.getBoolean(R.styleable.BatteryMeterView_batteryMeterIsCharging, isCharging)

        color = typedArray.getColor(R.styleable.BatteryMeterView_batteryMeterColor, color)
        indicatorColor = typedArray.getColor(
            R.styleable.BatteryMeterView_batteryMeterIndicatorColor,
            indicatorColor
        )

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryMeterChargingColor)) {
            chargingColor =
                typedArray.getColor(R.styleable.BatteryMeterView_batteryMeterChargingColor, color)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryMeterCriticalColor)) {
            criticalColor =
                typedArray.getColor(R.styleable.BatteryMeterView_batteryMeterCriticalColor, color)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_batteryMeterUnknownColor)) {
            unknownColor =
                typedArray.getColor(R.styleable.BatteryMeterView_batteryMeterUnknownColor, color)
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

    fun animateToLevel(target: Int, from: Int = 0) {
        val diff = target - from
        val valueAnimator = ValueAnimator
            .ofInt(from, from + diff)
            .setDuration(1000)
        valueAnimator.addUpdateListener { animation ->
            chargeLevel = animation.animatedValue as Int
            invalidate()
        }
        valueAnimator.start()
    }
}