# battery-meter-view
Material design battery meter view for Android

[ ![Download](https://api.bintray.com/packages/eo/view/batterymeter/images/download.svg) ](https://bintray.com/eo/view/batterymeter/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://github.com/eo/battery-meter-view/blob/master/LICENSE)
[![License](https://img.shields.io/badge/minSdkVersion-19-red.svg)](https://developer.android.com/about/dashboards/)

Download
--------

```groovy
dependencies {
  implementation 'eo.view:batterymeter:1.1.1'
}
```

Usage
-----
Library contains both `BatteryMeterView` and `BatteryMeterDrawable` classes. Following XML attributes have corresponding class properties.

```xml
<eo.view.batterymeter.BatteryMeterView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:batteryChargeLevel="80"
    app:batteryChargingColor="#4caf50"
    app:batteryColor="#0277bd"
    app:batteryCriticalChargeLevel="15"
    app:batteryCriticalColor="#d84315"
    app:batteryIndicatorColor="@android:color/transparent"
    app:batteryIsCharging="true"
    app:batteryTheme="rounded"
    app:batteryUnknownColor="#e0e0e0" />
```

Themes & States
---------------
|   | Unknown | Default | Charging | Critical
| - | ------- | ------- | -------- | --------
**Rounded** | ![Rounded Unknown](/images/rounded_unknown.png) | ![Rounded Default](/images/rounded_default.png) | ![Rounded Charging](/images/rounded_charging.png) | ![Rounded Critical](/images/rounded_critical.png)
**Sharp** | ![Sharp Unknown](/images/sharp_unknown.png) | ![Sharp Default](/images/sharp_default.png) | ![Sharp Charging](/images/sharp_charging.png) | ![Sharp Critical](/images/sharp_critical.png)

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
