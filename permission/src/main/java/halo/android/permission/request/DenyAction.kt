package halo.android.permission.request

/**
 * Created by Lucio on 18/4/5.
 */

interface DenyAction {
    fun onPermissionDenied(permissions: List<String>)
}

interface GrandAction{
    fun onPermissionGrand(permissions: List<String>)
}


interface PermissionListener :DenyAction,GrandAction