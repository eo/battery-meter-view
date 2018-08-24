package eo.view.batterymeter.sample

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import eo.view.batterymeter.BatteryMeter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupThemeSwitcher()
        setupChargeLevelControls()
        setupCriticalChargeLevelControls()
        setupChargingCheckBox()
    }

    private fun setupThemeSwitcher() {
        if (batteryMeter.theme == BatteryMeter.Theme.ROUNDED) {
            roundedThemeChip.isChecked = true
        } else {
            sharpThemeChip.isChecked = true
        }

        themeChipGroup.setOnCheckedChangeListener { _, chipId ->
            batteryMeter.theme = when (chipId) {
                R.id.roundedThemeChip -> BatteryMeter.Theme.ROUNDED
                else -> BatteryMeter.Theme.SHARP
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

            chargeLevelSeekBar.isVisible = isChecked
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

            criticalChargeLevelSeekBar.isVisible = isChecked
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
}
