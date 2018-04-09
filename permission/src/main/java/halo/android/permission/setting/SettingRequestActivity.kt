package halo.android.permission.setting

import android.content.Context
import android.content.Intent
import android.util.Log
import halo.android.permission.common.BaseRequestActivity


/**
 * the request caller by activity
 * Activity形式实现的请求权限者
 */
class SettingRequestActivity : BaseRequestActivity() {


    companion object {

        //静态属性  权限请求结果回调
        @JvmStatic
        private var mResponder: SettingResponder? = null

        @JvmStatic private val REQUEST_CODE = 0x1111

        /**
         * 开始请求
         */
        @JvmStatic
        internal fun startRequest(ctx: Context, responder: SettingResponder) {
            mResponder = responder
            val it = Intent(ctx, SettingRequestActivity::class.java)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(it)
        }
    }


    override fun onCreateInit() {
        var settingIntent = mResponder?.getCustomSettingIntent(this)
        if(settingIntent == null){
            settingIntent = SettingIntent.obtainSettingIntent(this)
        }

        if(settingIntent.resolveActivity(packageManager) == null){
            settingIntent = SettingIntent.default(this)
        }

        if(settingIntent.resolveActivity(packageManager) == null){
            Log.e("SettingRequestActivity","no activity can handle this intent $settingIntent")
        }
        startActivityForResult(settingIntent, REQUEST_CODE)
    }


    override fun onCreateException(e: Throwable) {
        notifySettingResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            notifySettingResult()
        }
    }

    private fun notifySettingResult(){
        mResponder?.onSettingResult(this)
    }
}
