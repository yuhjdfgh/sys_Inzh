package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        var login = findViewById<TextView>(R.id.loginProfile)
        val buttonExit = findViewById<Button>(R.id.buttonToExit)
        buttonExit.setOnClickListener(){
            val newStr = Intent(this, authorization::class.java)
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }

        val sharedPrefs = getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
        login.text = sharedPrefs.getString("loginKey", "")

        imgAvtor.setOnClickListener(){
            val newStr = Intent(this, between::class.java)
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }

        imgIzbr.setOnClickListener {
            val sharedPrefs = getSharedPreferences("CurrentChoice", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("choiceKey", "Избранное")
            editor.apply()
            val intent = Intent(this, ChoiceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        imgLike.setOnClickListener {
            val sharedPrefs = getSharedPreferences("CurrentChoice", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("choiceKey", "Понравилось")
            editor.apply()
            val intent = Intent(this, ChoiceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        imgDis.setOnClickListener {
            val sharedPrefs = getSharedPreferences("CurrentChoice", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("choiceKey", "Не понравилось")
            editor.apply()
            val intent = Intent(this, ChoiceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


    }
}