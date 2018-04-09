/**
 * Created by Lucio on 18/4/6.
 */
package halo.android.permission.caller

import android.content.Context

/**
 * 权限请求发起者
 * ps:目的是可以配置使用Activity或者Fragment，或者其它的方式向系统请求权限
 */
interface PermissionCaller {

    /**
     * @param responder 回调
     * @param permision 权限
     */
    fun requestPermission(ctx: Context, responder: PermissionResponder, vararg permision: String)

}