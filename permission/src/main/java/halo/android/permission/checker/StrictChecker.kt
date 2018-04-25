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

package halo.android.permission.checker

import android.Manifest
import android.content.Context
import android.os.Build
import halo.android.permission.checker.strict.*


/**
 * Created by Lucio on 18/4/12.
 * 严格权限校验
 */

class StrictChecker : PermissionChecker {

    override fun isPermissionGranted(ctx: Context, permission: String): Boolean {
        return Factory.create(ctx, permission).check()
    }

    internal companion object Factory {

        /**
         * API 16才有这两个变量
         */
        const val READ_CALL_LOG = "android.permission.READ_CALL_LOG"
        const val WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG"
        const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"

        /**
         * API 20才的变量
         */
        const val BODY_SENSORS = "android.permission.BODY_SENSORS"

        @JvmStatic
        fun create(ctx: Context, permission: String): BaseCheck {
            return when (permission) {
                Manifest.permission.READ_CONTACTS -> ReadContactsCheck(ctx)
                Manifest.permission.WRITE_CONTACTS -> WriteContactsCheck(ctx)
                READ_EXTERNAL_STORAGE -> ReadExternalStorageCheck(ctx)
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> WriteExternalStorageCheck(ctx)
                Manifest.permission.READ_CALENDAR -> ReadCalendarCheck(ctx)
                Manifest.permission.WRITE_CALENDAR -> WriteCalendarCheck(ctx)
                READ_CALL_LOG -> ReadCallLogCheck(ctx)
                WRITE_CALL_LOG -> WriteCallLogCheck(ctx)
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION -> LocationCheck(ctx)
                Manifest.permission.CAMERA -> {
                    if (Build.VERSION.SDK_INT >= 21) {
                        CameraCheck21(ctx)
                    } else {
                        CameraCheck(ctx)
                    }
                }
                Manifest.permission.RECORD_AUDIO -> RecordAudioCheck(ctx)
                BODY_SENSORS -> BodySensorCheck(ctx)
                Manifest.permission.READ_PHONE_STATE -> ReadPhoneStateCheck(ctx)
                Manifest.permission.READ_SMS -> ReadSMSCheck(ctx)
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_MMS,
                Manifest.permission.RECEIVE_SMS -> NormalCheck(ctx, permission)
                else -> NormalCheck(ctx, permission)
            }
        }
    }

}