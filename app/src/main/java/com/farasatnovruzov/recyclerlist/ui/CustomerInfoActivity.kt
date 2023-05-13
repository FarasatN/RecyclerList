package com.farasatnovruzov.recyclerlist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.farasatnovruzov.recyclerlist.R

class CustomerInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_info)

        val customerInfoImg = findViewById<ConstraintLayout>(R.id.customerInfoImg)
        val clientTypeCard = findViewById<CardView>(R.id.clientTypeCard)

        customerInfoImg.setOnClickListener {
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
        }

        clientTypeCard.setOnClickListener {
            val intent = Intent(this, ErrorActivity::class.java)
            startActivity(intent)
        }
    }
}