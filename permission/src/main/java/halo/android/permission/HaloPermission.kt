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

package halo.android.permission

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import halo.android.permission.caller.FragmentCaller
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.checker.PermissionChecker
import halo.android.permission.checker.StandardChecker
import halo.android.permission.checker.StrictChecker
import halo.android.permission.processor.PermissionProcessor
import halo.android.permission.request.GrandAction
import halo.android.permission.request.PermissionListener
import halo.android.permission.request.PermissionRequest

/**
 * Created by Lucio on 2019/6/23.
 */

object HaloPermission {

    /**
     * 透明fragment请求方式
     */
    fun newFragmentCaller(activity: FragmentActivity): PermissionCaller {
        return FragmentCaller(activity)
    }

    /**
     * 透明fragment请求方式
     */
    fun newFragmentCaller(fragment: Fragment): PermissionCaller {
        return FragmentCaller(fragment)
    }

    /**
     * 标准权限检测
     */
    fun newStandardChecker(): PermissionChecker {
        return StandardChecker()
    }

    /**
     * 严格权限检测
     */
    @Deprecated(message = "感觉没什么必要")
    fun newStrictChecker(): PermissionChecker {
        return StrictChecker()
    }

    fun newRequest(ctx: Context, vararg permissions: String): PermissionRequest {
        return PermissionRequest(ctx, permissions)
    }


    fun invokeRequest(activity: FragmentActivity,
                      grandAction: GrandAction,
                      vararg permissions: String) {
        newRequest(activity, *permissions)
                .setGrandAction(grandAction)
                .request(activity)
    }

    fun invokeRequest(fragment: Fragment,
                      grandAction: GrandAction,
                      vararg permissions: String) {
        newRequest(fragment.requireContext(), *permissions)
                .setGrandAction(grandAction)
                .request(fragment)
    }

    fun invokeRequest(activity: FragmentActivity,
                      permissionListener: PermissionListener,
                      vararg permissions: String) {
        newRequest(activity, *permissions)
                .setListener(permissionListener)
                .request(activity)
    }

    fun invokeRequest(fragment: Fragment,
                      permissionListener: PermissionListener,
                      vararg permissions: String) {
        newRequest(fragment.requireContext(), *permissions)
                .setListener(permissionListener)
                .request(fragment)
    }


    /**
     * 执行请求
     */
    fun invokeRequest(request: PermissionRequest,
                      caller: PermissionCaller,
                      checker: PermissionChecker) {
        PermissionProcessor(request, caller, checker)
                .invoke()
    }

    fun invokeRequest(activity: FragmentActivity,
                      request: PermissionRequest) {
        return request.request(activity)
    }

    fun invokeRequest(fragment: Fragment,
                      request: PermissionRequest) {
        return request.request(fragment)
    }

}