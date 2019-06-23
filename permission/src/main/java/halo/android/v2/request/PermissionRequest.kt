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

package halo.android.v2.request

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import halo.android.v2.HaloPermission
import halo.android.v2.caller.PermissionCaller
import halo.android.v2.checker.PermissionChecker
import halo.android.v2.processor.PermissionProcessor
import halo.android.v2.setting.SettingRender

/**
 * Created by Lucio on 2019/6/22.
 */

class PermissionRequest(val ctx: Context,
                        val permissions: Array<out String>) {

    /**
     * 用于渲染Rationale permission（通常是一个对话框提示用户让用户允许app接下来请求的权限）
     */
    private var mRationaleRender: RationaleRender? = null

    /**
     * 用于渲染请求设置权限（通常是一个对话框提示用户让用户设置应用权限）
     */
    private var mSettingRender: SettingRender? = null

    private var mGrandAction: GrandAction? = null

    private var mDenyAction: DenyAction? = null


    fun setRationaleRender(view: RationaleRender?): PermissionRequest {
        mRationaleRender = view
        return this
    }

    fun getRationaleRender(): RationaleRender? {
        return mRationaleRender
    }

    fun setSettingRender(view: SettingRender?): PermissionRequest {
        mSettingRender = view
        return this
    }

    fun getSettingRender(): SettingRender? {
        return mSettingRender
    }

    fun setGrandAction(action: GrandAction?): PermissionRequest {
        mGrandAction = action
        return this
    }

    fun getGrandAction(): GrandAction? {
        return mGrandAction
    }

    fun setDenyAction(action: DenyAction?): PermissionRequest {
        mDenyAction = action
        return this
    }

    fun getDenyAction(): DenyAction? {
        return mDenyAction
    }

    fun setListener(listener: PermissionListener?): PermissionRequest {
        mDenyAction = listener
        mGrandAction = listener
        return this
    }

    fun request(activity: FragmentActivity) {
        PermissionProcessor(this,
                HaloPermission.newFragmentCaller(activity),
                HaloPermission.newStandardChecker())
                .invoke()
    }

    fun request(fragment: Fragment) {
        PermissionProcessor(this,
                HaloPermission.newFragmentCaller(fragment),
                HaloPermission.newStandardChecker())
                .invoke()
    }

    /**
     * 执行请求
     */
    fun request(caller: PermissionCaller, checker: PermissionChecker) {
        PermissionProcessor(this, caller, checker)
                .invoke()
    }
}