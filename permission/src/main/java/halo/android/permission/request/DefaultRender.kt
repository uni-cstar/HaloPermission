/*
 * Copyright (C) 2019 Lucio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package halo.android.permission.request

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import halo.android.permission.setting.SettingRender

/**
 * Created by Lucio on 2019/6/23.
 */

 abstract class DefaultAlertRender(val msg: String,
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

/**
 * 默认的RationalRender
 */
 class DefaultRationaleRender(msg: String,
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

/**
 * 默认的SettingRender实现
 */
 class DefaultSettingRender(msg: String,
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