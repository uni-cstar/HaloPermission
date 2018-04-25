/**
 * Created by Lucio on 18/4/17.
 *
 */
package halo.android.permission.checker

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.ContextCompat
import halo.android.permission.common.Util

/**
 * 标准权限判断 + "application operation"
 */
class StandardChecker23 : PermissionChecker {

    override fun isPermissionGranted(ctx: Context, permission: String): Boolean {
        if (Build.VERSION.SDK_INT < Util.M)
            return true

        val isGranded: Boolean
        // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
        if (Util.isPermissionTargetVersion(ctx)) {
            isGranded = ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED
        } else {
            // targetSdkVersion < Android M, we have to use PermissionChecker
            isGranded = android.support.v4.content.PermissionChecker.checkSelfPermission(ctx, permission) == android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
        }

        if (isGranded) {
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