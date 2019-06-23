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

package halo.android.v2.common

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker

/**
 * Created by Lucio on 18/4/8.
 */

object Util {

    //23版本以下的没有[Build.VERSION_CODES.M]变量
    const val M = 23

    /**
     * 是否需要进行权限处理（目标版本是否是23及以上）
     */
    @JvmStatic
    fun isPermissionTargetVersion(ctx: Context): Boolean {
        val packInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        return packInfo.applicationInfo.targetSdkVersion >= M
    }

    /**
     * 权限是否被允许
     * ps:因为方法可能被很多地方调用，因此不用内联，否则可能增加编译代码
     */
    @JvmStatic
    fun isPermissionGranted(ctx: Context, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= M) {
            // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
            if (isPermissionTargetVersion(ctx)) {
                return ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                return PermissionChecker.checkSelfPermission(ctx, permission) == PermissionChecker.PERMISSION_GRANTED
            }
        }
        // For Android < Android M, self permissions are always granted.
        return true
    }

    /**
     * [ps: 创建此方法主要是为了对Activity的shouldShowRequestPermissionRationale做更好的说明]
     * 如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
     * 如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
     * 如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
     * @param permission 权限
     * @exception IllegalArgumentException 如果传递的permission是当前设备无法识别的权限则会抛出 java.lang.IllegalArgumentException: Unknown permission: [permission]
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun shouldShowRequestPermissionRationale(ctx: Context, permission: String): Boolean {
        if (ctx is Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale(ctx, permission)
        } else {

            val packageManager = ctx.packageManager
            val pkManagerClass = packageManager.javaClass
            try {
                val method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String::class.java)
                if (!method.isAccessible) method.isAccessible = true
                return method.invoke(packageManager, permission) as Boolean
            } catch (e: Exception) {
                return false
            }
        }
    }

    @JvmStatic
    fun shouldShowRequestPermissionRationale(fragment: android.app.Fragment, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= M) {
            return fragment.shouldShowRequestPermissionRationale(permission)
        }
        return false
    }

    @JvmStatic
    fun shouldShowRequestPermissionRationale(fragment: android.support.v4.app.Fragment, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= M) {
            return fragment.shouldShowRequestPermissionRationale(permission)
        }
        return false
    }

    /**
     * Checks whether a particular permissions has been unable for a
     * package by policy. Typically the device owner or the profile owner
     * may apply such a policy. The user cannot grant policy unable
     * permissions, hence the only way for an app to get such a permission
     * is by a policy change.
     *
     * @return 权限是否被政策限制: Whether the permission is restricted by policy.
     */
    @JvmStatic
    @Deprecated(message = "不知道具体使用场景")
    fun isPermissionRevoked(ctx: Context, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= M) {
            return ctx.applicationContext.packageManager.isPermissionRevokedByPolicy(permission, ctx.packageName)
        }
        // For Android < Android M, 默认权限没有被政策限制
        return false
    }

}