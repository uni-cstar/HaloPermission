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

package halo.android.permission.common

import android.util.Log

/**
 * Created by Lucio on 2019/6/23.
 */
object PLog {

    const val TAG = "HaloPermission"

    private var isDebug: Boolean = true

    fun setDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    private inline fun debugRun(action: () -> Unit) {
        if (isDebug) {
            action()
        }
    }

    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        debugRun {
            Log.d(tag, msg)
        }
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        debugRun {
            Log.i(tag, msg)
        }
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        debugRun {
            Log.e(tag, msg)
        }
    }
}