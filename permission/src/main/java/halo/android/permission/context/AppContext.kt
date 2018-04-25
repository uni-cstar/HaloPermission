package halo.android.permission.context

import android.content.Context
import android.content.Intent

/**
 * Created by Lucio on 18/4/4.
 */

class AppContext(override val ctx: Context) : PermissionContext {

    override fun startActivityForResult(it: Intent, requestCode: Int) {
        startActivity(it)
    }
}