package halo.android.permission.context

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import halo.android.v2.common.Util

/**
 * Created by Lucio on 18/4/4.
 */

class SupportFragmentContext(val fragment: Fragment) : PermissionContext {

    override val ctx: Context
        get() = fragment.requireContext()

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return Util.shouldShowRequestPermissionRationale(fragment, permission)
    }

    override fun startActivity(it: Intent) {
        fragment.startActivity(it)
    }

    override fun startActivityForResult(it: Intent, requestCode: Int) {
        fragment.startActivityForResult(it,requestCode)
    }
}