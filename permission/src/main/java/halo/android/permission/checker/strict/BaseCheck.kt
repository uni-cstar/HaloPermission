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
import android.database.Cursor


/**
 * Created by Lucio on 18/4/24.
 */

abstract class BaseCheck(val ctx: Context) {

    abstract fun check(): Boolean

    protected inline fun tryCheck(check: () -> Boolean): Boolean {
        return try {
            check()
        } catch (e: Exception) {
            false
        }
    }

    protected inline fun tryIgnore(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
        }
    }

    protected fun tryReadCursorData(cursor: Cursor?): Boolean {
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {//cursor is not empty,try read data
                    val columnIndex = 0
                    val type = cursor.getType(columnIndex)
                    when (type) {
                        Cursor.FIELD_TYPE_BLOB -> {
                            cursor.getBlob(columnIndex)
                        }
                        Cursor.FIELD_TYPE_NULL -> {
                            cursor.isNull(columnIndex)
                        }
                        Cursor.FIELD_TYPE_INTEGER, Cursor.FIELD_TYPE_FLOAT, Cursor.FIELD_TYPE_STRING -> {
                            cursor.getString(0)
                        }
                        else -> {
                            cursor.getString(0)
                        }
                    }
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false  // cause exception when read,regard as read failed
            } finally {
                if (!cursor.isClosed)
                    cursor.close()
            }
        } else {//cursor is null,regard as read failed
            return false
        }
    }
}