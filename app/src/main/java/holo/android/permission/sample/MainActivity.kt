package holo.android.permission.sample

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import halo.android.permission.HaloPermission
import halo.android.permission.HaoloSpecPermission
import halo.android.permission.common.PLog
import halo.android.permission.request.DefaultRationaleRender
import halo.android.permission.request.DefaultSettingRender
import halo.android.permission.request.PermissionListener
import halo.android.permission.request.RationaleRender
import halo.android.permission.setting.SettingRender
import halo.android.permission.spec.SpecialListener
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {

    private val NOTIFICATION_CHANEL = "default"

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

    fun callPhone() {
        val it = Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"))
        startActivity(it)
    }

    fun btn1Click(v: View?) {
        HaloPermission.newRequest(this, Manifest.permission.CALL_PHONE)
                .setListener(object : PermissionListener {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许打电话")

                    }

                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许打电话")
                        callPhone()
                    }

                }).request(this)
    }


    fun btn2Click(v: View?) {
        HaloPermission.newRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleRender(DefaultRationaleRender("为保障功能的正常使用，请允许程序访问外部存储。",
                        "温馨提示", "确定", "取消"))
                .setListener(object : PermissionListener {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许访问外部存储")

                    }

                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许访问外部存储")
                        callPhone()
                    }

                }).request(this)
    }

    fun btn3Click(v: View?) {
        HaloPermission.newRequest(this, Manifest.permission.CAMERA)
                .setSettingRender(DefaultSettingRender("无法调用相机，请允许权限。",
                        "温馨提示", "设置", "取消"))
                .setListener(object : PermissionListener {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("不允许使用相机")

                    }

                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("允许使用相机")
                        callPhone()
                    }

                }).request(this)
    }

    fun btn4Click(v: View?) {
        HaloPermission.newRequest(this, Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS)
                .setRationaleRender(object:RationaleRender{
                    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
                        AlertDialog.Builder(ctx)
                                .setMessage("这是一个权限申请解释：为了更好的使用功能，请允许接下来的权限申请")
                                .setPositiveButton("确认") { dialog, which ->
                                    process.onNext()
                                }
                                .setNegativeButton("取消") { dialog, which ->
                                    process.onCancel()
                                }
                                .setOnCancelListener {
                                    process.onCancel()
                                }
                                .create().show()
                    }

                })
                .setSettingRender(object:SettingRender{
                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
                        var alertMsg = ""
                        if(permission.contains(Manifest.permission.READ_CONTACTS) ){
                            if(permission.contains(Manifest.permission.SEND_SMS)){
                                alertMsg ="无法读取联系人和发送短信息，请允许权限"
                            }else{
                                alertMsg ="无法读取联系人，请允许权限"
                            }
                        }else if(permission.contains(Manifest.permission.SEND_SMS)){
                            alertMsg ="无法发送短信息，请允许权限"
                        }
                        AlertDialog.Builder(ctx)
                                .setMessage(alertMsg)
                                .setPositiveButton("设置") { dialog, which ->
                                    process.onNext(true)
                                }
                                .setNegativeButton("取消") { dialog, which ->
                                    process.onCancel()
                                }
                                .setOnCancelListener {
                                    process.onCancel()
                                }
                                .create().show()
                    }
                })

                .setListener(object : PermissionListener {
                    override fun onPermissionDenied(permissions: List<String>) {
                        toast("权限申请失败${permissions.joinToString(",")}")
                    }

                    override fun onPermissionGrand(permissions: List<String>) {
                        toast("权限申请成功")
                    }

                }).request(this)
    }

    private fun tryUi(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            toast(e.message.orEmpty())
        }
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun btnUnkonwnSourceAppClick(v: View) {
        try {
            HaoloSpecPermission.requestAppUnknownSource(this, object : SpecialListener {
                override fun onSpecialGrand() {
                    toast("允许未知来源")
                }

                override fun onSpecialDeny() {
                    toast("禁止未知来源")
                }

                override fun showRationalView(process: SpecialListener.Process) {
                    AlertDialog.Builder(this@MainActivity)
                            .setMessage("无法安装程序，为了能够正常的安装程序，请允许接下来请求的设置")
                            .setPositiveButton("设置") { dialog, which ->
                                process.onSpecialNext()
                            }
                            .setNegativeButton("取消") { dialog, which ->
                                process.onSpecialCancel()
                            }
                            .setOnCancelListener {
                                process.onSpecialCancel()
                            }
                            .create().show()
                }
            })
        } catch (e: Exception) {
            PLog.d("btnUnkonwnSourceAppClick:${e.message}")
        }
    }

    fun btnNotificationClick(v: View) {
        tryUi {
            HaoloSpecPermission.requestNotification(this, object : SpecialListener {
                override fun onSpecialGrand() {
                    toast("允许通知")
                    showNotification()
                }

                override fun onSpecialDeny() {
                    toast("禁止通知")
                }
            })
        }

    }

    fun btnNotificationChannelClick(v: View) {
        tryUi {
            HaoloSpecPermission.requestNotificationChanel(this, NOTIFICATION_CHANEL, object : SpecialListener {
                override fun onSpecialGrand() {
                    toast("允许通知渠道${NOTIFICATION_CHANEL}")
                }

                override fun onSpecialDeny() {
                    toast("禁止通知渠道${NOTIFICATION_CHANEL}")
                }

            })
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun showNotification() {

        tryUi {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            var channelEnabled = HaoloSpecPermission.areNotificationChannelsEnabled(this, NOTIFICATION_CHANEL)
            if(!channelEnabled){
                val channel = NotificationChannel(
                        NOTIFICATION_CHANEL,
                        "你有新的消息",
                        NotificationManager.IMPORTANCE_DEFAULT

                )
                notificationManager.createNotificationChannel(channel)
            }

            channelEnabled = HaoloSpecPermission.areNotificationChannelsEnabled(this, NOTIFICATION_CHANEL)

            if(!channelEnabled)
                throw RuntimeException("当前通知渠道不可用，无法进行通知，可以点击通知渠道按钮申请")

            val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANEL)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("title")
                    .setContentText("content")
                    .build()
            notificationManager.notify(1, notification)
        }

    }

    fun btnDrawOverlaysSettingClick(v: View) {
        HaoloSpecPermission.requestDrawOverlays(this, object : SpecialListener {
            override fun onSpecialGrand() {
                toast("允许悬浮窗")
                addOverlays()
            }

            override fun onSpecialDeny() {
                toast("禁止悬浮窗")
            }
        })
    }

    private fun addOverlays() {
        val view = TextView(this)
                .apply {
                    text = "这是一个悬浮窗"
                    setBackgroundResource(android.R.color.black)
                    setTextColor(resources.getColor(android.R.color.white))
                    setOnClickListener {
                        windowManager.removeView(this)
                    }
                }
        val lp = WindowManager.LayoutParams()
                .apply {
                    width = WindowManager.LayoutParams.WRAP_CONTENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                }
        windowManager.addView(view, lp)
    }

    fun btnWriteSystemSettingClick(v: View) {
        HaoloSpecPermission.requestWriteSystemSetting(this, object : SpecialListener {
            override fun onSpecialGrand() {
                toast("允许修改系统设置")
            }

            override fun onSpecialDeny() {
                toast("禁止修改系统设置")
            }
        })
    }

}