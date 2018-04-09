package halo.android.permission.setting

import android.content.Context
import android.content.DialogInterface
import halo.android.permission.common.DefaultAlertRender
import halo.android.permission.request.SettingRender

/**
 * Created by Lucio on 18/4/9.
 */

internal class DefaultSettingRender(msg: String,
                                    title: String? = null,
                                    okText: String = "OK",
                                    cancelText: String? = null)
    : DefaultAlertRender(msg, title, okText, cancelText), SettingRender {

    private var mProcess: SettingRender.Process? = null

    override fun onPositiveButtonClick(dialog: DialogInterface?, which: Int) {
        mProcess?.onNext()
        mProcess = null
    }

    override fun onCancelButtonClick(dialog: DialogInterface?, which: Int) {
        mProcess?.onCancel()
        mProcess = null
    }

    override fun onAlertCancelCallBack(dialog: DialogInterface?) {
        mProcess?.onCancel()
        mProcess = null
    }

    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
        mProcess = process
        buildAlert(ctx).show()
    }

}