package halo.android.permission.processor

import halo.android.permission.request.BaseRequest

/**
 * Created by Lucio on 18/4/5.
 */

/**
 * 权限检查流程处理器
 */
interface PermissionProcessor {

    val request: BaseRequest

    fun invoke()
}