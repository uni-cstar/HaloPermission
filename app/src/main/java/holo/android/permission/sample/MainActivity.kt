package holo.android.permission.sample

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.yanzhenjie.permission.AndPermission
import halo.android.permission.HaloPermission
import halo.android.permission.request.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        var fis: FileInputStream? = null
//        try {
//            val buildProperties = Properties()
//            fis = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
//            buildProperties.load(fis)
//            val entries = buildProperties.entries
//
//            for (item in entries){
//                println("${item.key}  = ${item.value}")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                fis?.close()
//            } catch (e: Exception) {
//            }
//        }
    }


    fun btn1Click(v: View?) {

        HaloPermission.with(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setListener(object : PermissionListener {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许读写外部存储")
                    }

                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许读写外部存储")
                    }

                })
                .run(true)
    }


    fun btn2Click(v: View?) {
        HaloPermission.with(this)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setListener(object : PermissionListener {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许打电话")
                    }

                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许打电话")
                    }

                })
                .setRationaleRender("为保障功能的正常使用，请允许程序使用打电话功能。")
                .run(true)
    }

    fun btn3Click(v: View?) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),11)
//        HaloPermission.original(this,Manifest.permission.READ_CONTACTS)
//                .setGrandAction(object : GrandAction {
//                    override fun onPermissionGrand(permissions: List<String>) {
//                        toast("允许读取联系人")
//                    }
//                })
//                .setDenyAction(object : DenyAction {
//                    override fun onPermissionDenied(permissions: List<String>) {
//                        toast("不允许读取联系人")
//                    }
//                })
//                .setSettingRender(object : SettingRender {
//                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
//
//                        print("hashcode = ${ctx.hashCode() == this@MainActivity.hashCode()} ")
//                        val fd = DialogFragment()
//                        fd.show(this@MainActivity.supportFragmentManager, "tt")
////                        AlertDialog.Builder(this@MainActivity)
////                                .setMessage("无法读取联系人权限，请设置，否则无法正常使用该功能。")
////                                .setPositiveButton("设置", object : DialogInterface.OnClickListener {
////                                    override fun onClick(dialog: DialogInterface?, which: Int) {
////                                        process.onNext()
////                                    }
////
////                                })
////                                .setNegativeButton("拒绝", object : DialogInterface.OnClickListener {
////                                    override fun onClick(dialog: DialogInterface?, which: Int) {
////                                        process.onCancel()
////                                    }
////                                })
////                                .setOnCancelListener {
////                                    process.onCancel()
////                                }.show()
//                    }
//                })
//                .run(true)
    }


    fun andPermission() {

        AndPermission.with(this)
                .permission(Manifest.permission.CAMERA)
                .rationale { context, permissions, executor ->
                    AlertDialog.Builder(this@MainActivity)
                            .setMessage("接下来请允许程序使用相机，否则无法正常使用该功能。")
                            .setPositiveButton("好的", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    executor.execute()
                                }

                            })
                            .setNegativeButton("不了", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    executor.cancel()
                                }
                            })
                            .setOnCancelListener {
                                executor.cancel()
                            }.show()
                }
                .onDenied {
                    toast("不允许使用照相机")
                }
                .onGranted {
                    toast("允许使用照相机")
                }.start()

    }

//    fun permission4MCamera() {
//        Permissions4M.get(this)
//                .requestForce(true)
//                .requestUnderM(true)
//                .requestPermissions(Manifest.permission.CAMERA)
//                .requestCodes(11)
//                .requestListener(object : ListenerWrapper.PermissionRequestListener {
//                    override fun permissionDenied(p0: Int) {
//                        toast("不允许使用照相机")
//                    }
//
//                    override fun permissionRationale(p0: Int) {
//                        toast("permissionRationale")
//                    }
//
//                    override fun permissionGranted(p0: Int) {
//                        toast("允许使用照相机")
//                    }
//                })
//                // 二次请求时回调
//                .requestCustomRationaleListener(object : ListenerWrapper.PermissionCustomRationaleListener {
//
//                    override fun permissionCustomRationale(code: Int) {
//                        AlertDialog.Builder(this@MainActivity)
//                                .setMessage("接下来请允许程序使用相机，否则无法正常使用该功能。")
//                                .setPositiveButton("好的", object : DialogInterface.OnClickListener {
//                                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                                        Permissions4M.get(this@MainActivity)
//                                                .requestOnRationale()
//                                                .requestPermissions(Manifest.permission.READ_PHONE_STATE)
//                                                .requestCodes(12)
//                                                .request()
//                                    }
//
//                                })
//                                .setNegativeButton("不了", object : DialogInterface.OnClickListener {
//                                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                                    }
//                                })
//                                .setOnCancelListener {
//                                }.show()
//                    }
//                })
//                // 权限完全被禁时回调函数中返回 intent 类型（手机管家界面）
//                .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
//                // 权限完全被禁时回调函数中返回 intent 类型（系统设置界面）
//                //.requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
//                // 权限完全被禁时回调，接口函数中的参数 Intent 是由上一行决定的
//                .requestPage(object : ListenerWrapper.PermissionPageListener {
//                    override fun pageIntent(p0: Int, intent: Intent?) {
//                        AlertDialog.Builder(this@MainActivity)
//                                .setMessage("读取通讯录权限申请：\n我们需要您开启读取通讯录权限(in activity with listener)")
//                                .setPositiveButton("好的", object : DialogInterface.OnClickListener {
//                                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                                        startActivity(intent);
//                                    }
//
//                                })
//                                .setNegativeButton("不了", object : DialogInterface.OnClickListener {
//                                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                                    }
//                                })
//                                .setOnCancelListener {
//                                }.show()
//                    }
//                })
//                .request()
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        Permissions4M.onRequestPermissionsResult(this, requestCode, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        HaloPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fd = DialogFragment()
        fd.show(this@MainActivity.supportFragmentManager, "tt")
    }

    fun btn4Click(v: View?) {
//        permission4MCamera()
//        permission4MCamera()
        HaloPermission.with(this)
                .setPermissions(Manifest.permission.CAMERA)
                .setGrandAction(object : GrandAction {
                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许使用照相机")
                    }

                })
                .setDenyAction(object : DenyAction {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许使用照相机")
                    }
                })
                .setRationaleRender(object : RationaleRender {
                    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("接下来请允许程序使用相机，否则无法正常使用该功能。")
                                .setPositiveButton("好的", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext()
                                    }

                                })
                                .setNegativeButton("不了", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onCancel()
                                    }
                                })
                                .setOnCancelListener {
                                    process.onCancel()
                                }.show()

                    }

                })
                .setSettingRender("无法使用相机，请设置，否则无法正常使用该功能。")
                .run(true)
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}

//interface IView{
//    fun onUserInfoBack(any:Any)
//}
//class Presenter(val view:IView){
//    fun requestUserInfo(){
//        Observable.just(Any())
//                .subscribe(object:Action{
//                    fun onNext(any:Any){
//                        view.onUserInfoBack(any)
//                    }
//                })
//    }
//}
//
//class AScene: Activity(),IView{
//    var mInfo:Any? = null
//
//    override fun onUserInfoBack(any: Any) {
//        mInfo = any
//    }
//
//    val mP: Presenter = Presenter(this)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mP.requestUserInfo()
//    }
//
//    fun onBtnClick(){
//        if(mInfo == null){
//            //数据没准备好
//        }else {
//            //跳转到B界面
//        }
//    }
//}