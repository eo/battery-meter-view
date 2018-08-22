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
            batteryMeterDrawable.theme = value
            invalidate()
        }

    override var chargeLevel: Int?
        get() = batteryMeterDrawable.chargeLevel
        set(value) {
            batteryMeterDrawable.chargeLevel = value
            invalidate()
        }

    override var criticalChargeLevel: Int?
        get() = batteryMeterDrawable.criticalChargeLevel
        set(value) {
            batteryMeterDrawable.criticalChargeLevel = value
            invalidate()
        }

    override var isCharging: Boolean
        get() = batteryMeterDrawable.isCharging
        set(value) {
            batteryMeterDrawable.isCharging = value
            invalidate()
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
            R.styleable.BatteryMeterView_battery_theme,
            BatteryMeter.Theme.SHARP.ordinal
        ).coerceIn(0, themes.lastIndex)

        batteryMeterDrawable = BatteryMeterDrawable(context, themes[themeIndex])

        if (typedArray.hasValue(R.styleable.BatteryMeterView_battery_chargeLevel)) {
            chargeLevel = typedArray.getInt(R.styleable.BatteryMeterView_battery_chargeLevel, 0)
        }

        if (typedArray.hasValue(R.styleable.BatteryMeterView_battery_criticalChargeLevel)) {
            criticalChargeLevel = typedArray.getInt(
                R.styleable.BatteryMeterView_battery_criticalChargeLevel, 0)
        }

        isCharging = typedArray.getBoolean(R.styleable.BatteryMeterView_battery_isCharging, isCharging)

        typedArray.recycle()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        batteryMeterDrawable.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        batteryMeterDrawable.setBounds(left, top, right, bottom);
    }
}