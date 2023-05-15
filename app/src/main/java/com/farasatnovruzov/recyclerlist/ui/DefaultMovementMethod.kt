package com.farasatnovruzov.recyclerlist.ui

import android.text.Selection
import android.text.Spannable
import android.text.method.MovementMethod
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView

class DefaultMovementMethod : MovementMethod {

    private var sInstance: DefaultMovementMethod? = null

    companion object {
        private val mInstance: DefaultMovementMethod =
            DefaultMovementMethod()

        @Synchronized
        fun getInstance(): DefaultMovementMethod {
            return mInstance
        }
    }

    override fun onTouchEvent(p0: TextView?, p1: Spannable?, p2: MotionEvent?): Boolean {
        return false
    }

    override fun canSelectArbitrarily(): Boolean {
        return false
    }

    override fun onKeyDown(p0: TextView?, p1: Spannable?, p2: Int, p3: KeyEvent?): Boolean {
        return false
    }

    override fun onKeyUp(p0: TextView?, p1: Spannable?, p2: Int, p3: KeyEvent?): Boolean {
        return false
    }

    override fun onGenericMotionEvent(p0: TextView?, p1: Spannable?, p2: MotionEvent?): Boolean {
        return false
    }

    override fun onTakeFocus(p0: TextView?, p1: Spannable?, p2: Int) {
    }

    override fun initialize(p0: TextView?, text: Spannable?) {
        Selection.setSelection(text, 0)
    }

    override fun onKeyOther(p0: TextView?, p1: Spannable?, p2: KeyEvent?): Boolean {
        return false
    }

    override fun onTrackballEvent(p0: TextView?, p1: Spannable?, p2: MotionEvent?): Boolean {
        return false
    }
}
