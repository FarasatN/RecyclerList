package com.farasatnovruzov.recyclerlist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.farasatnovruzov.recyclerlist.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val customerInfoCard = findViewById<CardView>(R.id.customerInfoCard)
        val securityCard = findViewById<CardView>(R.id.securityCard)
        val aboutAppCard = findViewById<CardView>(R.id.aboutAppCard)

        customerInfoCard.setOnClickListener {
            val intent = Intent(this, CustomerInfoActivity::class.java)
            startActivity(intent)
        }
        securityCard.setOnClickListener {
            val intent = Intent(this, SecurityActivity::class.java)
            startActivity(intent)
        }
        aboutAppCard.setOnClickListener {
            val intent = Intent(this, AboutAppActivity::class.java)
            startActivity(intent)
        }

    }
}