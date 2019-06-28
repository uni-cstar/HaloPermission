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

package halo.android.permission.spec

/**
 * Created by Lucio on 2019/6/24.
 */

interface SpecialListener {

    fun onSpecialGrand()

    fun onSpecialDeny()

    /**
     * 默认不做任何操作，只继续后续流程
     */
    fun showRationalView(process: Process){
        process.onSpecialNext()
    }


    interface Process {
        fun onSpecialNext()

        fun onSpecialCancel()
    }
}