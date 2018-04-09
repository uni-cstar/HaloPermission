package halo.android.permission.processor

import halo.android.permission.request.Request

/**
 * Created by Lucio on 18/4/5.
 */

/**
 * 权限检查流程处理器
 */
interface PermissionProcessor {

    val request: Request

    fun invoke()
}