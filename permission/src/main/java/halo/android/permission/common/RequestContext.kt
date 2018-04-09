package halo.android.permission.common

import android.content.Context

/**
 * Created by Lucio on 18/4/8.
 */

interface RequestContext {
    fun finish()

    fun getContext(): Context
}