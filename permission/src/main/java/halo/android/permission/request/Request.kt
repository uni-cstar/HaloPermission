package halo.android.permission.request

import halo.android.permission.caller.ActivityPermissionCaller
import halo.android.permission.caller.PermissionCaller
import halo.android.permission.checker.PermissionChecker
import halo.android.permission.checker.StandardChecker
import halo.android.permission.context.PermissionContext
import halo.android.permission.processor.CommonProcessor
import halo.android.permission.processor.PermissionProcessor

/**
 * Created by Lucio on 18/4/4.
 */
class Request(val permissionContext: PermissionContext) {

    /**
     * 用于渲染Rationale permission（通常是一个对话框提示用户让用户允许app接下来请求的权限）
     */
    private var mRationaleRender: RationaleRender? = null

    /**
     * 用于渲染请求设置权限（通常是一个对话框提示用户让用户设置应用权限）
     */
    private var mSettingRender: SettingRender? = null
    private var mPermissions: Array<out String> = emptyArray()
    private var mGrandAction: Action? = null
    private var mDenyAction: Action? = null

    fun getContext() = permissionContext.ctx

    fun setPermissions(vararg permissions: String): Request {
        mPermissions = permissions
        return this
    }

    fun getPermissions(): Array<out String> {
        return mPermissions
    }

    fun setRationaleRender(view: RationaleRender?): Request {
        mRationaleRender = view
        return this
    }

    fun getRationaleRender(): RationaleRender? {
        return mRationaleRender
    }

    fun setSettingRender(view: SettingRender?): Request {
        mSettingRender = view
        return this
    }

    fun getSettingRender(): SettingRender? {
        return mSettingRender
    }

    fun setGrandAction(action: Action?): Request {
        mGrandAction = action
        return this
    }

    fun getGrandAction(): Action? {
        return mGrandAction
    }

    fun setDenyAction(action: Action?): Request {
        mDenyAction = action
        return this
    }

    fun getDenyAction(): Action? {
        return mDenyAction
    }


    fun build(): PermissionProcessor {
        return build(StandardChecker(), ActivityPermissionCaller())
    }

    fun build(checker: PermissionChecker, caller: PermissionCaller): PermissionProcessor {
        return CommonProcessor(this, checker, caller)
    }

}