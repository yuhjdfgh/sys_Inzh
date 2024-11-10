package com.example.finalappp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DataBase(val context: Context, val factory: CursorFactory?):
SQLiteOpenHelper(context, "nameDataBase", factory, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE users (id INT PRIMARY KEY, login TEXT, mail TEXT, password TEXT)")
        db!!.execSQL("CREATE TABLE painters (id INT PRIMARY KEY, image TEXT, name TEXT, yearsOfLife TEXT, stile TEXT, liki INT, comm INT, dis INT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db!!.execSQL("DROP TABLE IF EXISTS painters")
        onCreate(db)
    }

    fun addUser(user: User){
        val values = ContentValues()
        values.put("login", user.login)
        values.put("mail", user.mail)
        values.put("password", user.password)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun addPainter(painter: PainterClass){
        val values = ContentValues()
        values.put("image", painter.image)
        values.put("name", painter.name)
        values.put("yearsOfLife", painter.yearsOfLife)
        values.put("stile", painter.stile)
        values.put("liki", painter.like)
        values.put("comm", painter.comm)
        values.put("dis", painter.dis)

        val db = this.writableDatabase
        db.insert("painters", null, values)
        db.close()
    }

    fun getUser(login: String, password: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND password = '$password'", null)
        return result.moveToFirst()
    }

}