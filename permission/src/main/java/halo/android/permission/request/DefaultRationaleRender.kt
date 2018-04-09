package halo.android.permission.request

import android.content.Context
import android.content.DialogInterface
import halo.android.permission.common.DefaultAlertRender

/**
 * Created by Lucio on 18/4/9.
 */

internal class DefaultRationaleRender(msg: String,
                                      title: String? = null,
                                      okText: String = "OK",
                                      cancelText: String? = null)
    : DefaultAlertRender(msg, title, okText, cancelText), RationaleRender {

    private var mProcess: RationaleRender.Process? = null

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


    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
        mProcess = process
        buildAlert(ctx).show()
    }

}