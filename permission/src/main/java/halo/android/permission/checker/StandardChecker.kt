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

/**
 * Created by Lucio on 18/4/8.
 *
 */
package halo.android.permission.checker

import android.content.Context
import android.support.v4.app.AppOpsManagerCompat
import halo.android.permission.common.Util

/**
 * 标准权限判断
 */
open class StandardChecker : PermissionChecker {

    override fun isPermissionGranted(ctx: Context, permission: String): Boolean {
        if (Util.isPermissionGranted(ctx, permission)) {
            //以下代码仿照v4中的PermissionChecker实现
            //将Permission转换成对应的Op
            val op = AppOpsManagerCompat.permissionToOp(permission)
            if (!op.isNullOrEmpty()) {
                //如果AppOpsManagerCompat.noteProxyOp(ctx, op, ctx.packageName) == PERMISSION_DENIED_APP_OP
                // 则表示The permission is denied because the app op is not allowed.
                return AppOpsManagerCompat.noteProxyOp(ctx, op, ctx.packageName) == AppOpsManagerCompat.MODE_ALLOWED
            }
            return true
        } else {
            return false
        }
    }
}