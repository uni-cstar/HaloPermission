/**
 * Created by Lucio on 18/4/6.
 */
package halo.android.permission.setting

import android.content.Context
import android.content.Intent

interface SettingResponder {

    fun onSettingResult()

    fun getCustomSettingIntent(ctx:Context): Intent?
}