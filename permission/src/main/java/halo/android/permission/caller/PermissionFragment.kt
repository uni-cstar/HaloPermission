/*
 * Copyright (C) 2019 Lucio
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

package halo.android.permission.caller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import halo.android.permission.common.PLog
import halo.android.permission.processor.PermissionResponder
import halo.android.permission.setting.SettingResponder
import halo.android.permission.spec.SpecPermission
import halo.android.permission.spec.SpecialCaller

class PermissionFragment : Fragment(), PermissionCaller, SpecialCaller {

    override val ctx: Context
        get() = this.requireContext()


    private var mPermissionResponder: PermissionResponder? = null
    private val mPermissionRequestCode = 11

    private var mPermissionSettingResponder: SettingResponder? = null
    private val mPermissionSettingRequestCode = 12

    private var mSpecPermission: SpecPermission? = null
    private val mSpecRequestCode = 13

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.retainInstance = true
    }

    override fun requestPermission(responder: PermissionResponder, vararg permissions: String) {
        mPermissionResponder = responder
        PLog.d("invokeRequest permission from PermissionFragment:\n" + permissions.joinToString("\n"))
        requestPermissions(permissions, mPermissionRequestCode)
    }

    override fun requestPermissionSetting(responder: SettingResponder, intent: Intent) {
        mPermissionSettingResponder = responder
        PLog.d("invokeRequest permission setting from PermissionFragment:\n$intent")
        startActivityForResult(intent, mPermissionSettingRequestCode)
    }

    override fun requestSpecialPermission(spec: SpecPermission) {
        mSpecPermission = spec
        startActivityForResult(spec.createSettingIntent(requireContext()), mSpecRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == mPermissionRequestCode) {
            mPermissionResponder?.onPermissionResponderResult(permissions, grantResults)
            mPermissionResponder = null
        }
        PLog.d("invokeRequest permission from PermissionFragment:\n" + permissions.joinToString("\n"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mPermissionSettingRequestCode) {
            mPermissionSettingResponder?.onSettingResponderResult(resultCode, data)
            mPermissionSettingResponder = null
        } else if (requestCode == mSpecRequestCode) {
            mSpecPermission?.apply {
                if(isGrand(requireContext())){
                    notifyGrand()
                }else{
                    notifyDeny()
                }
            }
            mSpecPermission = null
        }
    }
}