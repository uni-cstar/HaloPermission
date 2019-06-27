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

import android.Manifest
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationManagerCompat
import halo.android.v2.common.*
import halo.android.v2.spec.FragmentSpecialCaller
import halo.android.v2.spec.SpecialListener
import halo.android.v2.spec.SpecialPermission

/**
 * Created by Lucio on 2019/6/24.
 */

object SpecPermission {

    /**
     * 是否拥有悬浮窗权限
     */
    @JvmStatic
    @SuppressLint("InlinedApi")
    fun areDrawOverlaysEnable(ctx: Context): Boolean {
        val sdkInt = Build.VERSION.SDK_INT
        if (sdkInt >= Util.M) {
            val result = Settings.canDrawOverlays(ctx)
            PLog.d("[areDrawOverlaysEnable]:6.0之后的版本，检测结果=$result")
            return result
        } else if (sdkInt >= Util.KITKAT) {
            try {
                val appOpsManager = ctx.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

                val method = AppOpsManager::class.java.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                val result = 0 == method.invoke(appOpsManager, 24, Binder.getCallingUid(), ctx.packageName) as Int
                PLog.d("[areDrawOverlaysEnable]:4.4-6.0之间的版本，调用反射检测权限成功，result=$result")
                return result
            } catch (e: Exception) {
                PLog.d("[areDrawOverlaysEnable]:4.4-6.0之间的版本，调用反射检测权限失败，返回true")
                return true
            }
        } else {
            PLog.d("[areDrawOverlaysEnable]:4.4之前版本，默认返回true")
            //4.4之前直接使用：部分定制系统厂商自己处理的可能无效
            return true
        }
    }

    /**
     * 是否拥有悬浮窗权限
     * @throws Need to declare android.permission.SYSTEM_ALERT_WINDOW to call this api,
     * 需要在清单文件中申明android.permission.SYSTEM_ALERT_WINDOW，否则抛出异常
     */
    @JvmStatic
    @Throws(SecurityException::class)
    fun areDrawOverlaysEnableOrThrow(ctx: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Util.M && !Util.isManifestPermission(ctx, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            throw SecurityException("Need to declare android.permission.SYSTEM_ALERT_WINDOW to call this api." +
                    "需要在Manifest文件中申明android.permission.SYSTEM_ALERT_WINDOW权限，否则无法修改。")
        }
        return areDrawOverlaysEnable(ctx)
    }

    /**
     * 请求设置悬浮窗权限
     */
    @JvmStatic
    fun createDrawOverlaysSettingIntentOrDefault(ctx: Context): Intent {
        if (Build.VERSION.SDK_INT >= Util.M) {
            return IntentSupport.createDrawOverlaysSettingIntent(ctx).orDefault(ctx)
        } else {
            return IntentSupport.createAppDetailSettingIntent(ctx).orDefault(ctx)
        }
    }

    /**
     * 请求悬浮窗权限
     */
    fun requestDrawOverlays(activity: FragmentActivity, listener: SpecialListener) {
        checkAndRequest(activity, SpecialPermission.SYSTEM_ALERT_WINDOW, listener)
    }

    /**
     * 请求悬浮窗权限
     */
    fun requestDrawOverlays(fragment: Fragment, listener: SpecialListener) {
        checkAndRequest(fragment, SpecialPermission.SYSTEM_ALERT_WINDOW, listener)
    }

    /**
     * 是否能够更改系统设置
     * manifest必须配置：android.Manifest.permission#WRITE_SETTINGS
     */
    @JvmStatic
    fun areWriteSystemSettingEnable(ctx: Context): Boolean {
        //23以前默认没有权限
        if (Build.VERSION.SDK_INT < Util.M)
            return true
        return Settings.System.canWrite(ctx)
    }

    /**
     * 是否能够更改系统设置
     * @throws Need to declare android.permission.WRITE_SETTINGS to call this api,
     * 需要在清单文件中申明android.permission.WRITE_SETTINGS，否则抛出异常
     */
    @JvmStatic
    fun areWriteSystemSettingEnableOrThrow(ctx: Context): Boolean {
        //23以前默认没有权限
        if (Build.VERSION.SDK_INT >= Util.M && !Util.isManifestPermission(ctx, Manifest.permission.WRITE_SETTINGS)) {
            throw SecurityException("Need to declare android.permission.WRITE_SETTINGS to call this api,\n" +
                    "需要在清单文件中申明android.permission.WRITE_SETTINGS权限，否则无法修改设置")
        }
        return areWriteSystemSettingEnable(ctx)
    }

    /**
     * 请求系统设置修改Intent
     */
    @JvmStatic
    fun createWriteSystemSettingIntentOrDefault(ctx: Context): Intent {
        if (Build.VERSION.SDK_INT >= Util.M) {
            return IntentSupport.createWriteSystemSettingIntent(ctx).orDefault(ctx)
        } else {
            return IntentSupport.createAppDetailSettingIntent(ctx).orDefault(ctx)
        }
    }

    /**
     * 请求系统设置修改权限
     */
    fun requestWriteSystemSetting(activity: FragmentActivity, listener: SpecialListener) {
        checkAndRequest(activity, SpecialPermission.WRITE_SETTINGS, listener)
    }

    /**
     * 请求系统设置修改权限
     */
    fun requestWriteSystemSetting(fragment: Fragment, listener: SpecialListener) {
        checkAndRequest(fragment, SpecialPermission.WRITE_SETTINGS, listener)
    }

    /**
     * 是否能够请求应用安装
     * @throws Need to declare android.permission.REQUEST_INSTALL_PACKAGES to call this api,需要在清单文件中添加以下权限申明
     */
    @JvmStatic
    @Throws(SecurityException::class)
    fun areRequestPackageInstallsEnable(ctx: Context): Boolean {
        //8.0之前不需要适配未知来源 应用安装
        if (Build.VERSION.SDK_INT < Util.O)
            return true
        return ctx.packageManager.canRequestPackageInstalls()
    }

    /**
     * 意图：当前应用的"未知来源管理"
     */
    @JvmStatic
    fun createAppUnknownSourceManagerIntentOrDefault(ctx: Context): Intent {
        if (Build.VERSION.SDK_INT >= Util.O) {
            return IntentSupport.createAppUnknownSourceManagerIntent(ctx).orDefault(ctx)
        } else {
            return IntentSupport.createAppDetailSettingIntent(ctx).orSystemSettingIntent(ctx)
        }
    }

    /**
     * 请求"未知来源管理"权限
     * @throws Need to declare android.permission.REQUEST_INSTALL_PACKAGES to call this api,需要在清单文件中添加以下权限申明
     */
    fun requestAppUnknownSource(activity: FragmentActivity, listener: SpecialListener) {
        checkAndRequest(activity, SpecialPermission.UNKNOWN_APP_SOURCES, listener)
    }

    /**
     * 请求"未知来源管理"权限
     */
    fun requestAppUnknownSource(fragment: Fragment, listener: SpecialListener) {
        checkAndRequest(fragment, SpecialPermission.UNKNOWN_APP_SOURCES, listener)
    }

    /**
     * 通知是否已开启
     */
    @JvmStatic
    fun areNotificationEnabled(ctx: Context): Boolean {
        return NotificationManagerCompat.from(ctx.applicationContext)
                .areNotificationsEnabled()
    }

    /**
     * 通知渠道是否可用
     */
    fun areNotificationChannelsEnabled(ctx: Context, channelId: String): Boolean {
        if (Build.VERSION.SDK_INT <= Util.O)
            return true
        val notificationChannel = ctx.getSystemService(NotificationManager::class.java)
                .getNotificationChannel(channelId)
        //渠道为空或者渠道优先级不为none则视为拥有权限
        return notificationChannel == null || notificationChannel.importance != NotificationManager.IMPORTANCE_NONE
    }

    /**
     * 创建通知渠道设置意图
     */
    fun createNotificationChanelSettingIntentOrDefault(ctx: Context, channelId: String): Intent {
        return IntentSupport.createNotificationChanelSettingIntent(ctx, channelId)
                .orDefault(ctx)
    }

//    fun requestNotificationChanel(fragmentActivity: FragmentActivity, channelId: String, listener: SpecialListener) {
//        if (areNotificationChannelsEnabled(fragmentActivity, channelId)) {
//            listener.onSpecialGrand()
//        }
//        SpecPermission.checkAndRequest(fragmentActivity, )
//    }

    /**
     * 创建通知设置意图，或默认意图
     */
    fun createNotificationSettingIntentOrDefault(ctx: Context): Intent {
        return IntentSupport.createNotificationSettingIntent(ctx)
                .orDefault(ctx)
    }

    /**
     * 请求通知权限
     */
    fun requestNotification(activity: FragmentActivity, listener: SpecialListener) {
        checkAndRequest(activity, SpecialPermission.NOTIFICATION, listener)
    }

    /**
     * 请求通知权限
     */
    fun requestNotification(fragment: Fragment, listener: SpecialListener) {
        checkAndRequest(fragment, SpecialPermission.NOTIFICATION, listener)
    }

    private fun checkAndRequest(fragment: Fragment, spec: SpecialPermission, listener: SpecialListener) {
        if (isGrandOrThrow(fragment.requireContext(), spec)) {
            listener.onSpecialGrand(spec)
        } else {
            FragmentSpecialCaller(fragment)
                    .requestSpecialPermission(spec, listener)
        }
    }

    private fun checkAndRequest(fragmentActivity: FragmentActivity, spec: SpecialPermission, listener: SpecialListener) {
        if (isGrandOrThrow(fragmentActivity, spec)) {
            listener.onSpecialGrand(spec)
        } else {
            FragmentSpecialCaller(fragmentActivity)
                    .requestSpecialPermission(spec, listener)
        }
    }

    internal fun isGrandOrThrow(ctx: Context, spec: SpecialPermission): Boolean {
        return when (spec) {
            SpecialPermission.NOTIFICATION -> {
                SpecPermission.areNotificationEnabled(ctx)
            }
            SpecialPermission.SYSTEM_ALERT_WINDOW -> {
                SpecPermission.areDrawOverlaysEnableOrThrow(ctx)
            }
            SpecialPermission.UNKNOWN_APP_SOURCES -> {
                SpecPermission.areRequestPackageInstallsEnable(ctx)
            }
            SpecialPermission.WRITE_SETTINGS -> {
                SpecPermission.areWriteSystemSettingEnableOrThrow(ctx)
            }
        }
    }

    internal fun isGrand(ctx: Context, spec: SpecialPermission): Boolean {
        return when (spec) {
            SpecialPermission.NOTIFICATION -> {
                SpecPermission.areNotificationEnabled(ctx)
            }
            SpecialPermission.SYSTEM_ALERT_WINDOW -> {
                SpecPermission.areDrawOverlaysEnable(ctx)
            }
            SpecialPermission.UNKNOWN_APP_SOURCES -> {
                SpecPermission.areRequestPackageInstallsEnable(ctx)
            }
            SpecialPermission.WRITE_SETTINGS -> {
                SpecPermission.areWriteSystemSettingEnable(ctx)
            }
        }
    }
}