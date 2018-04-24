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
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.provider.CalendarContract
import android.support.annotation.RequiresPermission
import java.util.*

/**
 * Created by Lucio on 18/4/24.
 * 检查 日历 写权限
 */
class WriteCalendarCheck(ctx: Context) : BaseCheck(ctx) {

    private val name = "WriteCalendarCheck"
    private val account = "HaloPermission@github.com"
    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    override fun check(): Boolean = tryCheck {

        if (Build.VERSION.SDK_INT <= 14)
            return@tryCheck true
        try {
            val timeZone = TimeZone.getDefault()
            val value = ContentValues()
            value.put(CalendarContract.Calendars.NAME, name)
            value.put(CalendarContract.Calendars.ACCOUNT_NAME, account)
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, name)
            value.put(CalendarContract.Calendars.VISIBLE, 1)
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, name)
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)

            val insertUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, name)
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                    .build()
            val resourceUri = ctx.contentResolver.insert(insertUri, value)
            return ContentUris.parseId(resourceUri) > 0
        } finally {
            val deleteUri = CalendarContract.Calendars.CONTENT_URI.buildUpon().build()
            ctx.contentResolver.delete(deleteUri, CalendarContract.Calendars.ACCOUNT_NAME + "=?", arrayOf<String>(account))
        }
    }

}
