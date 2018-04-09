/**
 * Created by Lucio on 18/4/6.
 */
package halo.android.permission.setting

import android.content.Intent
import halo.android.permission.common.RequestContext


interface SettingResponder {

    fun onSettingResult(sender: RequestContext)

    fun getCustomSettingIntent(sender: RequestContext): Intent?
}