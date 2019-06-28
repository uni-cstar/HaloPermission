/**
 * Created by Lucio on 18/4/9.
 *
 */
package halo.android.permission.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import halo.android.v2.common.Util
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * 各大手机厂商权限设置界面打开方法
 * 参考资料：https://www.jianshu.com/p/b5c494dba0bc
 * 暂未一一验证
 *
 * 关于如何知道系统设置界面的名字 可以通过' adb shell dumpsys activity activities '命令
 */
object SettingIntent {

    const val MF_HUAWEI = "huawei"
    const val MF_XIAOMI = "xiaomi"
    const val MF_OPPO = "oppo"
    const val MF_VIVO = "vivo"
    const val MF_SAMSUNG = "samsung"
    const val MF_MEIZU = "meizu"
    const val MF_SMARTISAN = "smartisan"
    const val MF_SONY = "sony"
    const val MF_LETV = "letv"
    const val MF_LG = "lg"

    fun obtainSettingIntent(ctx: Context): Intent {
        //获取设备制造商,缺陷：如果用于将手机刷机，比如使用华为的手机刷机小米的系统，这种情况得到的判断可能有误
        val mf = Build.MANUFACTURER.toLowerCase()
        if (mf.contains(MF_HUAWEI)) {
            return huawei(ctx)
        } else if (mf.contains(MF_XIAOMI)) {
            return xiaomi(ctx)
        } else if (mf.contains(MF_OPPO)) {
            return oppo(ctx)
        } else if (mf.contains(MF_VIVO)) {
            return vivo(ctx)
        } else if (mf.contains(MF_SAMSUNG)) {
            return samsung(ctx)
        } else if (mf.contains(MF_MEIZU)) {
            return meizu(ctx)
        } else if (mf.contains(MF_SMARTISAN)) {
            return smartisan(ctx)
        } else if (mf.contains(MF_SONY)) {
            return sony(ctx)
        } else if (mf.contains(MF_LETV)) {
            return letv(ctx)
        } else if (mf.contains(MF_LG)) {
            return lg(ctx)
        } else {
            return default(ctx)
        }
    }


    /**
     * 获取能够被正常处理的SettingIntent
     * @param customIntent 期望／自定义的权限设置界面
     * @return 校验之后可以被处理的的Intent
     */
    internal fun getCanResolvedSettingIntent(ctx: Context, customIntent: Intent? = null): Intent? {
        var settingIntent = customIntent
        if (settingIntent == null) {
            settingIntent = SettingIntent.obtainSettingIntent(ctx)
        }

        if (settingIntent.resolveActivity(ctx.packageManager) != null)
            return settingIntent

        settingIntent = SettingIntent.default(ctx)

        if (settingIntent.resolveActivity(ctx.packageManager) != null) {
            return settingIntent
        }

        settingIntent = Intent(android.provider.Settings.ACTION_SETTINGS)
        if (settingIntent.resolveActivity(ctx.packageManager) != null) {
            return settingIntent
        }

        Log.e("SettingIntent", "no activity can handle intent for setting permission")
        return null
    }

    /**
     * 应用设置详情意图
     */
    fun createApplicationDetailSettingIntent(ctx: Context):Intent{
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", ctx.packageName, null)
        return intent
    }

    /**
     * 默认Intent
     */
    @JvmStatic
    fun default(ctx: Context): Intent {
        return createApplicationDetailSettingIntent(ctx)
    }

    private fun huawei(ctx: Context): Intent {
        if (Build.VERSION.SDK_INT >= Util.M) {
            return default(ctx)
        }
        val intent = Intent()
        intent.putExtra("packageName", ctx.packageName)
        intent.setClassName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
        return intent
    }


    private fun xiaomi(ctx: Context): Intent {
        val miuiVersion = getMIUIVersion().toUpperCase()
        return when (miuiVersion) {
            "V6", "V7" -> {
                miuiV6_7(ctx)
            }
            "V8", "V9" -> {
                miuiV8_9(ctx)
            }
            else -> {
                default(ctx)
            }
        }
    }

    /**
     * 获取MIUI系统版本
     */
    private fun getMIUIVersion(): String {
        return getSystemProperty("ro.miui.ui.version.name") ?: ""
    }

    private fun miuiV6_7(ctx: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", ctx.packageName)
        return intent
    }

    private fun miuiV8_9(ctx: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", ctx.packageName)
        return intent
    }

    private fun vivo(ctx: Context): Intent {
        val intent = Intent()
        intent.putExtra("packagename", ctx.packageName)
        if (Build.VERSION.SDK_INT >= 25) {
            intent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
        } else {
            intent.setClassName("com.iqoo.secure", "com.iqoo.secure.MainActivity")
        }
        return intent
    }

    private fun oppo(ctx: Context): Intent {
//        val intent = Intent()
//        intent.putExtra("packageName", ctx.packageName)
//        intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionManagerActivity");
//        return intent
        return default(ctx)
    }

    private fun meizu(context: Context): Intent {
        if (Build.VERSION.SDK_INT >= 25) {
            return default(context)
        }

        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return intent
    }

    private fun smartisan(context: Context): Intent {
        return default(context)
    }


    private fun samsung(context: Context): Intent {
        return default(context)
    }

    /**
     * 索尼
     */
    private fun sony(ctx: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", ctx.packageName)
        intent.setClassName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
        return intent
    }

    /**
     * 乐视
     */
    private fun letv(ctx: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", ctx.packageName)
        intent.setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps")
        return intent
    }

    private fun lg(ctx: Context): Intent {
        val intent = Intent("android.intent.action.MAIN")
        intent.putExtra("packageName", ctx.packageName)
        intent.setClassName("com.android.settings", "com.android.settings.Settings'$'AccessLockSummaryActivity")
        return intent
    }

    private fun qihoo360(ctx: Context): Intent {
        val intent = Intent("android.intent.action.MAIN")
        intent.putExtra("packageName", ctx.packageName)
        intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        return intent
    }

    /**
     * Returns a SystemProperty
     * @param propName The Property to retrieve
     * @return The Property, or NULL if not found
     * 此方法来源于[https://searchcode.com/codesearch/view/41537878/]
     */
    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop " + propName)
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            Log.e("getSystemProperty", "Unable to read sysprop " + propName, ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    Log.e("getSystemProperty", "Exception while closing InputStream", e)
                }
            }
        }
        return line
    }
}