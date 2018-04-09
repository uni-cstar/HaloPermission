/**
 * Created by Lucio on 18/4/6.
 */
package halo.android.permission.setting

import android.content.Context

/**
 * 使用Activity方式实现的权限请求发起者
 */
class SettingPermissionCaller : SettingCaller {

    override fun requestPermissionSetting(ctx: Context, responder: SettingResponder) {
        SettingRequestActivity.startRequest(ctx, responder)
    }

}