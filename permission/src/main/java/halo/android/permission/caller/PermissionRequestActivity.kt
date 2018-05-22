package halo.android.permission.caller

import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.util.Log
import halo.android.permission.common.BaseRequestActivity


/**
 * the request caller by activity
 * Activity形式实现的请求权限者
 */
class PermissionRequestActivity : BaseRequestActivity() {


    companion object {

        protected const val EXTRA_DATE = "extra_data"

        private const val REQUEST_CODE = 0x1111

        //静态属性  权限请求结果回调
        @JvmStatic
        private var mPermissionResponder: PermissionResponder? = null

        /**
         * 开始请求
         */
        @JvmStatic
        internal fun startRequest(ctx: Context, responder: PermissionResponder, vararg permission: String) {
            mPermissionResponder = responder
            val it = Intent(ctx, PermissionRequestActivity::class.java)
            it.putExtra(EXTRA_DATE, permission)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(it)
        }
    }

    //权限集合
    private lateinit var mPermissions: Array<String>

    override fun onCreateInit() {
        mPermissions = intent.getStringArrayExtra(EXTRA_DATE)
        if (mPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, mPermissions, REQUEST_CODE)
        } else {//no permission，finish activity
            finish()
        }
    }

    override fun onCreateException(e: Throwable) {
        Log.d("PermissionRequestActivity", "onCreate raise exception", e)
        notifyPermissionResult(mPermissions)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        notifyPermissionResult(permissions)
    }

    //call back
    private fun notifyPermissionResult(permissions: Array<out String>?) {
        mPermissionResponder?.onPermissionResult(permissions)
        this.finish()
    }

    override fun finish() {
        super.finish()
        // important to set  the static responder field null value
        mPermissionResponder = null
    }

}
