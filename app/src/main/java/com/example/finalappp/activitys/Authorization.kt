package com.example.finalappp.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalappp.DataBase
import com.example.finalappp.R

class authorization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_authorization)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userlogin = findViewById<EditText>(R.id.loginAuth)
        val userpassword = findViewById<EditText>(R.id.passwordAuth)
        val button = findViewById<Button>(R.id.confirmAuth)
        val regStr = findViewById<TextView>(R.id.goToReg)
        button.setOnClickListener(){
            var login = userlogin.text.toString().trim()
            val password = userpassword.text.toString().trim()
            if (login == "" || password == ""){
                if (login == ""){
                    userlogin.error = "Поле не заполнено"
                }
                if (password == ""){
                    userpassword.error = "Поле не заполнено"
                }
            }
            else{
                val db = DataBase(this, null)
                if (db.checkUser(login, password)) {
                    val intent = Intent(this, between::class.java)
                    val sharedPrefs = getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor.putString("loginKey", userlogin.text.toString())
                    editor.apply()
                    userlogin.text.clear()
                    userpassword.text.clear()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }
        regStr.setOnClickListener(){
            val newStr = Intent(this, MainActivity::class.java)
            userlogin.text.clear()
            userpassword.text.clear()
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }
    }
}