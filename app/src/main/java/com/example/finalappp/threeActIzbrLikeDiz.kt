package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class threeActIzbrLikeDiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_three_act_izbr_like_diz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonToBack = findViewById<ImageButton>(R.id.buttItemBack)
        buttonToBack.setOnClickListener(){
            val newStr = Intent(this, profile::class.java)
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }
        val sharedPrefs1 = getSharedPreferences("CurrentChoicePicPaint", Context.MODE_PRIVATE)
        val choice = sharedPrefs1.getString("choiceKeyPicPaint", "")
        val db = DataBase(this, null)
        val itemList = findViewById<RecyclerView>(R.id.itemList)
        val sharedPrefs = getSharedPreferences("CurrentChoice", Context.MODE_PRIVATE)
        val choiceText = findViewById<TextView>(R.id.nameStr)
        choiceText.text = sharedPrefs.getString("choiceKey", "").toString()
        val sharedPrefs2 = getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
        val userId = sharedPrefs2.getString("loginKey", "")

        Toast.makeText(this, userId.toString() + "   " + choice + "   " + choiceText.text, Toast.LENGTH_SHORT).show()

        fun load(){

            if (choice == "pictures") {
                val pictureIds = when (choiceText.text) {
                    "Избранное" -> db.getIzbrPictureIds(userId.toString().toInt())
                    "Понравилось" -> db.getReactedPictureIds(userId.toString().toInt(), 1) // Лайки с реакцией 1
                    "Не понравилось" -> db.getReactedPictureIds(userId.toString().toInt(), -1) // Дизлайки с реакцией -1
                    else -> emptyList()
                }
                val pictures = db.getPicturesByIds(pictureIds)

                if (pictures.isEmpty()) {
                    choiceText.text = "Пусто"
                    itemList.adapter = pictureAdapter(emptyList(), this)
                } else {
                    itemList.layoutManager = LinearLayoutManager(this)
                    itemList.adapter = pictureAdapter(pictures, this)
                }
            } else if (choice == "painters") {
                val painterIds = when (choiceText.text) {
                    "Избранное" -> db.getIzbrPainterIds(userId.toString().toInt())
                    "Понравилось" -> db.getReactedPainterIds(userId.toString().toInt(), 1) // Лайки с реакцией 1
                    "Не понравилось" -> db.getReactedPainterIds(userId.toString().toInt(), -1) // Дизлайки с реакцией -1
                    else -> emptyList()
                }
                val painters = db.getPaintersByIds(painterIds)

                if (painters.isEmpty()) {
                    choiceText.text ="Пусто"
                    itemList.adapter = pictureAdapter(emptyList(), this)
                } else {
                    itemList.layoutManager = LinearLayoutManager(this)
                    itemList.adapter = painterAdapter(painters, this)
                }
            }

        }

        load()

        val buttToUpdate = findViewById<ImageButton>(R.id.buttItemUpdate)
        buttToUpdate.setOnClickListener(){
            load()
        }

    }
}

