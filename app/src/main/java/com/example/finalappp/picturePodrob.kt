package com.example.finalappp

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class picturePodrob : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_picture_podrob)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = DataBase(this, null)
        val title = findViewById<TextView>(R.id.pictureTitlePodrob)
        val text = findViewById<TextView>(R.id.pictureTextPodrob)
        val butt = findViewById<ImageButton>(R.id.buttToBackPictures)
        val image = findViewById<ImageView>(R.id.pictureImgPodrob)
        val currPicture = intent.getIntExtra("pictureId", 0)
        title.text = db.getField(currPicture, "title")
        text.text = db.getField(currPicture, "text")

        val imageResId = resources.getIdentifier(db.getField(currPicture, "image"), "drawable", packageName)
        image.setImageResource(imageResId)


        butt.setOnClickListener{
            val newStr = Intent(this, mainStr::class.java)
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }

    }
}