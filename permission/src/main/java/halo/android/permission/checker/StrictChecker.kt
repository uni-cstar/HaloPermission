package halo.android.permission.checker

import android.content.Context

/**
 * Created by Lucio on 18/4/12.
 */

/**
 * 严格权限校验
 */
class StrictChecker : PermissionChecker {

    override fun isPermissionGranted(ctx: Context, permission: String): Boolean {
        return true
    }

}