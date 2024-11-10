package com.example.finalappp

import android.content.Intent
import android.os.Bundle
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

        val pictureId = findViewById<RecyclerView>(R.id.pictureList)
        val pictureList = arrayListOf<Picture>()
        val painterName: String = intent.getStringExtra("painter").toString()
        val naaame = findViewById<TextView>(R.id.namePainter)

        if (painterName == "1"){
            naaame.text = "Константин Ваисльев"
            pictureList.add(Picture(1, "svyatovit", "Святовит", "Краткое описание1", "Полное описание1", 100))
            pictureList.add(Picture(2, "wotan", "Вотан", "Краткое описание2", "Полное описание2", 200))
            pictureList.add(Picture(3, "gusi", "Гуси - Лебеди", "Краткое описание3", "Полное описание3", 300))
        }
        else if (painterName == "2"){
            naaame.text = "Иван Шишкин"
            pictureList.add(Picture(1, "utro", "Утро в сосновом лесу", "Краткое описание1", "Полное описание1", 100))
            pictureList.add(Picture(2, "rozh", "Рожь", "Краткое описание2", "Полное описание2", 200))
            pictureList.add(Picture(3, "dali", "Лесные дали", "Краткое описание3", "Полное описание3", 300))
        }

        pictureId.layoutManager = LinearLayoutManager(this)
        pictureId.adapter = pictureAdapter(pictureList, this)

    }
}