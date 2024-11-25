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
import com.example.finalappp.classes.User

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fun isValidEmail(email: String): Boolean {
            val regex = Regex("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,3}){1,2}$")
            return regex.matches(email)
        }
        val userlogin = findViewById<EditText>(R.id.login)
        val usermail = findViewById<EditText>(R.id.mail)
        val userpassword = findViewById<EditText>(R.id.password)
        val button = findViewById<Button>(R.id.confirm)
        val authStr = findViewById<TextView>(R.id.goToAuth)
        authStr.setOnClickListener(){
            val newStr = Intent(this, authorization::class.java)
            userlogin.text.clear()
            usermail.text.clear()
            userpassword.text.clear()
            newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(newStr)
            finish()
        }
        button.setOnClickListener(){
            val login = userlogin.text.toString().trim()
            val mail = usermail.text.toString().trim()
            val password = userpassword.text.toString().trim()
            if (login == "" || mail == "" || password == ""){
                if (login == ""){
                    userlogin.error = "Поле не заполнено"
                }
                if (mail == ""){
                    usermail.error = "Поле не заполнено"
                }
                if (password == ""){
                    userpassword.error = "Поле не заполнено"
                }
            }
            else{
                if ((isValidEmail(mail)) == true){
                    val db = DataBase(this, null)
                    var canGo = true
                    if (db.checkLogin(login) && db.checkMail(mail)){
                        Toast.makeText(this, "Пользователь с таким логином и почтой уже существует", Toast.LENGTH_SHORT).show()
                        canGo = false

                    }
                    else if (db.checkLogin(login)){
                        Toast.makeText(this, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()
                        canGo = false
                    }
                    else if (db.checkMail(mail)){
                        Toast.makeText(this, "Пользователь с такой почтой уже существует", Toast.LENGTH_SHORT).show()
                        canGo = false
                    }
                    else if (canGo == true){
                        db.addUser(User(login, mail, password))
                        val newStr = Intent(this, between::class.java)
                        val sharedPrefs = getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putString("loginKey", userlogin.text.toString())
                        editor.apply()
                        userlogin.text.clear()
                        usermail.text.clear()
                        userpassword.text.clear()
                        newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(newStr)
                        finish()
                    }
                }
                else{
                    Toast.makeText(this, "Неправильный формат почты", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}