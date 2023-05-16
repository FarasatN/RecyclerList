package com.farasatnovruzov.recyclerlist.ui

import android.content.Context
import android.os.Vibrator
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.farasatnovruzov.recyclerlist.R


class MlbPinNumberBoard : LinearLayout, View.OnClickListener {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        init(context)
    }

    val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private val keyValues: SparseArray<String> = SparseArray()

    private var inputConnection: InputConnection? = null

    override fun onClick(v: View?) {
        vibe.vibrate(50)
        v?.let {

            val selectedText: CharSequence? =
                inputConnection?.getExtractedText(ExtractedTextRequest(), 0)?.text

            if (v.id == R.id.buttonDelete) {
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection?.deleteSurroundingText(1, 0)
                } else {
                    inputConnection?.setComposingText(
                        selectedText?.substring(
                            0,
                            selectedText.let { it.length - 1 }
                        ), 1
                    )
                }
            } else {
                val value: String = keyValues.get(v.id)
                inputConnection?.setComposingText(selectedText.toString() + value, 1)
            }
        }
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.test, this, true)

        val button1 = findViewById<CardView>(R.id.button1)
        val button2 = findViewById<CardView>(R.id.button2)
        val button3 = findViewById<CardView>(R.id.button3)
        val button4 = findViewById<CardView>(R.id.button4)
        val button5 = findViewById<CardView>(R.id.button5)
        val button6 = findViewById<CardView>(R.id.button6)
        val button7 = findViewById<CardView>(R.id.button7)
        val button8 = findViewById<CardView>(R.id.button8)
        val button9 = findViewById<CardView>(R.id.button9)
        val button0 = findViewById<CardView>(R.id.button0)
        val buttonCancel = findViewById<CardView>(R.id.buttonCancel)
        val buttonDelete = findViewById<CardView>(R.id.buttonDelete)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button0.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
        buttonDelete.setOnClickListener(this)

        keyValues.put(R.id.button1, "1")
        keyValues.put(R.id.button2, "2")
        keyValues.put(R.id.button3, "3")
        keyValues.put(R.id.button4, "4")
        keyValues.put(R.id.button5, "5")
        keyValues.put(R.id.button6, "6")
        keyValues.put(R.id.button7, "7")
        keyValues.put(R.id.button8, "8")
        keyValues.put(R.id.button9, "9")
        keyValues.put(R.id.button0, "0")
    }

    open fun setInputConnection(iC: InputConnection?) {
        inputConnection = iC
    }
}