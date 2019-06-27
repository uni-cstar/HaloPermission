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

package halo.android.v2.spec

import android.content.Context
import android.content.Intent
import halo.android.v2.SpecPermission

/**
 * Created by Lucio on 2019/6/24.
 */

enum class SpecialPermission {

    /**
     * 通知权限
     */
    NOTIFICATION,

    /**
     * 悬浮窗权限
     */
    SYSTEM_ALERT_WINDOW,

    /**
     * 允许安装未知权限
     */
    UNKNOWN_APP_SOURCES,

    /**
     * 更改设置
     */
    WRITE_SETTINGS;

    /**
     * 新建设置intent
     */
    fun newSettingIntent(ctx: Context): Intent {
        return when (this) {
            NOTIFICATION -> {
                SpecPermission.createNotificationSettingIntentOrDefault(ctx)
            }
            SYSTEM_ALERT_WINDOW -> {
                SpecPermission.createDrawOverlaysSettingIntentOrDefault(ctx)
            }
            UNKNOWN_APP_SOURCES -> {
                SpecPermission.createAppUnknownSourceManagerIntentOrDefault(ctx)
            }
            WRITE_SETTINGS -> {
                SpecPermission.createWriteSystemSettingIntentOrDefault(ctx)
            }
        }
    }
}