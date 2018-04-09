package halo.android.permission.processor

import android.content.Context
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.checker.PermissionChecker
import halo.android.permission.checker.StandardChecker
import halo.android.permission.common.RequestContext
import halo.android.permission.request.Request

/**
 * Created by Lucio on 18/4/4.
 *
 * 标准处理流程
 *
 * @param request 权限请求所包含的所有内容
 * @param checker 权限检查器
 * @param caller 权限发起请求调用器
 */
open class CommonProcessor(request: Request,
                           val checker: PermissionChecker,
                           caller: PermissionCaller) : BaseProcessor(request, caller) {

    override fun isPermissionGrantedForInitCheck(ctx: Context, permission: String): Boolean {
        return checker.isPermissionGranted(ctx, permission)
    }

    override fun isPermissionGrantedForPermissionResult(ctx: Context, permission: String): Boolean {
        return checker.isPermissionGranted(ctx, permission)
    }

}

