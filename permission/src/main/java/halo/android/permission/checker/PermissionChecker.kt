/**
 * Created by Lucio on 18/4/4.
 */
package halo.android.permission.checker

import android.content.Context


/**
 * 权限检查器
 * ps:目的是为了可以任意定制不同的规则决定权限是否被允许（比如23以前使用严格功能判断，之后使用23以上的方法判断等）
 */
interface PermissionChecker {
    fun isPermissionGranted(ctx: Context, permission: String): Boolean
}