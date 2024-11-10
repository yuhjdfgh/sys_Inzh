package com.example.finalappp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
                Toast.makeText(this, "Вы заполнили не все поля", Toast.LENGTH_LONG).show()
            }
            else{
                val db = DataBase(this, null)
                val isAuth = db.getUser(login, password)
                if (isAuth) {
                    val intent = Intent(this, between::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                    userlogin.text.clear()
                    userpassword.text.clear()
                }
                else{
                    Toast.makeText(this, "Пользователь $login не существует", Toast.LENGTH_LONG).show()
                }
            }
        }
        regStr.setOnClickListener(){
            val newStr = Intent(this, MainActivity::class.java)
            startActivity(newStr)
        }
    }
}