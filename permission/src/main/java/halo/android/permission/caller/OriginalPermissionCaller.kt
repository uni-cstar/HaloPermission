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
//package halo.android.permission.caller
//
//import android.app.Activity
//import android.app.Fragment
//import android.content.Context
//import android.os.Build
//import android.support.v4.app.ActivityCompat
//import java.lang.ref.SoftReference
//
///**
// * Created by Lucio on 2018/5/22.
// */
//
//abstract class BaseOriginalPermissionCaller(val requestCode: Int) : PermissionCaller {
//
//    internal companion object {
//
//        //静态属性  权限请求结果回调
//        private var mCallerRef: SoftReference<BaseOriginalPermissionCaller>? = null
//
//        internal fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
//            mCallerRef?.get()?.run {
//                onRequestPermissionsResult(requestCode, permissions, grantResults)
//            }
//            mCallerRef?.clear()
//            mCallerRef = null
//        }
//
//    }
//
//    protected var responderRef: SoftReference<PermissionResponder>? = null
//
//    override fun requestPermission(ctx: Context, responder: PermissionResponder, vararg permision: String) {
//        responderRef = SoftReference(responder)
//        mCallerRef = SoftReference(this)
//        requestPermission(*permision)
//    }
//
//    protected abstract fun requestPermission(vararg permision: String)
//
//    internal fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
//        if (requestCode != this.requestCode)
//            return
//        notifyResult(permissions)
//    }
//
//    protected fun notifyResult(permissions: Array<out String>?) {
//        responderRef?.get()?.run {
//            onPermissionResult(permissions)
//        }
//    }
//
//}
//
//class OriginalActivityCaller @JvmOverloads constructor(activity: Activity, requestCode: Int = 0x11) : BaseOriginalPermissionCaller(requestCode) {
//
//    private var activityRef: SoftReference<Activity> = SoftReference(activity)
//
//    override fun requestPermission(vararg permision: String) {
//        activityRef.get()?.run {
//            ActivityCompat.requestPermissions(this, permision, requestCode)
//        }
//    }
//}
//
//class OriginalFragmentCaller @JvmOverloads constructor(fragment: Fragment, requestCode: Int = 0x11) : BaseOriginalPermissionCaller(requestCode) {
//
//    private var fragmentRef = SoftReference(fragment)
//
//    override fun requestPermission(vararg permision: String) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            fragmentRef.get()?.requestPermissions(permision, requestCode)
//        } else {
//            notifyResult(permision)
//        }
//    }
//}
//
//class OriginalSupportFragmentCaller @JvmOverloads constructor(fragment: android.support.v4.app.Fragment, requestCode: Int = 0x11) : BaseOriginalPermissionCaller(requestCode) {
//
//    private var fragmentRef = SoftReference(fragment)
//
//    override fun requestPermission(vararg permision: String) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            fragmentRef.get()?.requestPermissions(permision, requestCode)
//        } else {
//            notifyResult(permision)
//        }
//    }
//}