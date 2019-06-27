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

package halo.android.v2.caller

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import halo.android.v2.SpecPermission
import halo.android.v2.common.PLog
import halo.android.v2.processor.PermissionResponder
import halo.android.v2.setting.SettingResponder
import halo.android.v2.spec.SpecialCaller
import halo.android.v2.spec.SpecialListener
import halo.android.v2.spec.SpecialPermission

/**
 * Created by Lucio on 2019/6/22.
 */


open class FragmentCaller(val fm: FragmentManager) : PermissionCaller {

    private val TAG = FragmentCaller::class.java.simpleName

    protected val callerFragment: PermissionFragment by lazy {
        var instance: PermissionFragment? = findPermissionFragment()
        if (instance == null) {
            instance = PermissionFragment()
            fm.beginTransaction().add(instance, TAG).commitNow()
        }
        instance!!
    }

    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager)

    constructor(fragment: Fragment) : this(fragment.childFragmentManager)

    private fun findPermissionFragment(): PermissionFragment? {
        return fm.findFragmentByTag(TAG) as? PermissionFragment
    }

    override fun requestPermission(responder: PermissionResponder, vararg permissions: String) {
        callerFragment.requestPermission(responder, *permissions)
    }

    override fun requestPermissionSetting(responder: SettingResponder, intent: Intent) {
        callerFragment.requestPermissionSetting(responder, intent)
    }
}

class PermissionFragment : Fragment(), PermissionCaller, SpecialCaller {


    private var mPermissionResponder: PermissionResponder? = null
    private val mPermissionRequestCode = 11

    private var mPermissionSettingResponder: SettingResponder? = null
    private val mPermissionSettingRequestCode = 12

    private var mSpecResponder: SpecialListener? = null
    private val mSpecRequestCode = 13
    private var mSpecPermission: SpecialPermission? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.retainInstance = true
    }

    override fun requestPermission(responder: PermissionResponder, vararg permissions: String) {
        mPermissionResponder = responder
        PLog.d("request permission from PermissionFragment:\n" + permissions.joinToString("\n"))
        requestPermissions(permissions, mPermissionRequestCode)
    }

    override fun requestPermissionSetting(responder: SettingResponder, intent: Intent) {
        mPermissionSettingResponder = responder
        PLog.d("request permission setting from PermissionFragment:\n$intent")
        startActivityForResult(intent, mPermissionSettingRequestCode)
    }

    override fun requestSpecialPermission(spec: SpecialPermission, listener: SpecialListener) {
        mSpecResponder = listener
        mSpecPermission = spec
        val intent = spec.newSettingIntent(requireContext())
        startActivityForResult(intent, mSpecRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == mPermissionRequestCode) {
            mPermissionResponder?.onPermissionResponderResult(permissions, grantResults)
            mPermissionResponder = null
        }
        PLog.d("request permission from PermissionFragment:\n" + permissions.joinToString("\n"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mPermissionSettingRequestCode) {
            mPermissionSettingResponder?.onSettingResponderResult(resultCode, data)
            mPermissionSettingResponder = null
        } else if (requestCode == mSpecRequestCode) {
            if (mSpecPermission != null && mSpecResponder != null) {
                if (SpecPermission.isGrand(requireContext(),mSpecPermission!!)) {
                    mSpecResponder?.onSpecialGrand(mSpecPermission!!)
                } else {
                    mSpecResponder?.onSpecialDeny(mSpecPermission!!)
                }
            }
            mSpecPermission = null
            mSpecResponder = null
        }
    }
}