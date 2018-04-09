package halo.android.permission.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.ComponentName
import android.os.Build
import halo.android.permission.common.Util


@SuppressLint("ParcelCreator")
/**
 * Created by Lucio on 18/4/9.
 */

object SettingIntent {

    fun obtainSettingIntent(ctx: Context): Intent {
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

    @JvmStatic
    fun default(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }


    private fun huawei(context: Context): Intent {
        if (Build.VERSION.SDK_INT >= Util.M) {
            return default(context)
        }
        val intent = Intent()
        intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
        return intent
    }


    private fun xiaomi(context: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_pkgname", context.packageName)
        return intent
    }


    private fun vivo(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packagename", context.packageName)
        if (Build.VERSION.SDK_INT >= 25) {
            intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
        } else {
            intent.component = ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity")
        }
        return intent
    }

    /**
     * Oppo phone to achieve the method.
     */
    private fun oppo(context: Context): Intent {
        return default(context)
    }

    /**
     * Meizu phone to achieve the method.
     */
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

}