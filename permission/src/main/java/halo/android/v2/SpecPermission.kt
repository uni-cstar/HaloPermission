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

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import halo.android.v2.common.Util

/**
 * Created by Lucio on 2019/6/24.
 */

object SpecPermission {

    /**
     * 是否能够请求应用安装
     */
    fun canRequestPackageInstalls(ctx: Context): Boolean {
        //8.0之前不需要适配未知来源 应用安装
        if (Build.VERSION.SDK_INT <= Util.O)
            return true
        return ctx.packageManager.canRequestPackageInstalls()
    }

    /**
     * 意图：管理"所有未知来源管理"
     */
    @TargetApi(Util.O)
    fun newManageUnknownAppSourceIntent(): Intent {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent
    }

    /**
     * 意图：当前应用的"未知来源管理"
     */
    @TargetApi(Util.O)
    fun newUnknownAppSourceIntent(ctx: Context): Intent {
        // 开启当前应用的权限管理页
        val packageUri = Uri.fromParts("package", ctx.packageName, null)
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent
    }

}