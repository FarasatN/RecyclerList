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


        val aboutAppCard = findViewById<CardView>(R.id.aboutAppCard)

        aboutAppCard.setOnClickListener {
            val intent = Intent(this, PinActivity::class.java)
            startActivity(intent)
        }
    }
}