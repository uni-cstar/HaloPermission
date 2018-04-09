package halo.android.permission.common

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

/**
 * Created by Lucio on 18/4/9.
 */

internal abstract class DefaultAlertRender(val msg: String,
                                           val title: String? = null,
                                           val okText: String = "OK",
                                           val cancelText: String? = null) {

    fun buildAlert(ctx: Context): AlertDialog {
        val builder = AlertDialog.Builder(ctx).setMessage(msg)
        if (!title.isNullOrEmpty()) {
            builder.setTitle(title)
        }

        builder.setPositiveButton(okText, ::onPositiveButtonClick)

        if (!cancelText.isNullOrEmpty()) {
            builder.setNegativeButton(cancelText, ::onCancelButtonClick)
        }

        builder.setOnCancelListener(::onAlertCancelCallBack)
        return builder.create()
    }

    protected abstract fun onPositiveButtonClick(dialog: DialogInterface?, which: Int)

    protected abstract fun onCancelButtonClick(dialog: DialogInterface?, which: Int)

    protected abstract fun onAlertCancelCallBack(dialog: DialogInterface?)

}