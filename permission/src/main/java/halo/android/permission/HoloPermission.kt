package halo.android.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import halo.android.permission.context.ActivityContext
import halo.android.permission.context.AppContext
import halo.android.permission.context.FragmentContext
import halo.android.permission.context.SupportFragmentContext
import halo.android.permission.request.Request


/**
 * Created by Lucio on 18/4/4.
 */

object HoloPermission {

    fun with(ctx: Context): Request {
        return Request(if (ctx is Activity) ActivityContext(ctx) else AppContext(ctx))
    }

    fun with(fragment: android.app.Fragment): Request {
        return Request(FragmentContext(fragment))
    }

    fun with(fragment: Fragment): Request {
        return Request(SupportFragmentContext(fragment))
    }

    fun with(ctx: Context, vararg permission: String): Request {
        return with(ctx).setPermissions(*permission)
    }

    fun with(fragment: android.app.Fragment, vararg permission: String): Request {
        return with(fragment).setPermissions(*permission)
    }

    fun with(fragment: Fragment, vararg permission: String): Request {
        return with(fragment).setPermissions(*permission)
    }
}