package halo.android.permission.common

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager

/**
 * Created by Lucio on 18/4/9.
 */
abstract class BaseRequestActivity : Activity(), RequestContext {


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            invasionStatusBar()
            onCreateInit()
        } catch (e: Exception) {
            onCreateException(e)
        }
    }

    protected abstract fun onCreateInit()

    protected abstract  fun onCreateException(e: Throwable)

    /**
     * Set the content layout full the StatusBar, but do not hide StatusBar.
     */
    private fun invasionStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (decorView.systemUiVisibility
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * [RequestContext]回调
     */
    override fun getContext(): Context {
        return this
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //prevent the back key event
        return if (event?.keyCode == KeyEvent.KEYCODE_BACK) true else super.onKeyDown(keyCode, event)
    }
}