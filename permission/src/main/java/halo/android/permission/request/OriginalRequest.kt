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

package halo.android.permission.request

import android.app.Activity
import android.app.Fragment
import halo.android.permission.HaloPermission
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.checker.PermissionChecker
import halo.android.permission.context.ActivityContext
import halo.android.permission.context.FragmentContext
import halo.android.permission.context.SupportFragmentContext

/**
 * Created by Lucio on 18/4/4.
 */
class OriginalRequest : BaseRequest {

    private lateinit var mCaller: PermissionCaller

    constructor(activity: Activity, requestCode: Int) : super(ActivityContext(activity)) {
        mCaller = HaloPermission.originalCaller(activity, requestCode)
    }

    constructor(fragment: Fragment, requestCode: Int) : super(FragmentContext(fragment)) {
        mCaller = HaloPermission.originalCaller(fragment, requestCode)
    }

    constructor(fragment: android.support.v4.app.Fragment, requestCode: Int) : super(SupportFragmentContext(fragment)) {
        mCaller = HaloPermission.originalCaller(fragment, requestCode)
    }


    /**
     * 立即执行
     */
    @JvmOverloads
    fun run(enableStrictCheck: Boolean = true) = run(enableStrictCheck, mCaller)

    fun run(checker: PermissionChecker) = run(checker, mCaller)

    override fun run(enableStrictCheck: Boolean, caller: PermissionCaller) {
        super.run(enableStrictCheck, mCaller)
    }

    override fun run(checker: PermissionChecker, caller: PermissionCaller) {
        super.run(checker, mCaller)
    }
}