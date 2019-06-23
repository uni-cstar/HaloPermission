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

package halo.android.v2

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import halo.android.v2.caller.FragmentCaller
import halo.android.v2.caller.PermissionCaller
import halo.android.v2.checker.PermissionChecker
import halo.android.v2.checker.StandardChecker
import halo.android.v2.checker.StrictChecker
import halo.android.v2.processor.PermissionProcessor
import halo.android.v2.request.GrandAction
import halo.android.v2.request.PermissionListener
import halo.android.v2.request.PermissionRequest

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
    fun newStrictChecker(): PermissionChecker {
        return StrictChecker()
    }

    fun newRequest( ctx:Context,vararg permissions: String): PermissionRequest {
        return PermissionRequest(ctx,permissions)
    }


    fun request(activity: FragmentActivity,
                grandAction: GrandAction,
                vararg permissions: String) {
        newRequest(activity,*permissions)
                .setGrandAction(grandAction)
                .request(activity)
    }

    fun request(fragment: Fragment,
                grandAction: GrandAction,
                vararg permissions: String) {
        newRequest(fragment.requireContext(),*permissions)
                .setGrandAction(grandAction)
                .request(fragment)
    }

    fun request(activity: FragmentActivity,
                permissionListener: PermissionListener,
                vararg permissions: String) {
        newRequest(activity,*permissions)
                .setListener(permissionListener)
                .request(activity)
    }

    fun request(fragment: Fragment,
                permissionListener: PermissionListener,
                vararg permissions: String) {
        newRequest(fragment.requireContext(),*permissions)
                .setListener(permissionListener)
                .request(fragment)
    }


    /**
     * 执行请求
     */
    fun request(request: PermissionRequest,
                caller: PermissionCaller,
                checker: PermissionChecker) {
        PermissionProcessor( request, caller, checker)
                .invoke()
    }

    fun request(activity: FragmentActivity,
                request: PermissionRequest){
        return request.request(activity)
    }

    fun request(fragment: Fragment,
                request: PermissionRequest){
        return request.request(fragment)
    }

}