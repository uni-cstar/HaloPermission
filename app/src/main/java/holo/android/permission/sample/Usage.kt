//package holo.android.permission.sample
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.widget.Toast
//import halo.android.permission.HoloPermission
//import halo.android.permission.caller.PermissionCaller
//import halo.android.permission.caller.PermissionResponder
//import halo.android.permission.checker.PermissionChecker
//import halo.android.permission.request.GrandAction
//import halo.android.permission.request.PermissionListener
//import halo.android.permission.request.RationaleRender
//import halo.android.permission.request.SettingRender
//
///**
// * Created by Lucio on 18/4/10.
// */
//
//class Usage : Activity() {
//    fun request() {
//        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE)
//                .setListener(object: PermissionListener{
//                    override fun onPermissionDenied(permissions: List<String>) {
//
//                    }
//                    override fun onPermissionGrand(permissions: List<String>) {
//                    }
//                }).run()
//
//        val permissions:Array<String> = arrayOf("","")
//        HoloPermission.with(this,*permissions)
//    }
//
//    fun request2() {
//        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setGrandAction(object:GrandAction{
//                    override fun onPermissionGrand(permissions: List<String>) {
//
//                    }
//
//                }).run()
//    }
//
//    fun request3() {
//        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setListener(object: PermissionListener{
//                    override fun onPermissionDenied(permissions: List<String>) {
//
//                    }
//                    override fun onPermissionGrand(permissions: List<String>) {
//                    }
//                })
//                .setRationaleRender("为了确保功能的正常使用，请允许接下来的权限请求申请。")
//                .run()
//    }
//
//    fun request4() {
//        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setListener(object: PermissionListener{
//                    override fun onPermissionDenied(permissions: List<String>) {
//
//                    }
//                    override fun onPermissionGrand(permissions: List<String>) {
//                    }
//                })
//                .setRationaleRender(object:RationaleRender{
//                    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
//                        Toast.makeText(ctx,"为了确保功能的正常使用，请允许接下来的权限请求申请。",Toast.LENGTH_SHORT).show()
//                        process.onNext()
//                    }
//                })
//                .run()
//    }
//
//    fun request5() {
//        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setListener(object: PermissionListener{
//                    override fun onPermissionDenied(permissions: List<String>) {
//
//                    }
//                    override fun onPermissionGrand(permissions: List<String>) {
//                    }
//                })
//                .setSettingRender("无法使用外部存储，请设置权限以便使用。")
//                .run()
//    }
//
//    fun request6() {
//        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setListener(object: PermissionListener{
//                    override fun onPermissionDenied(permissions: List<String>) {
//
//                    }
//                    override fun onPermissionGrand(permissions: List<String>) {
//                    }
//                })
//                .setSettingRender(object:SettingRender{
//                    override fun getCustomSettingIntent(ctx: Context): Intent? {
//                        return super.getCustomSettingIntent(ctx)
//                    }
//
//                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
//                        Toast.makeText(ctx,"为了确保功能的正常使用，请允许接下来的权限请求申请。",Toast.LENGTH_SHORT).show()
//                        process.onNext()
//                    }
//                })
//                .run()
//    }
//
//    class CustomChecker:PermissionChecker{
//        override fun isPermissionGranted(ctx: Context, permission: String): Boolean {
//            {}
//        }
//    }
//
//
//}