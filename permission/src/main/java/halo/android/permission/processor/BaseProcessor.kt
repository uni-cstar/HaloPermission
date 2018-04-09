package halo.android.permission.processor

import android.content.Context
import android.content.Intent
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.caller.PermissionResponder
import halo.android.permission.common.RequestContext
import halo.android.permission.request.RationaleRender
import halo.android.permission.request.Request
import halo.android.permission.request.SettingRender
import halo.android.permission.setting.SettingCaller
import halo.android.permission.setting.SettingPermissionCaller
import halo.android.permission.setting.SettingResponder

/**
 * Created by Lucio on 18/4/4.
 *
 * 标准处理流程
 *
 * @param request 权限请求所包含的所有内容
 * @param checker 权限检查器
 * @param caller 权限发起请求调用器
 */
abstract class BaseProcessor(override val request: Request,
                             val caller: PermissionCaller)
    : PermissionProcessor, PermissionResponder, SettingResponder {


    //被拒绝的权限集合
    private var mDenidPermissons: MutableList<String> = mutableListOf()


    /**
     * 用于[invoke]方法中判断权限是否被允许,即初次校验权限是否被允许
     * @see [invoke]
     */
    protected abstract fun isPermissionGrantedForInitCheck(ctx: Context, permission: String): Boolean

    /**
     * 用于[onPermissionResult]方法中判断权限是否被允许，即用于校验请求系统之后，权限是否被允许
     * @see [onPermissionResult]
     */
    protected abstract fun isPermissionGrantedForPermissionResult(ctx: Context, permission: String): Boolean

    /**
     * 校验请求
     */
    override fun invoke() {
        invoke(false)
    }

    /**
     * 是否忽略检查RationalePermission：用于在设置权限界面关闭之后，重新校验权限，但不用再次询问Rationale部分的处理
     */
    fun invoke(ignoreRationalCheck: Boolean) {
        val permissions = request.getPermissions()

        mDenidPermissons.clear()
        //过滤被拒绝的权限
        permissions.filterTo(mDenidPermissons) {
            !isPermissionGrantedForInitCheck(request.getContext(), it)
        }

        if (mDenidPermissons.isNotEmpty()) {
            if (ignoreRationalCheck) {
                requestPermissionsReal()
            } else {
                //过滤rationnale权限
                val rationalePermissions = mDenidPermissons.filter {
                    request.permissionContext.shouldShowRequestPermissionRationale(it)
                }

                if (rationalePermissions.isNotEmpty()) {
                    notifyRationaleView(rationalePermissions)
                } else {
                    requestPermissionsReal()
                }
            }
        } else {
            notifyPermissionSucceed()
        }
    }

    /**
     * 权限请求成功
     */
    protected fun notifyPermissionSucceed() {
        request.getGrandAction()?.invoke(request.getPermissions().toList())
    }

    /**
     * 权限请求失败
     */
    protected open fun notifyPermissionFailed() {
        request.getDenyAction()?.invoke(mDenidPermissons)
    }

    /**
     * 请求权限
     */
    private fun requestPermissionsReal() {
        caller.requestPermission(request.getContext(), this, *request.getPermissions())
    }

    /**
     * 请求权限设置界面
     */
    private fun requestSettingReal() {
        mSettingCaller.requestPermissionSetting(request.getContext(), this)
    }


    /**
     * 处理RationaleView
     */
    private fun notifyRationaleView(permissions: List<String>) {
        val rationaleView = request.getRationaleRender()
        if (rationaleView != null) {
            //不为空则显示RationaleView
            rationaleView.show(request.permissionContext.ctx, permissions, mRationaleProcess)
        } else {
            notifySettingRender()
//            //为空则视为RationaleView取消流程（因为存在用户无法获取的权限，则回调deny告知客户端）
//            notifyPermissionFailed(mDenidPermissons)
        }
    }

    /**
     * 控制SettingRender只显示一次
     */
    private var isSettingRenderDone = false

    /**
     * 控制在设置界面关闭之后，是否自动重新检查权限
     */
    private var autoCheckWhenSettingResult: Boolean = true

    /**
     * 处理权限设置SettingRende
     */
    private fun notifySettingRender() {
        val settingRender = request.getSettingRender()
        if (settingRender != null && !isSettingRenderDone) {
            isSettingRenderDone = true
            settingRender.show(request.getContext(), mDenidPermissons, mSettingProcess)
        } else {
            notifyPermissionFailed()
        }
    }

    /**
     * [PermissionResponder]回调 处理请求的权限结果返回
     */
    override fun onPermissionResult(sender: RequestContext, permissions: Array<out String>?) {
        //关闭上下文
        sender.finish()
        //过滤被拒绝的权限
        val denyPermissions = permissions?.filter {
            !isPermissionGrantedForPermissionResult(request.getContext(), it)
        }

        if (denyPermissions == null || denyPermissions.isEmpty()) {
            notifyPermissionSucceed()
        } else {
            notifySettingRender()
        }
    }

    /**
     * [SettingResponder]回调
     */
    override fun onSettingResult(sender: RequestContext) {
        sender.finish()
        if (autoCheckWhenSettingResult) {
            //过滤被拒绝的权限
            mDenidPermissons.clear()
            request.getPermissions().filterTo(mDenidPermissons) {
                !isPermissionGrantedForPermissionResult(request.getContext(), it)
            }

            if (mDenidPermissons.isEmpty()) {
                notifyPermissionSucceed()
            } else {
                notifyPermissionFailed()
            }
        } 
    }

    /**
     * [SettingResponder]回调 获取用户自定义权限设置界面
     */
    override fun getCustomSettingIntent(sender: RequestContext): Intent? {
        return request.getSettingRender()?.getCustomSettingIntent(sender.getContext())
    }

    /**
     * RationaleView回调
     */
    private val mRationaleProcess: RationaleRender.Process by lazy {
        object : RationaleRender.Process {
            /**
             * RationaleView回调 下一步
             */
            override fun onNext() {
                requestPermissionsReal()
            }

            /**
             * RationaleView回调 取消
             */
            override fun onCancel() {
                notifySettingRender()
            }
        }
    }

    private val mSettingCaller: SettingCaller by lazy {
        SettingPermissionCaller()
    }
    /**
     * RationaleView回调
     */
    private val mSettingProcess: SettingRender.Process by lazy {


        object : SettingRender.Process {


            /**
             * RationaleView回调 下一步
             */
            override fun onNext(autoCheckWhenSettingResult: Boolean) {
                this@BaseProcessor.autoCheckWhenSettingResult = autoCheckWhenSettingResult
                requestSettingReal()
            }

            /**
             * RationaleView回调 取消
             */
            override fun onCancel() {
                notifyPermissionFailed()
            }

        }
    }

}