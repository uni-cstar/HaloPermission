package halo.android.permission.request

/**
 * Created by Lucio on 18/4/5.
 */

interface Action {
    fun invoke(permissions: List<String>)
}