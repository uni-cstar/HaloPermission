package halo.android.permission.context

import android.content.Context
import android.app.Fragment
import android.content.Intent
import halo.android.permission.common.Util

/**
 * Created by Lucio on 18/4/4.
 */

class FragmentContext(val fragment: Fragment) : PermissionContext {

    override val ctx: Context
        get() = fragment.activity

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