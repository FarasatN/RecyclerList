package com.farasatnovruzov.recyclerlist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R

class CustomerInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_info)

        val customerInfoImg = findViewById<ConstraintLayout>(R.id.customerInfoImg)
        val clientTypeCard = findViewById<CardView>(R.id.clientTypeCard)
        val phoneNumberCard = findViewById<CardView>(R.id.phoneNumberCard)
        val emailCard = findViewById<CardView>(R.id.emailCard)
        val keywordCard = findViewById<CardView>(R.id.keywordCard)

        customerInfoImg.setOnClickListener {
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
        }

        clientTypeCard.setOnClickListener {
            val intent = Intent(this, ErrorActivity::class.java)
            startActivity(intent)
        }

        phoneNumberCard.setOnClickListener {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }

        emailCard.setOnClickListener {
            val intent = Intent(this, EditPhoneNumberActivity::class.java)
            startActivity(intent)
        }

        keywordCard.setOnClickListener {
            val intent = Intent(this, EditEmailActivity::class.java)
            startActivity(intent)
        }
    }
}