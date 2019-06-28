//package halo.android.permission
//
//import android.app.Activity
//import android.content.Context
//import android.support.v4.app.Fragment
//import halo.android.permission.caller.*
//import halo.android.permission.context.ActivityContext
//import halo.android.permission.context.AppContext
//import halo.android.permission.context.FragmentContext
//import halo.android.permission.context.SupportFragmentContext
//import halo.android.permission.request.OriginalRequest
//import halo.android.permission.request.Request
//
//
///**
// * Created by Lucio on 18/4/4.
// */
//
//object HaloPermission {
//
//    @JvmStatic
//    fun with(ctx: Context, vararg permission: String): Request {
//        val request = Request(if (ctx is Activity) ActivityContext(ctx) else AppContext(ctx))
//        request.setPermissions(*permission)
//        return request
//    }
//
//    @JvmStatic
//    fun with(fragment: android.app.Fragment, vararg permission: String): Request {
//        val request = Request(FragmentContext(fragment))
//        request.setPermissions(*permission)
//        return request
//    }
//
//    @JvmStatic
//    fun with(fragment: Fragment, vararg permission: String): Request {
//        val request = Request(SupportFragmentContext(fragment))
//        request.setPermissions(*permission)
//        return request
//    }
//
//
//    /**
//     * for original request
//     */
//    @JvmStatic
//    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
//        BaseOriginalPermissionCaller.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    @JvmStatic
//    @JvmOverloads
//    fun original(activity: Activity, vararg permission: String, requestCode: Int = 0x111): OriginalRequest {
//        val request = OriginalRequest(activity, requestCode)
//        request.setPermissions(*permission)
//        return request
//    }
//
//    @JvmStatic
//    @JvmOverloads
//    fun original(fragment: android.app.Fragment, vararg permission: String, requestCode: Int = 0x111): OriginalRequest {
//        val request = OriginalRequest(fragment, requestCode)
//        request.setPermissions(*permission)
//        return request
//    }
//
//    @JvmStatic
//    @JvmOverloads
//    fun original(fragment: Fragment, vararg permission: String, requestCode: Int = 0x111): OriginalRequest {
//        val request = OriginalRequest(fragment, requestCode)
//        request.setPermissions(*permission)
//        return request
//    }
//
//    fun originalCaller(fragment: Fragment, requestCode: Int = 0x11): PermissionCaller {
//        return OriginalSupportFragmentCaller(fragment, requestCode)
//    }
//
//    fun originalCaller(fragment: android.app.Fragment, requestCode: Int = 0x11): PermissionCaller {
//        return OriginalFragmentCaller(fragment, requestCode)
//    }
//
//    fun originalCaller(activity: Activity, requestCode: Int = 0x11): PermissionCaller {
//        return OriginalActivityCaller(activity, requestCode)
//    }
//
//}