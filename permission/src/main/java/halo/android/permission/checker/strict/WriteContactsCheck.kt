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

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract


/**
 * Created by Lucio on 18/4/24.
 * 检查读取联系人权限
 */
class WriteContactsCheck(ctx: Context) : BaseCheck(ctx) {

    override fun check(): Boolean = tryCheck {
        //在联系人表中插入一条数据，再删除数据，如果成功则认为有权限
        var rawContactId: Long = -1
        var dataId: Long = -1
        val contentResolver = ctx.contentResolver
        try {
            //在ContactsContract.RawContacts.CONTENT_URI中插入一条数据
            val values = ContentValues()
            val rawContractUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
            rawContactId = ContentUris.parseId(rawContractUri)

            //在ContactsContract.Data.CONTENT_URI中插入一条数据
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "WriteContactsCheck");
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1")
            val dataUri = contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
            dataId = ContentUris.parseId(dataUri)

            return true
        } finally {
            try {//预防脏数据
                //删除[ContactsContract.RawContacts.CONTENT_URI]中新增的数据
                if (rawContactId > 0) {
                    contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=?", arrayOf(rawContactId.toString()))
                }
            } catch (e: Exception) {
            }

            try {//预防脏数据
                //删除[ContactsContract.Data.CONTENT_URI]中新增的数据
                if (dataId > 0)
                    contentResolver.delete(ContactsContract.Data.CONTENT_URI, ContactsContract.Data._ID + "=?", arrayOf(dataId.toString()))
            } catch (e: Exception) {
            }
        }
    }

}
