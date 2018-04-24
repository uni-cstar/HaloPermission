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

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CallLog
import android.support.annotation.RequiresPermission
import halo.android.permission.common.Permissions

/**
 * Created by Lucio on 18/4/24.
 * 检查 写 通话记录 权限
 * 备注：API16以下默认返回true
 */
class WriteCallLogCheck(ctx: Context) : BaseCheck(ctx) {

    private val testNumber = "1"
    private val testDate = System.currentTimeMillis()

    @RequiresPermission(Permissions.WRITE_CALL_LOG)
    override fun check(): Boolean = tryCheck {
        val contentResolver: ContentResolver = ctx.contentResolver
        try {
            val content = ContentValues()
            content.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE)
            content.put(CallLog.Calls.NUMBER, testNumber)
            content.put(CallLog.Calls.DATE, testDate)
            content.put(CallLog.Calls.NEW, "0")
            val resourceUri = contentResolver.insert(CallLog.Calls.CONTENT_URI, content)
            return ContentUris.parseId(resourceUri) > 0
        } finally {
            contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", arrayOf(testNumber))
        }
    }
}
