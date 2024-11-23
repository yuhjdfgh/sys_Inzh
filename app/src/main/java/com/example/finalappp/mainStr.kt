package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class mainStr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_str)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonToBack = findViewById<ImageButton>(R.id.buttPicturesBack)
        buttonToBack.setOnClickListener(){
            val newStr = Intent(this, between::class.java)
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }

        val buttonProfile = findViewById<ImageButton>(R.id.buttPicturesProfile)
        buttonProfile.setOnClickListener(){
            val newStr = Intent(this, profile::class.java)
            newStr.putExtra("strToBack", this::class.java.name)
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }

        val db = DataBase(this, null)
        val pictureId = findViewById<RecyclerView>(R.id.pictureList)
        val sharedPrefs = getSharedPreferences("CurrentPainter", Context.MODE_PRIVATE)
        val painterId  = sharedPrefs.getString("painterKey", "").toString().toInt()
        val naaame = findViewById<TextView>(R.id.namePainter)
        naaame.text = db.getPainterById(painterId)
        val pictureList = db.getPicturesByPainterId(painterId.toString())
        pictureId.layoutManager = LinearLayoutManager(this)
        pictureId.adapter = pictureAdapter(pictureList, this)
    }
}