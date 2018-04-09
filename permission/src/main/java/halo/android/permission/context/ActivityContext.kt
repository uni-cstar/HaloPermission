package halo.android.permission.context

import android.app.Activity
import android.content.Intent

/**
 * Created by Lucio on 18/4/4.
 */

class ActivityContext(override val ctx: Activity) : PermissionContext {

    override fun startActivityForResult(it: Intent, requestCode: Int) {
        ctx.startActivityForResult(it, requestCode)
    }

}