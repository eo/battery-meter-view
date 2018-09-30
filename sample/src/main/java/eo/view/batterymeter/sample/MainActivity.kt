package eo.view.batterymeter.sample

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.forEach
import androidx.core.view.isVisible
import eo.view.batterymeter.BatteryMeter
import eo.view.batterymeter.BatteryMeterDrawable
import eo.view.batterymeter.sample.util.dpToPx
import eo.view.batterymeter.sample.util.pxToDp
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBatteryMeter()
        setupThemeSwitcher()
        setupChargeLevelControls()
        setupCriticalChargeLevelControls()
        setupChargingCheckBox()

        constraintLayout.doOnLayout { _ ->
            setupHeightSpinner()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // no-op
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)

        val iconPadding = Rect(
            dpToPx(7f).toInt(), dpToPx(2f).toInt(),
            dpToPx(7f).toInt(), dpToPx(2f).toInt()
        )

        menu.findItem(R.id.menu_colors).subMenu.forEach { colorMenuItem ->
            if (colorMenuItem.itemId == R.id.menu_indicator_color) return@forEach

            val iconDrawable = BatteryMeterDrawable(this).apply {
                setPadding(iconPadding.left, iconPadding.top, iconPadding.right, iconPadding.bottom)
            }

            when (colorMenuItem.itemId) {
                R.id.menu_default_color -> {
                    iconDrawable.chargeLevel = 60
                }
                R.id.menu_charging_color -> {
                    iconDrawable.chargeLevel = 70
                    iconDrawable.isCharging = true
                }
                R.id.menu_critical_color -> {
                    iconDrawable.chargeLevel = iconDrawable.criticalChargeLevel
                }
                R.id.menu_unknown_color -> {
                    iconDrawable.chargeLevel = null
                }
            }

            colorMenuItem.icon = iconDrawable
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_colors -> {
            item.subMenu.forEach { colorMenuItem ->
                (colorMenuItem.icon as? BatteryMeterDrawable)?.let { batteryMeterDrawable ->
                    batteryMeterDrawable.theme = batteryMeter.theme

                    when (colorMenuItem.itemId) {
                        R.id.menu_default_color -> {
                            batteryMeterDrawable.color = batteryMeter.color
                        }
                        R.id.menu_charging_color -> {
                            batteryMeterDrawable.chargingColor = batteryMeter.chargingColor ?:
                                    batteryMeter.color
                        }
                        R.id.menu_critical_color -> {
                            batteryMeterDrawable.criticalColor = batteryMeter.criticalColor ?:
                                    batteryMeter.color
                        }
                        R.id.menu_unknown_color -> {
                            batteryMeterDrawable.unknownColor = batteryMeter.unknownColor ?:
                                    batteryMeter.color
                        }
                    }
                }
            }

            true
        }

        R.id.menu_default_color -> {
            openColorPicker(item.icon as BatteryMeter, ColorEditActivity.ColorType.DEFAULT)
            true
        }

        R.id.menu_charging_color -> {
            openColorPicker(item.icon as BatteryMeter, ColorEditActivity.ColorType.CHARGING)
            true
        }

        R.id.menu_critical_color -> {
            openColorPicker(item.icon as BatteryMeter, ColorEditActivity.ColorType.CRITICAL)
            true
        }

        R.id.menu_unknown_color -> {
            openColorPicker(item.icon as BatteryMeter, ColorEditActivity.ColorType.UNKNOWN)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun setupBatteryMeter() {
        with(batteryMeter) {
            theme = BatteryMeter.Theme.ROUNDED
            chargeLevel = 70
            criticalChargeLevel = 15
            isCharging = true
        }
    }

    private fun setupThemeSwitcher() {
        if (batteryMeter.theme == BatteryMeter.Theme.ROUNDED) {
            roundedThemeChip.isChecked = true
        } else {
            sharpThemeChip.isChecked = true
        }

        themeChipGroup.setOnCheckedChangeListener { _, chipId ->
            when (chipId) {
                R.id.roundedThemeChip -> batteryMeter.theme = BatteryMeter.Theme.ROUNDED
                R.id.sharpThemeChip -> batteryMeter.theme = BatteryMeter.Theme.SHARP
                else -> {
                    if (batteryMeter.theme == BatteryMeter.Theme.ROUNDED) {
                        roundedThemeChip.isChecked = true
                    } else {
                        sharpThemeChip.isChecked = true
                    }
                }
            }
        }
    }

    private fun setupChargeLevelControls() {
        chargeLevelCheckBox.setOnCheckedChangeListener { _, isChecked ->
            batteryMeter.chargeLevel = if (isChecked) {
                chargeLevelSeekBar.progress
            } else {
                null
            }

            chargeLevelValueGroup.isVisible = isChecked
            criticalChargeLevelCheckBox.isVisible = isChecked
            criticalChargeLevelValueGroup.isVisible = isChecked &&
                    criticalChargeLevelCheckBox.isChecked
            chargingCheckBox.isVisible = isChecked
        }

        chargeLevelSeekBar.progress = batteryMeter.chargeLevel ?: 0
        chargeLevelValueText.text =
                getString(R.string.battery_charge_level_value, chargeLevelSeekBar.progress)

        chargeLevelSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                batteryMeter.chargeLevel = progress
                chargeLevelValueText.text = getString(R.string.battery_charge_level_value, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // no-op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // no-op
            }
        })
    }

    private fun setupCriticalChargeLevelControls() {
        criticalChargeLevelCheckBox.setOnCheckedChangeListener { _, isChecked ->
            batteryMeter.criticalChargeLevel = if (isChecked) {
                criticalChargeLevelSeekBar.progress
            } else {
                null
            }

            criticalChargeLevelValueGroup.isVisible = isChecked
        }

        criticalChargeLevelSeekBar.progress = batteryMeter.criticalChargeLevel ?: 0
        criticalChargeLevelValueText.text =
                getString(R.string.battery_charge_level_value, criticalChargeLevelSeekBar.progress)

        criticalChargeLevelSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                batteryMeter.criticalChargeLevel = progress
                criticalChargeLevelValueText.text =
                        getString(R.string.battery_charge_level_value, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // no-op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // no-op
            }
        })
    }

    private fun setupChargingCheckBox() {
        chargingCheckBox.isChecked = batteryMeter.isCharging

        chargingCheckBox.setOnCheckedChangeListener { _, isCharging ->
            batteryMeter.isCharging = isCharging
        }
    }

    private fun setupHeightSpinner() {
        val currentHeight = pxToDp(batteryMeter.height.toFloat()).toInt()
        val heights = (20..currentHeight step 10).toList()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, heights)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        heightSpinner.adapter = arrayAdapter
        heightSpinner.setSelection(heights.lastIndex)

        heightSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val batteryMeterLayoutParams = batteryMeter.layoutParams
                batteryMeterLayoutParams.height = dpToPx(heights[pos].toFloat()).toInt()
                batteryMeter.layoutParams = batteryMeterLayoutParams
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // no-op
            }
        }
    }

    private fun openColorPicker(batteryMeter: BatteryMeter, colorType: ColorEditActivity.ColorType) {
        startActivityForResult(
            ColorEditActivity.newIntent(this, batteryMeter, colorType),
            colorType.ordinal
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val color = data?.getIntExtra(ColorEditActivity.EXTRA_SELECTED_COLOR, Color.BLACK)
                ?: Color.BLACK

            when (requestCode) {
                ColorEditActivity.ColorType.DEFAULT.ordinal -> {
                    batteryMeter.color = color
                }

                ColorEditActivity.ColorType.INDICATOR.ordinal -> {
                    batteryMeter.indicatorColor = color
                }

                ColorEditActivity.ColorType.CHARGING.ordinal -> {
                    batteryMeter.chargingColor = color
                }

                ColorEditActivity.ColorType.CRITICAL.ordinal -> {
                    batteryMeter.criticalColor = color
                }

                ColorEditActivity.ColorType.UNKNOWN.ordinal -> {
                    batteryMeter.unknownColor = color
                }
            }
        }
    }
}
