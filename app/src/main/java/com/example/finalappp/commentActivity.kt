package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class commentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val butt = findViewById<ImageButton>(R.id.buttToBackComm)
        val list = findViewById<ListView>(R.id.listComments)
        val editComm = findViewById<EditText>(R.id.editComment)
        val sendComm = findViewById<ImageButton>(R.id.sendMessage)

        val whichComm = intent.getStringExtra("whichComm")
        val whichPainter = intent.getStringExtra("painter")?.toIntOrNull() ?: 0
        val whichPicture = intent.getIntExtra("picture", 0)

        val sharedPrefs = getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
        val nameUser = sharedPrefs.getString("loginKey", "") ?: ""
        val db = DataBase(this, null)
        val userID = db.getUserByName(nameUser)

        val masList: MutableList<String> = mutableListOf()
        val adapterList = ArrayAdapter(this, android.R.layout.simple_list_item_1, masList)
        list.adapter = adapterList

        if (whichComm == "1"){
            fun loadComments() {
                masList.clear()
                val comments = db.getAllCommPainter(whichPainter)
                Log.d("loadComments", "Comments retrieved: ${comments.size}")
                if (comments.isEmpty()) {
                    masList.add("Комментариев пока нет.")
                } else {
                    comments.forEach { comment ->
                        val userName = db.getUserById(comment.idUser)
                        Log.d("loadComments", "User: $userName, Comment: ${comment.comment}")
                        masList.add("$userName: ${comment.comment}")
                    }
                }
                adapterList.notifyDataSetChanged()
            }

            loadComments()

            sendComm.setOnClickListener {
                val newComment = editComm.text.toString().trim()
                if (newComment.isEmpty()) {
                    Toast.makeText(this, "Ваш комментарий пуст!", Toast.LENGTH_SHORT).show()
                } else {
                    db.addComment(CommentClass(whichPainter, userID, newComment))
                    editComm.setText("")
                    loadComments()
                    db.incComm(whichPainter)
                }
            }

            butt.setOnClickListener{
                val strToBack = intent.getStringExtra("strToBack")
                val newStr = Intent(this, Class.forName(strToBack))
                newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(newStr)
                finish()
            }

        }
        else if (whichComm == "2"){

            fun loadComments() {
                masList.clear()
                val comments = db.getAllCommPicture(whichPicture)
                if (comments.isEmpty()) {
                    masList.add("Комментариев пока нет.")
                } else {
                    comments.forEach { comment ->
                        val userName = db.getUserById(comment.idUser)
                        masList.add("$userName: ${comment.comment}")
                    }
                }
                adapterList.notifyDataSetChanged()
            }

            loadComments()

            sendComm.setOnClickListener {
                val newComment = editComm.text.toString().trim()
                if (newComment.isEmpty()) {
                    Toast.makeText(this, "Ваш комментарий пуст!", Toast.LENGTH_SHORT).show()
                } else {
                    db.addCommPicture(commentPicture(whichPicture, userID, newComment))
                    editComm.setText("")
                    loadComments()
                    db.changeReactionPicture(whichPicture, "comments", "+")
                }
            }

            butt.setOnClickListener{
                val strToBack = intent.getStringExtra("strToBack")
                val newStr = Intent(this, Class.forName(strToBack))
                newStr.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(newStr)
                finish()
            }

        }
    }
}