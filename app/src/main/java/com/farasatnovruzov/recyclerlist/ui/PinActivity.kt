package com.farasatnovruzov.recyclerlist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R

class PinActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin3)

        val pin = findViewById<StcPinView>(R.id.createNewPinView)
        val pin2 = findViewById<StcPinView>(R.id.createNewPinView)

        pin.showSoftInputOnFocus = true
    }
}