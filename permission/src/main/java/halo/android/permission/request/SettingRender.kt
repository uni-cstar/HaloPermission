package halo.android.permission.request

import android.content.Context
import android.content.Intent

/**
 * Created by Lucio on 18/4/4.
 */

interface SettingRender {

    fun show(ctx: Context, permission: List<String>, process: Process)

    /**
     * 获取自定义权限设置界面
     * ps:用于发起方自定义权限设置界面Intent而不使用HoloPermission提供的settingIntent，但是不建议这么做。
     * HoloPermission提供的权限设置界面会尽可能的兼容更多的设备
     */
    fun getCustomSettingIntent(ctx: Context): Intent? = null

    interface Process {

        fun onNext(autoCheckWhenSettingResult: Boolean)

        fun onCancel()
    }
}
