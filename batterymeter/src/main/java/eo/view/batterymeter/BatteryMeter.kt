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

    // Colors
    var color: Int
    var indicatorColor: Int
    var chargingColor: Int?
    var criticalColor: Int?
    var unknownColor: Int?
    var backgroundColor: Int?
}