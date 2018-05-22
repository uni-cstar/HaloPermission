/**
 * Created by Lucio on 18/4/6.
 */
package halo.android.permission.caller

/**
 * 权限请求结果回调
 * ps:用于接收向系统请求权限的结果回调
 */
interface PermissionResponder {

    fun onPermissionResult(permissions: Array<out String>?)

}