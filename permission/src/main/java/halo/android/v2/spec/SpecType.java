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

package halo.android.v2.spec;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static halo.android.v2.spec.SpecType.NOTIFICATION;
import static halo.android.v2.spec.SpecType.NOTIFICATION_CHANNEL;
import static halo.android.v2.spec.SpecType.SYSTEM_ALERT_WINDOW;
import static halo.android.v2.spec.SpecType.UNKNOWN_APP_SOURCES;
import static halo.android.v2.spec.SpecType.WRITE_SETTINGS;

/**
 * Created by Lucio on 2019/6/27.
 * 特殊权限类型
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({NOTIFICATION, NOTIFICATION_CHANNEL, SYSTEM_ALERT_WINDOW, UNKNOWN_APP_SOURCES, WRITE_SETTINGS})
@interface SpecType {
    /**
     * 通知权限
     */
    int NOTIFICATION = 1;

    /**
     * 通知渠道
     */
    int NOTIFICATION_CHANNEL = 2;

    /**
     * 悬浮窗权限
     */
    int SYSTEM_ALERT_WINDOW = 3;

    /**
     * 允许安装未知权限
     */
    int UNKNOWN_APP_SOURCES = 4;

    /**
     * 更改设置
     */
    int WRITE_SETTINGS = 5;
}
