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
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import halo.android.v2.processor.PermissionResponder
import halo.android.v2.setting.SettingResponder

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

