package eo.view.batterymeter.sample

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import eo.view.batterymeter.BatteryMeter
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
        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

        val currentHeight = pxToDp(batteryMeter.height)
        val heights = (20..currentHeight step 10).toList()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, heights)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        heightSpinner.adapter = arrayAdapter
        heightSpinner.setSelection(heights.lastIndex)

        heightSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val batteryMeterLayoutParams = batteryMeter.layoutParams
                batteryMeterLayoutParams.height = dpToPx(heights[pos])
                batteryMeter.layoutParams = batteryMeterLayoutParams
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // no-op
            }
        }
    }
}
