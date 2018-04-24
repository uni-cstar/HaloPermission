/**
 * Created by Lucio on 18/4/17.
 *
 */
package halo.android.permission.checker

import android.content.Context
import android.support.v4.app.AppOpsManagerCompat
import halo.android.permission.common.Util

/**
 * 标准权限判断 + "application operation"
 */
class StandardChecker23 : PermissionChecker {

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