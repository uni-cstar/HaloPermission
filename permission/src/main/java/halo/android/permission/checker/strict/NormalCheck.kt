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
import halo.android.permission.checker.StandardChecker23

/**
 * Created by Lucio on 18/4/24.
 */

class NormalCheck(ctx:Context,val permission:String) : BaseCheck(ctx){
    override fun check(): Boolean {
        return StandardChecker23().isPermissionGranted(ctx,permission)
    }

}