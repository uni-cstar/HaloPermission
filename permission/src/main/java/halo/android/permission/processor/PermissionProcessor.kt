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

package halo.android.permission.processor

import android.content.Intent
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.checker.PermissionChecker
import halo.android.permission.common.PLog
import halo.android.permission.request.PermissionRequest
import halo.android.permission.request.RationaleRender
import halo.android.permission.setting.PermissionSetting
import halo.android.permission.setting.SettingRender
import halo.android.permission.setting.SettingResponder

/**
 * Created by Lucio on 2019/6/22.
 */

class PermissionProcessor(val request: PermissionRequest,
                          val caller: PermissionCaller,
                          val checker: PermissionChecker) : PermissionResponder, SettingResponder {

    //被拒绝的权限集合
    private var permissionStates: List<PermissionState> = listOf()

    /**
     * 控制SettingRender只显示一次
     */
    private var isSettingRenderDone = false

    /**
     * 控制在设置界面关闭之后，是否自动重新检查权限
     */
    private var autoCheckWhenSettingResult: Boolean = true

    /**
     * 校验请求
     */
    fun invoke() {
        val permissions = request.permissions
        PLog.d("[invoke]:即将请求以下权限：${permissions.joinToString(",")}")
        //过滤被拒绝的权限
        val denyPermissions = permissions.filter {
            !checker.isPermissionGranted(request.ctx, it)
        }
        if (denyPermissions.isNotEmpty()) {
            PLog.d("[invoke]:以下权限未授权，进行请求${denyPermissions.joinToString(",")}")
            requestPermissionsReal()
//            if (Build.VERSION.SDK_INT >= Util.M) {//有未授权权限
//                //过滤Rationale权限
//                val rationalePermissions = permissionStates.filter {
//                    checker.shouldShowRequestPermissionRationale(invokeRequest.ctx, it)
//                }
//                if (!rationalePermissions.isNullOrEmpty()) {
//                    //有Rationale权限，执行RatinaleRender流程，判断是否需要向用户解释获取权限的原因
//                    invokeRationaleRenderProcess(rationalePermissions)
//                } else {
//                    //没有Rationale权限，直接请求权限得到结果
//                    requestPermissionsReal()
//                }
//            } else {
//                //23以下，直接处理SettingRender
//                invokeSettingRenderProcess()
//            }
        } else {
            PLog.d("[invoke]:所有权限已授权，直接通知成功回调")
            //所有权限已通过，直接触发回调
            notifyPermissionSucceed()
        }
    }

    /**
     * 权限请求成功
     */
    protected fun notifyPermissionSucceed() {
        request.getGrandAction()?.onPermissionGrand(request.permissions.toList())
    }

    private fun getDeniedPermissions():List<String>{
       return permissionStates.filter{
            !it.isGrand
        }.map {
           it.value
       }
    }
    /**
     * 权限请求失败
     */
    protected fun notifyPermissionFailed() {
        request.getDenyAction()?.onPermissionDenied(getDeniedPermissions())
    }

    /**
     * 请求权限
     */
    private fun requestPermissionsReal() {
        caller.requestPermission(this, *request.permissions)
    }

    /**
     * [PermissionResponder]回调 处理请求的权限结果返回
     */
    override fun onPermissionResponderResult(permissions: Array<out String>, grantResults: IntArray) {
        PLog.d("[onPermissionResponderResult]")
        var containsNever: Boolean = false
        var exitsDenyPermission: Boolean = false

        val ctx = request.ctx
         permissionStates = permissions.map { permission ->
            val isGrand = checker.isPermissionGranted(ctx, permission)
            val shouldRationale = checker.shouldShowRequestPermissionRationale(ctx, permission)

            if (!isGrand) {
                exitsDenyPermission = true
            }

            if (!isGrand && !shouldRationale) {
                //如果权限被拒绝，并不允许show rationale，则说明权限被拒绝并且被设置为不再询问等，
                // 也就是说即便二次请求也无法获得权限
                containsNever = true
            }
            PermissionState(permission, isGrand, shouldRationale)
        }

        if (exitsDenyPermission) {//存在未授权权限
            PLog.d("[onPermissionResponderResult]:存在未授权权限")
            if (containsNever) {//存在无法二次请求获取授权的权限
                PLog.d("[onPermissionResponderResult]:存在无法二次请求获取授权的权限，执行SettingRender")
                invokeSettingRenderProcess()
            } else {
                PLog.d("[onPermissionResponderResult]:执行RatinalRender")
                invokeRationaleRenderProcess(permissionStates.filter {
                    it.shouldRationale
                }.map {
                    it.value
                })
            }
        } else {
            PLog.d("[onPermissionResponderResult]:所有权限已授权，进行成功回调")
            notifyPermissionSucceed()
        }

//        permissionStates.clear()
//        //过滤被拒绝的权限
//        permissions.filterTo(permissionStates) {
//            !checker.isPermissionGranted(invokeRequest.ctx, it)
//        }
//
//        if (permissionStates.isNullOrEmpty()) {
//            notifyPermissionSucceed()
//        } else {
//            invokeSettingRenderProcess()
//        }
    }

    /**
     * 执行RationaleRender流程
     */
    private fun invokeRationaleRenderProcess(permissions: List<String>) {
        PLog.d("[invokeRationaleRenderProcess]")
        val rationaleRender = request.getRationaleRender()
        if (rationaleRender != null) {
            PLog.d("[invokeRationaleRenderProcess]:执行RationaleRender展示")
            //执行RationaleRender显示
            rationaleRender.show(request.ctx, permissions, mRationaleRenderProcess)
        } else {
            PLog.d("[invokeRationaleRenderProcess]:未设置RationaleRender,执行invokeSettingRenderProcess")
            invokeSettingRenderProcess()
//            //没有设置RationaleRender，则直接请求权限
//            requestPermissionsReal()
        }
    }

    /**
     * RationaleRender回调
     */
    private val mRationaleRenderProcess: RationaleRender.Process by lazy {
        object : RationaleRender.Process {
            /**
             * 执行权限请求
             */
            override fun onNext() {
                PLog.d("[RationaleRender.Process]:onNext")
                requestPermissionsReal()
            }

            /**
             * 取消则执行权限设置流程
             */
            override fun onCancel() {
                PLog.d("[RationaleRender.Process]:onCancel")
                invokeSettingRenderProcess()
            }
        }
    }

    /**
     * 处理权限设置SettingRende
     */
    private fun invokeSettingRenderProcess() {
        PLog.d("[invokeSettingRenderProcess]")
        val settingRender = request.getSettingRender()
        if (settingRender != null && !isSettingRenderDone) {
            PLog.d("[invokeSettingRenderProcess]:已设置SettingRender，并且未显示过权限设置界面，显示")
            isSettingRenderDone = true
            settingRender.show(request.ctx, getDeniedPermissions(), mSettingRenderProcess)
        } else {
            PLog.d("[invokeSettingRenderProcess]:未设置SettingRender，通知权限请求失败")
            notifyPermissionFailed()
        }
    }

    /**
     * RationaleView回调
     */
    private val mSettingRenderProcess: SettingRender.Process by lazy {

        object : SettingRender.Process {

            /**
             * RationaleView回调 下一步
             */
            override fun onNext(autoCheckWhenSettingResult: Boolean) {
                PLog.d("[SettingRender.Process]:onNext(autoCheckWhenSettingResult=$autoCheckWhenSettingResult)")
                this@PermissionProcessor.autoCheckWhenSettingResult = autoCheckWhenSettingResult
                requestPermissionSettingReal()
            }

            /**
             * RationaleView回调 取消
             */
            override fun onCancel() {
                PLog.d("[SettingRender.Process]:onCancel")
                notifyPermissionFailed()
            }

        }
    }

    /**
     * 请求权限设置界面
     */
    private fun requestPermissionSettingReal() {
        val settingIntent = PermissionSetting.getCanResolvedSettingIntent(request.ctx, request.getSettingRender()?.getCustomSettingIntent(request.ctx))
        if (settingIntent != null) {
            caller.requestPermissionSetting(this, settingIntent)
        } else {
            notifySettingResult()
        }
    }

    /**
     * [SettingResponder]回调
     */
    override fun onSettingResponderResult(resultCode: Int, data: Intent?) {
        notifySettingResult()
    }

    private fun notifySettingResult() {
        if (autoCheckWhenSettingResult) {
            //过滤被拒绝的权限
            val deniedPermissions = request.permissions.filter{
                !checker.isPermissionGranted(request.ctx, it)
            }

            if (deniedPermissions.isEmpty()) {
                notifyPermissionSucceed()
            } else {
                notifyPermissionFailed()
            }
        }
    }

}