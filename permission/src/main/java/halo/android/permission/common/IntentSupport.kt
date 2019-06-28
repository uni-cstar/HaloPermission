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

package halo.android.permission.common

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * Created by Lucio on 2019/6/25.
 */

object IntentSupport {

    /**
     * 系统设置界面
     */
    @JvmStatic
    fun createSystemSettingIntent(): Intent {
        return Intent(Settings.ACTION_SETTINGS)
    }

    /**
     * 应用详情界面
     */
    @JvmStatic
    fun createAppDetailSettingIntent(pkgName: String): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", pkgName, null)
        }
    }

    @JvmStatic
    fun createAppDetailSettingIntent(ctx: Context): Intent {
        return createAppDetailSettingIntent(ctx.packageName)
    }

    /**
     * 所有应用详情界面
     */
    fun createAllAppSettingIntent(ctx: Context): Intent {
        return Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS)
    }


    /**
     * 创建系统通知设置意图
     */
    @JvmStatic
    fun createNotificationSettingIntent(ctx: Context): Intent {
        val sdkInt = Build.VERSION.SDK_INT
        return if (sdkInt >= 26) {
            createNotificationSettingApi26(ctx)
        } else if (sdkInt >= 19) {
            createNotificationSettingApi19(ctx)
        } else {
            createNotificationSettingDefault(ctx)
        }
    }

    @JvmStatic
    private fun createNotificationSettingDefault(ctx: Context): Intent {
        var intent = createAppDetailSettingIntent(ctx.packageName)
        if (intent.resolveActivity(ctx.packageManager) == null) {
            intent = createSystemSettingIntent()
        }
        return intent
    }

    @JvmStatic
    @TargetApi(26)
    private fun createNotificationSettingApi26(ctx: Context): Intent {
        return Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, ctx.packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, ctx.applicationInfo.uid)
                }
    }

    @JvmStatic
    @TargetApi(19)
    private fun createNotificationSettingApi19(ctx: Context): Intent {
        return Intent("android.settings.APP_NOTIFICATION_SETTINGS")
                .apply {
                    putExtra("app_package", ctx.packageName)
                    putExtra("app_uid", ctx.applicationInfo.uid)
                }
    }

    /**
     * 创建通知渠道设置意图
     */
    fun createNotificationChanelSettingIntent(ctx: Context,channelId:String):Intent{
        if(Build.VERSION.SDK_INT >= 26){
            return IntentSupport.createNotificationChanelSettingIntentApi26(ctx, channelId)
        }else{
            return createNotificationSettingIntent(ctx)
        }
    }

    /**
     * 创建通知渠道设置意图
     */
    @TargetApi(26)
    fun createNotificationChanelSettingIntentApi26(ctx: Context,channelId:String):Intent{
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, ctx.packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        return intent
    }

    /**
     * 意图：当前应用的"未知来源管理"
     */
    @JvmStatic
    @TargetApi(Util.O)
    fun createAppUnknownSourceManagerIntent(ctx: Context): Intent {
        // 开启当前应用的权限管理页
        val packageUri = Uri.fromParts("package", ctx.packageName, null)
        return Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
    }

    /**
     * 请求系统设置修改Intent
     */
    @JvmStatic
    @TargetApi(Util.M)
    fun createWriteSystemSettingIntent(ctx: Context): Intent {
        val packageURI = Uri.fromParts("package", ctx.packageName, null)
        return Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, packageURI)
    }

    /**
     * 请求设置悬浮窗权限
     */
    @JvmStatic
    @TargetApi(Util.M)
    fun createDrawOverlaysSettingIntent(ctx: Context): Intent {
        val packageURI = Uri.fromParts("package", ctx.packageName, null)
        return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI)
    }

}

internal fun Intent.orAppSettingIntent(ctx: Context): Intent {
    return if (resolveActivity(ctx.packageManager) == null) {
        IntentSupport.createAppDetailSettingIntent(ctx)
    }else {
        this
    }
}

internal fun Intent.orSystemSettingIntent(ctx: Context):Intent{
    return if (resolveActivity(ctx.packageManager) == null) {
        IntentSupport.createSystemSettingIntent()
    }else {
        this
    }
}

internal fun Intent.orDefault(ctx: Context):Intent{
    return this.orAppSettingIntent(ctx)
            .orSystemSettingIntent(ctx)
}



