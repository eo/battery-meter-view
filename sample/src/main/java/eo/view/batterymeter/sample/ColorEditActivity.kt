package eo.view.batterymeter.sample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import eo.view.batterymeter.BatteryMeter
import eo.view.colorinput.ColorInputView
import kotlinx.android.synthetic.main.activity_color_edit.*
import kotlinx.android.synthetic.main.include_battery_meters_with_indicator.*

class ColorEditActivity : AppCompatActivity(), ColorInputView.ColorChangeListener {

    companion object {
        const val EXTRA_SELECTED_COLOR = "selected_color"

        private const val EXTRA_COLOR_TYPE = "color_type"

        private const val EXTRA_THEME = "theme"
        private const val EXTRA_CHARGE_LEVEL = "charge_level"
        private const val EXTRA_CRITICAL_CHARGE_LEVEL = "critical_charge_level"
        private const val EXTRA_IS_CHARGING = "is_charging"

        private const val EXTRA_DEFAULT_COLOR = "default_color"
        private const val EXTRA_INDICATOR_COLOR = "indicator_color"
        private const val EXTRA_CHARGING_COLOR = "charging_color"
        private const val EXTRA_CRITICAL_COLOR = "critical_color"
        private const val EXTRA_UNKNOWN_COLOR = "unknown_color"

        fun newIntent(context: Context, batteryMeter: BatteryMeter, colorType: ColorType): Intent {
            return Intent(context, ColorEditActivity::class.java).apply {
                putExtra(EXTRA_COLOR_TYPE, colorType.ordinal)

                putExtra(EXTRA_THEME, batteryMeter.theme.ordinal)
                batteryMeter.chargeLevel?.let { putExtra(EXTRA_CHARGE_LEVEL, it) }
                batteryMeter.criticalChargeLevel?.let { putExtra(EXTRA_CRITICAL_CHARGE_LEVEL, it) }
                putExtra(EXTRA_IS_CHARGING, batteryMeter.isCharging)

                putExtra(EXTRA_DEFAULT_COLOR, batteryMeter.color)
                putExtra(EXTRA_INDICATOR_COLOR, batteryMeter.indicatorColor)
                batteryMeter.chargingColor?.let { putExtra(EXTRA_CHARGING_COLOR, it) }
                batteryMeter.criticalColor?.let { putExtra(EXTRA_CRITICAL_COLOR, it) }
                batteryMeter.unknownColor?.let { putExtra(EXTRA_UNKNOWN_COLOR, it) }
            }
        }
    }

    enum class ColorType {
        DEFAULT,
        INDICATOR,
        CHARGING,
        CRITICAL,
        UNKNOWN
    }

    private val colorType: ColorType by lazy {
        ColorType.values()
            .firstOrNull { it.ordinal == intent.getIntExtra(EXTRA_COLOR_TYPE, 0) }
            ?: ColorType.DEFAULT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_edit)

        setupActionBar()
        setupBatteryMeters()
        setupColorInput()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_color_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.menu_set -> {
            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_SELECTED_COLOR, colorInputView.color)
            })
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
            title = getString(when (colorType) {
                ColorType.DEFAULT -> R.string.battery_colors_default_color
                ColorType.INDICATOR -> R.string.battery_colors_indicator_color
                ColorType.CHARGING -> R.string.battery_colors_charging_color
                ColorType.CRITICAL -> R.string.battery_colors_critical_color
                ColorType.UNKNOWN -> R.string.battery_colors_unknown_color
            })
        }
    }

    private fun setupBatteryMeters() {
        when (colorType) {
            ColorType.INDICATOR ->  {
                setupIndicatorBatteryMeters()
                batteryMeter.isInvisible = true
                indicatorBatteryMeterGroup.isVisible = true
            }
            else -> {
                setupBattery(batteryMeter)
                indicatorBatteryMeterGroup.isVisible = false
                batteryMeter.isVisible = true
            }
        }
    }

    private fun setupIndicatorBatteryMeters() {
        with(chargingBatteryMeter) {
            setupBattery(this)
            chargeLevel = 70
            isCharging = true
        }

        with(criticalBatteryMeter) {
            setupBattery(this)
            isCharging = false
            criticalChargeLevel = 15
            chargeLevel = criticalChargeLevel
        }

        with(unknownBatteryMeter) {
            setupBattery(this)
            chargeLevel = null
        }
    }

    private fun setupBattery(battery: BatteryMeter) {
        with (intent) {
            battery.theme = when (getIntExtra(EXTRA_THEME, 0)) {
                BatteryMeter.Theme.ROUNDED.ordinal -> BatteryMeter.Theme.ROUNDED
                else -> BatteryMeter.Theme.SHARP
            }

            if (hasExtra(EXTRA_CHARGE_LEVEL)) {
                battery.chargeLevel = getIntExtra(EXTRA_CHARGE_LEVEL, 0)
            }

            if (hasExtra(EXTRA_CRITICAL_CHARGE_LEVEL)) {
                battery.criticalChargeLevel = getIntExtra(EXTRA_CRITICAL_CHARGE_LEVEL, 0)
            }

            battery.isCharging = getBooleanExtra(EXTRA_IS_CHARGING, false)

            battery.color = getIntExtra(EXTRA_DEFAULT_COLOR, Color.BLACK)
            battery.indicatorColor  = getIntExtra(EXTRA_INDICATOR_COLOR, Color.TRANSPARENT)

            if (hasExtra(EXTRA_CHARGING_COLOR)) {
                battery.chargingColor = getIntExtra(EXTRA_CHARGING_COLOR, Color.BLACK)
            }

            if (hasExtra(EXTRA_CRITICAL_COLOR)) {
                battery.criticalColor = getIntExtra(EXTRA_CRITICAL_COLOR, Color.BLACK)
            }

            if (hasExtra(EXTRA_UNKNOWN_COLOR)) {
                battery.unknownColor = getIntExtra(EXTRA_UNKNOWN_COLOR, Color.BLACK)
            }
        }
    }

    private fun setupColorInput() {
        colorInputView.color = when (colorType) {
            ColorType.DEFAULT -> batteryMeter.color
            ColorType.INDICATOR -> chargingBatteryMeter.indicatorColor
            ColorType.CHARGING -> batteryMeter.chargingColor ?: batteryMeter.color
            ColorType.CRITICAL -> batteryMeter.criticalColor ?: batteryMeter.color
            ColorType.UNKNOWN -> batteryMeter.unknownColor ?: batteryMeter.color
        }
        colorInputView.colorChangeListener = this
    }

    override fun onColorChanged(color: Int) {
        when (colorType) {
            ColorType.DEFAULT -> batteryMeter.color = color
            ColorType.INDICATOR -> {
                chargingBatteryMeter.indicatorColor = color
                criticalBatteryMeter.indicatorColor = color
                unknownBatteryMeter.indicatorColor = color
            }
            ColorType.CHARGING -> batteryMeter.chargingColor = color
            ColorType.CRITICAL -> batteryMeter.criticalColor = color
            ColorType.UNKNOWN -> batteryMeter.unknownColor = color
        }
    }
}