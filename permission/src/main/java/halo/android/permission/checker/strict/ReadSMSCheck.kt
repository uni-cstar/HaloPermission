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
import android.os.Build
import android.provider.Telephony

/**
 * Created by Lucio on 18/4/24.
 * 检测 短信 是否可读
 * 备注：在API19以前，默认返回true
 */
class ReadSMSCheck(ctx: Context) : BaseCheck(ctx) {

    override fun check(): Boolean = tryCheck {
        if (Build.VERSION.SDK_INT >= 19) {
            val projection = arrayOf(Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.PERSON, Telephony.Sms.BODY)
            val cursor = ctx.contentResolver.query(Telephony.Sms.CONTENT_URI, projection, null, null, null)
            return tryReadCursorData(cursor)
        } else {
            return true
        }
    }


}
