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
import android.os.Environment
import java.io.File


/**
 * Created by Lucio on 18/4/24.
 * 检测外置存储卡是否可写
 */

class WriteExternalStorageCheck(ctx: Context) : BaseCheck(ctx) {

    override fun check(): Boolean = tryCheck {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED)
        // sd卡不存在
            return false

        val directory = Environment.getExternalStorageDirectory()
        if (!directory.exists() || !directory.canWrite())
            return false

        val testFile = File(directory, "permission_check.txt")
        if (testFile.exists()) {
            return testFile.delete()
        } else {
            return testFile.createNewFile()
        }
    }
}