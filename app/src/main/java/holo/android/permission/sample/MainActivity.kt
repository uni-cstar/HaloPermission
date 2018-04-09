package holo.android.permission.sample

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import halo.android.permission.HoloPermission
import halo.android.permission.request.Action
import halo.android.permission.request.RationaleRender
import halo.android.permission.request.SettingRender

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btn1Click(v: View?) {
        HoloPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setGrandAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("允许读写外部存储")
                    }

                })
                .setDenyAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("不允许读写外部存储")
                    }
                })
                .build()
                .invoke()
    }



    fun btn2Click(v: View?) {
        HoloPermission.with(this)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setGrandAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("允许打电话")
                    }

                })
                .setDenyAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("不允许打电话")
                    }
                })
                .setRationaleRender(object:RationaleRender{
                    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("接下来请允许程序使用打电话权限，否则无法正常使用该功能。")
                                .setPositiveButton("好的",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext()
                                    }

                                })
                                .setNegativeButton("不了",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext()
                                    }
                                })
                                .setOnCancelListener {
                                    process.onCancel()
                                }.show()

                    }

                })
                .build()
                .invoke()
    }

    fun btn3Click(v: View?) {
        HoloPermission.with(this)
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .setGrandAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("允许读取联系人")
                    }

                })
                .setDenyAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("不允许读取联系人")
                    }
                })
                .setSettingRender(object:SettingRender{
                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("无法读取联系人权限，请设置，否则无法正常使用该功能。")
                                .setPositiveButton("设置",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext(false)
                                    }

                                })
                                .setNegativeButton("拒绝",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onCancel()
                                    }
                                })
                                .setOnCancelListener {
                                    process.onCancel()
                                }.show()
                    }
                })
                .build()
                .invoke()
    }

    fun btn4Click(v: View?) {
        HoloPermission.with(this)
                .setPermissions(Manifest.permission.CAMERA)
                .setGrandAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("允许使用照相机")
                    }

                })
                .setDenyAction(object:Action{
                    override fun invoke(permissions: List<String>) {
                        toast("不允许使用照相机")
                    }
                })
                .setRationaleRender(object:RationaleRender{
                    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("接下来请允许程序使用相机，否则无法正常使用该功能。")
                                .setPositiveButton("好的",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext()
                                    }

                                })
                                .setNegativeButton("不了",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext()
                                    }
                                })
                                .setOnCancelListener {
                                    process.onCancel()
                                }.show()

                    }

                })
                .setSettingRender(object:SettingRender{
                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("无法使用相机，请设置，否则无法正常使用该功能。")
                                .setPositiveButton("设置",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onNext(true)
                                    }

                                })
                                .setNegativeButton("拒绝",object:DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        process.onCancel()
                                    }
                                })
                                .setOnCancelListener {
                                    process.onCancel()
                                }.show()
                    }
                })
                .build()
                .invoke()
    }

    fun toast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}
