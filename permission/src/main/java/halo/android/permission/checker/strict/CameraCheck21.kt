/*
 * Copyright (C) 2018 Lucio
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
package halo.android.permission.checker.strict

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import android.support.annotation.RequiresPermission
import java.util.concurrent.CountDownLatch

/**
 * Created by Lucio on 18/4/25.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@Deprecated("异步检测结果，待测试")
class CameraCheck21(ctx: Context) : BaseCheck(ctx) {

    private var checkResult: Boolean = false

    private val lock: CountDownLatch = CountDownLatch(1)

    private val callBack = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            if (camera != null) {
                checkResult = true
                camera.close()
            }
            countDown()
        }

        override fun onDisconnected(camera: CameraDevice?) {
            checkResult = false
            countDown()
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            checkResult = error == CameraDevice.StateCallback.ERROR_CAMERA_IN_USE || error == CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE
            countDown()
        }
    }

    private inline fun countDown() {
        if (lock.count > 0) {
            lock.countDown()
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    override fun check(): Boolean {

        val cameraManager = ctx.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList
        if (cameraIds.isEmpty())
            return false
        val cameraId = cameraIds[0]
        cameraManager.openCamera(cameraId, callBack, null)

        lock.await()
        return checkResult
    }

}
