package com.example.finalappp

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
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
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
            startActivity(newStr)
        }
        button.setOnClickListener(){
            val login = userlogin.text.toString().trim()
            val mail = usermail.text.toString().trim()
            val password = userpassword.text.toString().trim()
            if (login == "" || mail == "" || password == ""){
                Toast.makeText(this, "Вы заполнили не все поля", Toast.LENGTH_LONG).show()
            }
            else{
                if ((isValidEmail(mail)) == true){
                    val user = User(login, mail, password)
                    val db = DataBase(this, null)
                    db.addUser(user)
                    val newStr = Intent(this, between::class.java)
                    newStr.putExtra("login", login)
                    startActivity(newStr)
                }
                else{
                    Toast.makeText(this, "Почта указана неверено", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}