package com.farasatnovruzov.recyclerlist.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R


class OTPActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        val pinCodeView = findViewById<StcPinView>(R.id.pinCodeView)
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(pinCodeView, InputMethodManager.SHOW_IMPLICIT)
    }
}