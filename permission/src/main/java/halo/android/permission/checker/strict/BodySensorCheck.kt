/*
 * Copyright (C) 2018 Lucio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package halo.android.permission.checker.strict

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build

/**
 * Created by Lucio on 18/4/25.
 * 身体传感器权限
 */
class BodySensorCheck(ctx: Context) : BaseCheck(ctx), SensorEventListener {

    override fun check(): Boolean = tryCheck {
        if(Build.VERSION.SDK_INT < 20)
            return true

        val sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor != null) {
            //尝试绑定回调，再移除
            sensorManager.registerListener(this, heartRateSensor, 3)
            sensorManager.unregisterListener(this)
        }
        return true
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
    }
}
