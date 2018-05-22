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
//import android.content.Context
//import android.content.Intent
//
//
///**
// * the request caller by activity
// * Activity形式实现的请求权限者
// */
// class PermissionOriginalRequest  {
//
//
//    companion object {
//
//        protected const val EXTRA_DATE = "extra_data"
//
//        private const val REQUEST_CODE = 0x1111
//
//        //静态属性  权限请求结果回调
//        @JvmStatic
//        private var mPermissionResponder: PermissionResponder? = null
//
//        /**
//         * 开始请求
//         */
//        @JvmStatic
//        internal fun startRequest(ctx: Context, responder: PermissionResponder, vararg permission: String) {
//            mPermissionResponder = responder
//            val it = Intent(ctx, PermissionOriginalRequest::class.java)
//            it.putExtra(EXTRA_DATE, permission)
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            ctx.startActivity(it)
//        }
//    }
//
//    //权限集合
//    private lateinit var mPermissions: Array<String>
//
//
//     fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
//        notifyPermissionResult(permissions)
//    }
//
//    //call back
//    private fun notifyPermissionResult(permissions: Array<out String>?) {
//        mPermissionResponder?.onPermissionResult(this, permissions)
//        this.finish()
//    }
//
//    override fun finish() {
//        super.finish()
//        // important to set  the static responder field null value
//        mPermissionResponder = null
//    }
//
//}
