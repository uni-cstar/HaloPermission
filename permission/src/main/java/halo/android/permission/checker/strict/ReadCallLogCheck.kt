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
import android.provider.CallLog
import android.support.annotation.RequiresPermission
import halo.android.v2.checker.StrictChecker

/**
 * Created by Lucio on 18/4/24.
 * 检查 读取通话记录 权限
 */
class ReadCallLogCheck(ctx: Context) : BaseCheck(ctx) {

    @RequiresPermission(StrictChecker.READ_CALL_LOG)
    override fun check(): Boolean = tryCheck {
        //        if (Build.VERSION.SDK_INT < 16)
//            return@tryCheck true
        val projection = arrayOf(CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE)
        val cursor = ctx.contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null)
        return tryReadCursorData(cursor)
    }
}
