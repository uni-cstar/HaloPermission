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

package halo.android.permission.spec

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import halo.android.permission.caller.FragmentCaller

/**
 * Created by Lucio on 2019/6/25.
 */

class SpecPermissionFragmentCaller : FragmentCaller, SpecialCaller {

    constructor(ctx: Context, fm: FragmentManager) : super(ctx, fm)
    constructor(activity: FragmentActivity) : super(activity)
    constructor(fragment: Fragment) : super(fragment)


    override fun requestSpecialPermission(spec: SpecPermission) {
        callerFragment.requestSpecialPermission(spec)
    }

}