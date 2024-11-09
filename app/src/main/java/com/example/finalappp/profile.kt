package com.example.finalappp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgIzbr = findViewById<ImageButton>(R.id.izbrProfile)
        val imgLike = findViewById<ImageButton>(R.id.likeProfile)
        val imgDis = findViewById<ImageButton>(R.id.disProfile)
        val imgAvtor = findViewById<ImageButton>(R.id.avtorProfile)
        val login = findViewById<TextView>(R.id.loginProfile)
        val inteee = intent.getStringExtra("login")
        login.text = inteee.toString()

        imgIzbr.setOnClickListener(){
            Toast.makeText(this, "Нажал", Toast.LENGTH_LONG).show()
        }

        imgAvtor.setOnClickListener(){



            val newStr = Intent(this, between::class.java)
            startActivity(newStr)
        }

    }
}