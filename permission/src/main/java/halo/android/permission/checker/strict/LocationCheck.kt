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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.annotation.RequiresPermission

/**
 * Created by Lucio on 18/4/25.
 * 定位权限
 */
class LocationCheck(ctx: Context) : BaseCheck(ctx) {

    @RequiresPermission(anyOf = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION))
    override fun check(): Boolean = tryCheck {
        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val list = locationManager.getProviders(true)

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            return true
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            return true
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f,
                    @SuppressLint("MissingPermission")
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            locationManager.removeUpdates(this)
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                            locationManager.removeUpdates(this)
                        }

                        override fun onProviderEnabled(provider: String?) {
                            locationManager.removeUpdates(this)
                        }

                        override fun onProviderDisabled(provider: String?) {
                            locationManager.removeUpdates(this)
                        }

                    })
        }
        return true
    }

}
