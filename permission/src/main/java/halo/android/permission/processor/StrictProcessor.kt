///*
// * Copyright (C) 2018 Lucio
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package halo.android.permission.processor
//
//import android.content.Context
//import halo.android.permission.caller.PermissionCaller
//import halo.android.permission.checker.PermissionChecker
//import halo.android.permission.checker.StandardChecker23
//import halo.android.permission.checker.StrictChecker
//import halo.android.permission.request.BaseRequest
//import halo.android.permission.request.Request
//
///**
// * Created by Lucio on 18/4/25.
// * 严格权限校验流程
// * 即第一次使用常规权限检测判断，请求权限之后，用严格权限校验检测判断
// */
//class StrictProcessor(request: BaseRequest, caller: PermissionCaller) : BaseProcessor(request, caller) {
//
//
//    private val standardChecker: PermissionChecker by lazy {
//        StandardChecker23()
//    }
//
//    private val strictChecker: PermissionChecker by lazy {
//        StrictChecker()
//    }
//
//    override fun isPermissionGrantedForInitCheck(ctx: Context, permission: String) = isPermissionGranted(ctx, permission)
//
//    override fun isPermissionGrantedForPermissionResultCheck(ctx: Context, permission: String) = isPermissionGranted(ctx, permission)
//
//    private fun isPermissionGranted(ctx: Context, permission: String): Boolean {
//        return standardChecker.isPermissionGranted(ctx, permission) && strictChecker.isPermissionGranted(ctx, permission)
//    }
//
//}
//
