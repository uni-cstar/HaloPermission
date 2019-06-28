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
import android.provider.ContactsContract

/**
 * Created by Lucio on 18/4/24.
 * 检查读取联系人权限
 */
class ReadContactsCheck(ctx: Context) : BaseCheck(ctx) {

    override fun check(): Boolean = tryCheck {
        val projection = arrayOf(ContactsContract.Data._ID, ContactsContract.Data.DATA1)
        val cursor = ctx.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null)
        return tryReadCursorData(cursor)
    }


}