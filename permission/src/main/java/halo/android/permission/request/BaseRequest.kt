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

import halo.android.permission.R
import halo.android.permission.caller.ActivityPermissionCaller
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.checker.PermissionChecker
import halo.android.permission.checker.StandardChecker23
import halo.android.permission.context.PermissionContext
import halo.android.permission.processor.CommonProcessor
import halo.android.permission.processor.PermissionProcessor
import halo.android.permission.processor.StrictProcessor
import halo.android.permission.setting.DefaultSettingRender

/**
 * Created by Lucio on 18/4/4.
 */
open class BaseRequest(val permissionContext: PermissionContext) {

    /**
     * 用于渲染Rationale permission（通常是一个对话框提示用户让用户允许app接下来请求的权限）
     */
    private var mRationaleRender: RationaleRender? = null

    /**
     * 用于渲染请求设置权限（通常是一个对话框提示用户让用户设置应用权限）
     */
    private var mSettingRender: SettingRender? = null
    private var mPermissions: Array<out String> = emptyArray()
    private var mGrandAction: GrandAction? = null
    private var mDenyAction: DenyAction? = null

    fun getContext() = permissionContext.ctx

    fun setPermissions(vararg permissions: String): BaseRequest {
        mPermissions = permissions
        return this
    }

    fun getPermissions(): Array<out String> {
        return mPermissions
    }

    fun setRationaleRender(view: RationaleRender?): BaseRequest {
        mRationaleRender = view
        return this
    }

    /**
     * 使用对话框形式引导用户
     */
    fun setRationaleRender(msg: String, title: String? = null,
                           okText: String = "OK",
                           cancelText: String? = null): BaseRequest {
        mRationaleRender = DefaultRationaleRender(msg, title, okText, cancelText)
        return this
    }

    fun getRationaleRender(): RationaleRender? {
        return mRationaleRender
    }

    fun setSettingRender(view: SettingRender?): BaseRequest {
        mSettingRender = view
        return this
    }

    /**
     * 使用对话框的形式引导用户
     */
    fun setSettingRender(msg: String, title: String? = null,
                         okText: String = getContext().getString(R.string.setting_render_confirm_text),
                         cancelText: String? = getContext().getString(R.string.setting_render_cancel_text)): BaseRequest {
        mSettingRender = DefaultSettingRender(msg, title, okText, cancelText)
        return this
    }

    fun getSettingRender(): SettingRender? {
        return mSettingRender
    }

    fun setGrandAction(action: GrandAction?): BaseRequest {
        mGrandAction = action
        return this
    }

    fun getGrandAction(): GrandAction? {
        return mGrandAction
    }

    fun setDenyAction(action: DenyAction?): BaseRequest {
        mDenyAction = action
        return this
    }

    fun getDenyAction(): DenyAction? {
        return mDenyAction
    }

    fun setListener(action: PermissionListener?): BaseRequest {
        mDenyAction = action
        mGrandAction = action
        return this
    }

    protected fun build(enableStrictCheck: Boolean, caller: PermissionCaller): PermissionProcessor {
        if (enableStrictCheck) {
            return StrictProcessor(this, caller)
        } else {
            return build(StandardChecker23(), caller)
        }
    }

    protected fun build(checker: PermissionChecker, caller: PermissionCaller): PermissionProcessor {
        return CommonProcessor(this, checker, caller)
    }

    /**
     * 立即执行
     */
    @JvmOverloads
    open fun run(enableStrictCheck: Boolean = true, caller: PermissionCaller = ActivityPermissionCaller()) = build(enableStrictCheck, caller).invoke()

    @JvmOverloads
    open fun run(checker: PermissionChecker, caller: PermissionCaller = ActivityPermissionCaller()) = build(checker, caller).invoke()
}