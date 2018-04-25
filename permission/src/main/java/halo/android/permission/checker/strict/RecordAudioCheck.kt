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
import android.media.MediaRecorder
import java.io.File

/**
 * Created by Lucio on 18/4/25.
 * 录音权限检测
 */
class RecordAudioCheck(ctx: Context) : BaseCheck(ctx) {

    override fun check(): Boolean = tryCheck {
        var mr: MediaRecorder? = null
        var tempFile: File? = null
        try {
            mr = MediaRecorder()
            tempFile = File.createTempFile("RecordAudioCheck", "test")

            mr.run {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(tempFile.absolutePath)
                prepare()
                start()
            }
            return true
        } finally {//清除测试资源
            //释放MediaRecorder
            mr?.run {
                tryIgnore {
                    stop()
                }

                tryIgnore {
                    release()
                }
            }

            // 删除临时文件
            tempFile?.run {
                if (exists()) {
                    delete()
                }
            }
        }
    }
}
