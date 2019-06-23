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

package halo.android.v2.processor

import android.content.Intent
import android.os.Build
import halo.android.permission.setting.SettingIntent
import halo.android.v2.caller.PermissionCaller
import halo.android.v2.checker.PermissionChecker
import halo.android.v2.common.Util
import halo.android.v2.request.PermissionRequest
import halo.android.v2.request.RationaleRender
import halo.android.v2.setting.SettingRender
import halo.android.v2.setting.SettingResponder

/**
 * Created by Lucio on 2019/6/22.
 */

class PermissionProcessor(val request: PermissionRequest,
                          val caller: PermissionCaller,
                          val checker: PermissionChecker) : PermissionResponder, SettingResponder {

    //被拒绝的权限集合
    private var denidPermissons: MutableList<String> = mutableListOf()

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
        //过滤被拒绝的权限
        val denidPermissons = permissions.filter {
            !checker.isPermissionGranted(request.ctx, it)
        }

        if (denidPermissons.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Util.M) {//有未授权权限
                //过滤Rationale权限
                val rationalePermissions = denidPermissons.filter {
                    checker.shouldShowRequestPermissionRationale(request.ctx, it)
                }
                if (!rationalePermissions.isNullOrEmpty()) {
                    //有Rationale权限，执行RatinaleRender流程，判断是否需要向用户解释获取权限的原因
                    invokeRationaleRenderProcess(rationalePermissions)
                } else {
                    //没有Rationale权限，直接请求权限得到结果
                    requestPermissionsReal()
                }
            } else {
                //23以下，直接处理SettingRender
                invokeSettingRenderProcess()
            }
        } else {
            //所有权限已通过，直接触发回调
            notifyPermissionSucceed()
        }
    }

    /**
     * 执行RationaleRender流程
     */
    private fun invokeRationaleRenderProcess(permissions: List<String>) {
        val rationaleRender = request.getRationaleRender()
        if (rationaleRender != null) {
            //执行RationaleRender显示
            rationaleRender.show(request.ctx, permissions, mRationaleRenderProcess)
        } else {
            //没有设置RationaleRender，则直接请求权限
            requestPermissionsReal()
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
                requestPermissionsReal()
            }

            /**
             * 取消则执行权限设置流程
             */
            override fun onCancel() {
                invokeSettingRenderProcess()
            }
        }
    }

    /**
     * 权限请求成功
     */
    protected fun notifyPermissionSucceed() {
        request.getGrandAction()?.onPermissionGrand(request.permissions.toList())
    }

    /**
     * 权限请求失败
     */
    protected fun notifyPermissionFailed() {
        request.getDenyAction()?.onPermissionDenied(denidPermissons)
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
        denidPermissons.clear()
        //过滤被拒绝的权限
        permissions.filterTo(denidPermissons) {
            !checker.isPermissionGranted(request.ctx, it)
        }

        if (denidPermissons.isNullOrEmpty()) {
            notifyPermissionSucceed()
        } else {
            invokeSettingRenderProcess()
        }
    }

    /**
     * 处理权限设置SettingRende
     */
    private fun invokeSettingRenderProcess() {
        val settingRender = request.getSettingRender()
        if (settingRender != null && !isSettingRenderDone) {
            isSettingRenderDone = true
            settingRender.show(request.ctx, denidPermissons, mSettingRenderProcess)
        } else {
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
                this@PermissionProcessor.autoCheckWhenSettingResult = autoCheckWhenSettingResult
                requestPermissionSettingReal()
            }

            /**
             * RationaleView回调 取消
             */
            override fun onCancel() {
                notifyPermissionFailed()
            }

        }
    }

    /**
     * 请求权限设置界面
     */
    private fun requestPermissionSettingReal() {
        val settingIntent = SettingIntent.getCanResolvedSettingIntent(request.ctx, request.getSettingRender()?.getCustomSettingIntent(request.ctx))
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
            denidPermissons.clear()
            request.permissions.filterTo(denidPermissons) {
                !checker.isPermissionGranted(request.ctx, it)
            }

            if (denidPermissons.isEmpty()) {
                notifyPermissionSucceed()
            } else {
                notifyPermissionFailed()
            }
        }
    }

}