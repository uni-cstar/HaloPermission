/**
 * Created by Lucio on 18/4/9.
 *
 */
package halo.android.permission.setting

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import halo.android.permission.common.Util
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

    fun obtainSettingIntent(ctx: Context): Intent {
        //获取设备制造商,缺陷：如果用于将手机刷机，比如使用华为的手机刷机小米的系统，这种情况得到的判断可能有误
        val mark = Build.MANUFACTURER.toLowerCase()
        if (mark.contains("huawei")) {
            return huawei(ctx)
        } else if (mark.contains("xiaomi")) {
            return xiaomi(ctx)
        } else if (mark.contains("oppo")) {
            return oppo(ctx)
        } else if (mark.contains("vivo")) {
            return vivo(ctx)
        } else if (mark.contains("samsung")) {
            return samsung(ctx)
        } else if (mark.contains("meizu")) {
            return meizu(ctx)
        } else if (mark.contains("smartisan")) {
            return smartisan(ctx)
        }
        return default(ctx)
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
     * 默认Intent
     */
    @JvmStatic
    fun default(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
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

    private fun vivo(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packagename", context.packageName)
        if (Build.VERSION.SDK_INT >= 25) {
            intent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
        } else {
            intent.setClassName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity")
        }
        return intent
    }

    private fun oppo(context: Context): Intent {
        return default(context)
    }

    private fun meizu(context: Context): Intent {
        if (Build.VERSION.SDK_INT >= 25) {
            return default(context)
        }

        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.component = ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return intent
    }

    private fun smartisan(context: Context): Intent {
        return default(context)
    }


    private fun samsung(context: Context): Intent {
        return default(context)
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