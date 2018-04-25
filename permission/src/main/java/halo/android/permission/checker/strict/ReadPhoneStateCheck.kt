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
import android.support.annotation.RequiresPermission

/**
 * Created by Lucio on 18/4/24.
 * 检查 日历 读权限
 */
class ReadPhoneStateCheck(ctx: Context) : BaseCheck(ctx) {

    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    override fun check(): Boolean = tryCheck {
        val service = ctx.getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
        return !service.deviceId.isNullOrEmpty() || !service.subscriberId.isNullOrEmpty()
    }

}
