package halo.android.permission.request

import android.content.Context

/**
 * Created by Lucio on 18/4/4.
 */

interface RationaleRender {

    fun show(ctx: Context, permission: List<String>, process: Process)

    interface Process {
        fun onNext()

        fun onCancel()
    }
}
