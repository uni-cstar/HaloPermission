package holo.android.permission.sample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import halo.android.permission.HaloPermission
import halo.android.permission.request.*
import java.io.File
import java.io.FileInputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fis: FileInputStream? = null
        try {
            val buildProperties = Properties()
            fis = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
            buildProperties.load(fis)
            val entries = buildProperties.entries

            for (item in entries){
                println("${item.key}  = ${item.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fis?.close()
            } catch (e: Exception) {
            }
        }
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
        HaloPermission.with(this)
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .setGrandAction(object : GrandAction {
                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许读取联系人")
                    }
                })
                .setDenyAction(object : DenyAction {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许读取联系人")
                    }
                })
                .setSettingRender(object : SettingRender {
                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("无法读取联系人权限，请设置，否则无法正常使用该功能。")
                                .setPositiveButton("设置", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext()
                                    }

                                })
                                .setNegativeButton("拒绝", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onCancel()
                                    }
                                })
                                .setOnCancelListener {
                                    process.onCancel()
                                }.show()
                    }
                })
                .run(true)
    }

    fun btn4Click(v: View?) {
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


    inline fun increment(i: Int): Int = i + 1

    val valValue: Int = 1
    var varValue: Int = 2
    val valObj: Activity = this
    var varObj: Activity = this

    inline fun inlineMethod(value: String) {
        println(value)
    }

    fun main() {
        val value = "a value"
        inlineMethod(value)
        println("after inline method")
    }

    fun testsdfd() {
        val i = 11
        if (i in 1..10) { // 等同于 1 <= i && i <= 10
            println(i)
        }
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