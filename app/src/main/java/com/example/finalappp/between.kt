package com.example.finalappp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class between : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_between)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        val list = findViewById<ListView>(R.id.list1)
//        val masList: MutableList<String> = mutableListOf()
//        val adapterList = ArrayAdapter(this, android.R.layout.simple_list_item_1, masList)
//        list.adapter = adapterList
//        adapterList.insert("Константин Васильев", 0)
//        adapterList.insert("Вася Пупкин", 1)
//        list.setOnItemClickListener { parent, view, position, id ->
//            val ItemWhichClicked = list.getItemAtPosition(position).toString()
//            if (ItemWhichClicked == "Константин Васильев"){
//                Toast.makeText(this, "Вы выбрали Item: $ItemWhichClicked", Toast.LENGTH_SHORT).show()
//                val who: String = "1"
//                val newStr = Intent(this, mainStr::class.java)
//                newStr.putExtra("painter", who)
//                startActivity(newStr)
//            }
//            else if (ItemWhichClicked == "Вася Пупкин"){
//                Toast.makeText(this, "Вы выбрали Item: $ItemWhichClicked", Toast.LENGTH_SHORT).show()
//                val who: String = "2"
//                val newStr = Intent(this, mainStr::class.java)
//                newStr.putExtra("painter", who)
//                startActivity(newStr)
//            }
//
//        }
        val login = intent.getStringExtra("login")
        val buttonProfile = findViewById<ImageButton>(R.id.buttBetweenProfile)
        buttonProfile.setOnClickListener(){
            val newStr = Intent(this, profile::class.java)
            newStr.putExtra("login", login.toString().trim())
            startActivity(newStr)
        }
        val painterId = findViewById<RecyclerView>(R.id.painterList)
        val painterList = arrayListOf<PainterClass>()
        painterList.add(PainterClass(1, "vasilyev", "Константин Васильев", "1942 - 1976", "Костя", 0, 0, 0))
        painterList.add(PainterClass(2, "shihkin", "Иван Шишкин", "годы жизни шишка", "шишка", 0, 0, 0))
        painterId.layoutManager = LinearLayoutManager(this)
        painterId.adapter = painterAdapter(painterList, this)

    }
}