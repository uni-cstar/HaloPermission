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
import android.content.Intent
import halo.android.permission.HaoloSpecPermission

/**
 * Created by Lucio on 2019/6/27.
 */


//通知权限
open class SpecPermissionNotification(listener: SpecialListener) : SpecPermission(listener) {

    override fun isGrandOrThrow(ctx: Context): Boolean {
        return isGrand(ctx)
    }

    override fun isGrand(ctx: Context): Boolean {
        return HaoloSpecPermission.areNotificationEnabled(ctx)
    }

    override fun createSettingIntent(ctx: Context): Intent {
        return HaoloSpecPermission.createNotificationSettingIntentOrDefault(ctx)
    }
}

//通知渠道权限
class SpecPermissionNotificationChannel(listener: SpecialListener,val channelId:String)
    :SpecPermissionNotification(listener){


    override fun isGrand(ctx: Context): Boolean {
        //通知渠道的权限受限于通知总开关，所以需要先判断是否具有通知权限
        return super.isGrand(ctx) &&
                HaoloSpecPermission.areNotificationChannelsEnabled(ctx,channelId)
    }

    override fun isGrandOrThrow(ctx: Context): Boolean {
        //通知渠道的权限受限于通知总开关，所以需要先判断是否具有通知权限
        return super.isGrandOrThrow(ctx) &&
                HaoloSpecPermission.areNotificationChannelsEnabledOrThrow(ctx,channelId)
    }

    override fun createSettingIntent(ctx: Context): Intent {
        //没有通知权限时先打开通知开关界面，其次再打开通知渠道界面
        if(!super.isGrand(ctx)){
            return super.createSettingIntent(ctx)
        }else{
            return HaoloSpecPermission.createNotificationChanelSettingIntentOrDefault(ctx, channelId)
        }
    }

}

//悬浮窗权限
class SpecPermissionSystemAlertWindow(listener: SpecialListener) : SpecPermission(listener) {

    override fun isGrand(ctx: Context): Boolean {
        return HaoloSpecPermission.areDrawOverlaysEnable(ctx)
    }

    override fun isGrandOrThrow(ctx: Context): Boolean {
        return HaoloSpecPermission.areDrawOverlaysEnableOrThrow(ctx)
    }

    override fun createSettingIntent(ctx: Context): Intent {
        return HaoloSpecPermission.createDrawOverlaysSettingIntentOrDefault(ctx)
    }

}

//未知来源安装权限
class SpecPermissionPackageInstall(listener: SpecialListener) : SpecPermission(listener) {

    override fun isGrandOrThrow(ctx: Context): Boolean {
        return isGrand(ctx)
    }

    override fun isGrand(ctx: Context): Boolean {
        return HaoloSpecPermission.areRequestPackageInstallsEnable(ctx)
    }

    override fun createSettingIntent(ctx: Context): Intent {
        return HaoloSpecPermission.createAppUnknownSourceManagerIntentOrDefault(ctx)
    }

}

//系统设置修改
class SpecPermissionWriteSystemSetting(listener: SpecialListener) : SpecPermission(listener) {
    override fun isGrandOrThrow(ctx: Context): Boolean {
        return HaoloSpecPermission.areWriteSystemSettingEnableOrThrow(ctx)
    }

    override fun isGrand(ctx: Context): Boolean {
        return HaoloSpecPermission.areWriteSystemSettingEnable(ctx)
    }

    override fun createSettingIntent(ctx: Context): Intent {
        return HaoloSpecPermission.createWriteSystemSettingIntentOrDefault(ctx)
    }

}
