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
        val settingIntent = SettingIntent.getCanResolvedSettingIntent(this, mResponder?.getCustomSettingIntent(this))
        if (settingIntent != null) {
            startActivityForResult(settingIntent, REQUEST_CODE)
        } else {
            //无法打开设置界面，回调以便继续后面流程
            Log.e("SettingRequestActivity", "no activity can handle this intent $settingIntent")
            notifySettingResult()
        }
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

    private fun notifySettingResult() {
        mResponder?.onSettingResult(this)
    }
}
