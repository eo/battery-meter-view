# Battery Meter View
![Icon](/sample/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

Material design battery meter view for Android

[ ![Download](https://api.bintray.com/packages/eo/view/batterymeter/images/download.svg) ](https://bintray.com/eo/view/batterymeter/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://github.com/eo/battery-meter-view/blob/master/LICENSE)
[![License](https://img.shields.io/badge/minSdkVersion-19-red.svg)](https://developer.android.com/about/dashboards/)

Download
--------

```groovy
dependencies {
  implementation 'eo.view:batterymeter:2.0.0'
}
```

Usage
-----
Library contains both `BatteryMeterView` and `BatteryMeterDrawable` classes. Following XML attributes have corresponding class properties.

```xml
<eo.view.batterymeter.BatteryMeterView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:batteryMeterChargeLevel="80"
    app:batteryMeterChargingColor="#4caf50"
    app:batteryMeterColor="#0277bd"
    app:batteryMeterCriticalChargeLevel="15"
    app:batteryMeterCriticalColor="#d84315"
    app:batteryMeterIndicatorColor="@android:color/transparent"
    app:batteryMeterIsCharging="true"
    app:batteryMeterTheme="rounded"
    app:batteryMeterUnknownColor="#e0e0e0" />
```
If you want the battery indicator to animate to a charge level, you can call the method `animateToLevel(target)`, where **target** is the intended charge level. Optionally, you can pass a second parameter to this method if you want the animation to start from a different start level.

Themes & States
---------------
|   | Unknown | Default | Charging | Critical
| - | ------- | ------- | -------- | --------
**Rounded** | ![Rounded Unknown](/images/rounded_unknown.png) | ![Rounded Default](/images/rounded_default.png) | ![Rounded Charging](/images/rounded_charging.png) | ![Rounded Critical](/images/rounded_critical.png)
**Sharp** | ![Sharp Unknown](/images/sharp_unknown.png) | ![Sharp Default](/images/sharp_default.png) | ![Sharp Charging](/images/sharp_charging.png) | ![Sharp Critical](/images/sharp_critical.png)

Style
-----
Battery meter view is styleable using `batteryMeterStyle` in your theme. `Widget.BatteryMeter` can be used as a base style.

Sample
------
Download sample app under releases to play with the library

![Sample First Screenshot](/images/screenshot_sample_1.png)
![Sample Second Screenshot](/images/screenshot_sample_2.png)


License
-------

    Copyright 2018 Erdem Orman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
