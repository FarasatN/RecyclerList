package com.farasatnovruzov.recyclerlist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.farasatnovruzov.recyclerlist.R

class AboutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)


        val googlepayCard = findViewById<CardView>(R.id.googlepayCard)
        val aboutAppCard = findViewById<CardView>(R.id.aboutAppCard)

        googlepayCard.setOnClickListener {
            val intent = Intent(this, GooglePayActivity::class.java)
            startActivity(intent)
        }

        aboutAppCard.setOnClickListener {
            val intent = Intent(this, PinActivity::class.java)
            startActivity(intent)
        }
    }
}