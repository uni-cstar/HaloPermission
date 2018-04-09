package halo.android.permission.context

import android.content.Context
import android.content.Intent
import halo.android.permission.common.Util

/**
 * Created by Lucio on 18/4/4.
 */

/**
 * 权限请求上下文
 * @see [ActivityContext]、[SupportFragmentContext]、[FragmentContext]、[AppContext]
 */
interface PermissionContext {

    val ctx: Context

    fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return Util.shouldShowRequestPermissionRationale(ctx, permission)
    }

    fun startActivity(it:Intent){
        ctx.startActivity(it)
    }

    abstract fun startActivityForResult(it:Intent,requestCode:Int)

}