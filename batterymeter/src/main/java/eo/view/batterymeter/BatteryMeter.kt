package eo.view.batterymeter

interface BatteryMeter {

    enum class Theme {
        ROUNDED,
        SHARP
    }

    var theme: Theme

    var chargeLevel: Int?

    var criticalChargeLevel: Int?

    var isCharging: Boolean

}