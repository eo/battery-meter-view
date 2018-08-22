package eo.view.batterymeter.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eo.view.batterymeter.BatteryMeter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batteryMeter.setOnClickListener {
            batteryMeter.theme = when (batteryMeter.theme) {
                BatteryMeter.Theme.SHARP -> BatteryMeter.Theme.ROUNDED
                BatteryMeter.Theme.ROUNDED -> BatteryMeter.Theme.SHARP
            }
        }
    }
}
