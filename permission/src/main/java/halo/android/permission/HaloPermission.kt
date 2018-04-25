package halo.android.permission

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import halo.android.permission.context.ActivityContext
import halo.android.permission.context.AppContext
import halo.android.permission.context.FragmentContext
import halo.android.permission.context.SupportFragmentContext
import halo.android.permission.request.Request


/**
 * Created by Lucio on 18/4/4.
 */

object HaloPermission {

    @JvmStatic
    fun with(ctx: Context): Request {
        return Request(if (ctx is Activity) ActivityContext(ctx) else AppContext(ctx))
    }

    @JvmStatic
    fun with(fragment: android.app.Fragment): Request {
        return Request(FragmentContext(fragment))
    }

    @JvmStatic
    fun with(fragment: Fragment): Request {
        return Request(SupportFragmentContext(fragment))
    }

    @JvmStatic
    fun with(ctx: Context, vararg permission: String): Request {
        return with(ctx).setPermissions(*permission)
    }

    @JvmStatic
    fun with(fragment: android.app.Fragment, vararg permission: String): Request {
        return with(fragment).setPermissions(*permission)
    }

    @JvmStatic
    fun with(fragment: Fragment, vararg permission: String): Request {
        return with(fragment).setPermissions(*permission)
    }
}