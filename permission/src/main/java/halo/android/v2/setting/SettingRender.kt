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

package halo.android.v2.setting

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import halo.android.v2.request.DefaultAlertRender

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

        /**
         * @param autoCheckWhenSettingResult 当进入权限设置界面之后返回，是否再自动检测权限
         */
        fun onNext(autoCheckWhenSettingResult: Boolean = true)

        fun onCancel()
    }
}

/**
 * 默认的SettingRender实现
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