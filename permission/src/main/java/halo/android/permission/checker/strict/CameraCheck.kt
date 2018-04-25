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

import android.content.Context
import android.hardware.Camera

/**
 * Created by Lucio on 18/4/25.
 */
class CameraCheck(ctx: Context) : BaseCheck(ctx) {

    override fun check(): Boolean = tryCheck {
        var canUse = false
        var mCamera: Camera? = null

        try {
            mCamera = Camera.open(0)
            val mParameters = mCamera!!.parameters
            mCamera.parameters = mParameters
        } catch (e: Exception) {
            canUse = false
        }

        if (mCamera != null) {
            mCamera.release()
            canUse = true
        }
        return canUse
    }
}
