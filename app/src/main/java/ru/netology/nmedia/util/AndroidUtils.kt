package ru.netology.nmedia.util

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver.OnWindowFocusChangeListener
import androidx.core.view.WindowInsetsControllerCompat

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val controller = getWindowInsetsController(view)
        controller?.hide(androidx.core.view.WindowInsetsCompat.Type.ime())
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        if (view.hasWindowFocus()) {
            showKeyboardNow(view)
        } else {
            view.viewTreeObserver.addOnWindowFocusChangeListener(object : OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        showKeyboardNow(view)
                        view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
        }
    }

    private fun showKeyboardNow(view: View) {
        if (!view.isFocused) return
        val controller = getWindowInsetsController(view)
        controller?.show(androidx.core.view.WindowInsetsCompat.Type.ime())
    }

        private fun getWindowInsetsController(view: View): WindowInsetsControllerCompat? {
        val activity = view.context as? Activity ?: return null
        return WindowInsetsControllerCompat(activity.window, view)
    }
}