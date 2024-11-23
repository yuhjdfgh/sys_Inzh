package com.example.finalappp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(val context: Context, val factory: CursorFactory?):
SQLiteOpenHelper(context, "nameDataBase", factory, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, mail TEXT, password TEXT)")
        db.execSQL("CREATE TABLE painters (id INTEGER PRIMARY KEY, image TEXT, name TEXT, yearsOfLife TEXT, stile TEXT, likes INTEGER, comm INTEGER, dis INTEGER)")
        db.execSQL("CREATE TABLE pictures (id INTEGER PRIMARY KEY, idPainter INTEGER, image TEXT, title TEXT, description TEXT, text TEXT, price INTEGER, likes INTEGER, dislikes INTEGER, comments INTEGER)")
        db.execSQL("CREATE TABLE comments (id INTEGER PRIMARY KEY AUTOINCREMENT, idPainter INTEGER, idUser INTEGER, comment TEXT)")
        db.execSQL("CREATE TABLE commentsPicture (id INTEGER PRIMARY KEY AUTOINCREMENT, idPicture INTEGER, idUser INTEGER, comment TEXT)")
        db!!.execSQL("CREATE TABLE reactionPainter (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, painterId INTEGER, reaction INTEGER)") // 1 0 -1
        db!!.execSQL("CREATE TABLE reactionPicture (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, pictureId INTEGER, reaction INTEGER)")
        db!!.execSQL("CREATE TABLE izbrPainter (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, painterId INTEGER, reaction INTEGER)") // 1 0
        db!!.execSQL("CREATE TABLE izbrPicture (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, pictureId INTEGER, reaction INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS painters")
        db.execSQL("DROP TABLE IF EXISTS pictures")
        db.execSQL("DROP TABLE IF EXISTS comments")
        db.execSQL("DROP TABLE IF EXISTS commentsPicture")
        db.execSQL("DROP TABLE IF EXISTS reactionPainter")
        db.execSQL("DROP TABLE IF EXISTS reactionPicture")
        db.execSQL("DROP TABLE IF EXISTS izbrPainter")
        db.execSQL("DROP TABLE IF EXISTS izbrPicture")
        onCreate(db)
    }

    /////////ПОЛЬЗОВАТЕЛИ//////
    @SuppressLint("Range")
    fun getUserByName(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE login = ?", arrayOf(name))
        var userId = 0
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"))
        }
        cursor.close()
        return userId
    }
    @SuppressLint("Range")
    fun getUserById(userId: Int): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT login FROM users WHERE id = ?", arrayOf(userId.toString()))
        var login = "Unknown User"
        if (cursor.moveToFirst()) {
            login = cursor.getString(cursor.getColumnIndex("login"))
        }
        cursor.close()
        return login
    }
    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("login", user.login)
        values.put("mail", user.mail)
        values.put("password", user.password)
        db.insert("users", null, values)
        db.close()
    }
    fun checkUser(login: String, password: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND password = '$password'", null)
        return result.moveToFirst()
    }
    fun checkLogin(login: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login'", null)
        return result.moveToFirst()
    }
    fun checkMail(mail: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE mail = '$mail'", null)
        return result.moveToFirst()
    }
    /////////ПОЛЬЗОВАТЕЛИ//////

    /////////ХУДОЖНИКИ//////
    @SuppressLint("Range")
    fun getAllCommPainter(painterId: Int): List<CommentClass> {
        val db = this.readableDatabase
        val commentsList = mutableListOf<CommentClass>()
        val cursor = db.rawQuery("SELECT * FROM comments WHERE idPainter = ?", arrayOf(painterId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idPainter = cursor.getInt(cursor.getColumnIndex("idPainter"))
                val idUser = cursor.getInt(cursor.getColumnIndex("idUser"))
                val comment = cursor.getString(cursor.getColumnIndex("comment"))
                commentsList.add(CommentClass(idPainter, idUser, comment))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return commentsList
    }
    fun addComment(comm: CommentClass) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("idPainter", comm.idPainter)
        values.put("idUser", comm.idUser)
        values.put("comment", comm.comment)
        db.insert("comments", null, values)
        db.close()
    }
    fun addPainter(painter: PainterClass) {
        val values = ContentValues()
        values.put("id", painter.id)
        values.put("image", painter.image)
        values.put("name", painter.name)
        values.put("yearsOfLife", painter.yearsOfLife)
        values.put("stile", painter.stile)
        values.put("likes", painter.likes)
        values.put("comm", painter.comm)
        values.put("dis", painter.dis)
        val db = this.writableDatabase
        db.insert("painters", null, values)
        db.close()
    }
    fun checkPainter(namePainter: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM painters WHERE name = '$namePainter'", null)
        return result.moveToFirst()
    }
    @SuppressLint("Range")
    fun getPainter(idd: Int): PainterClass{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, image, name, yearsOfLife, stile, likes, comm, dis FROM painters WHERE id = '$idd'", null)
        var image = ""
        var name = ""
        var yearsOfLife = ""
        var stile = ""
        var likes = 0
        var comm = 0
        var dis = 0
        if (cursor.moveToFirst()){
            image = cursor.getString(cursor.getColumnIndex("image"))
            name = cursor.getString(cursor.getColumnIndex("name"))
            yearsOfLife = cursor.getString(cursor.getColumnIndex("yearsOfLife"))
            stile = cursor.getString(cursor.getColumnIndex("stile"))
            likes = cursor.getInt(cursor.getColumnIndex("likes"))
            comm = cursor.getInt(cursor.getColumnIndex("comm"))
            dis = cursor.getInt(cursor.getColumnIndex("dis"))
        }
        cursor.close()
        val result = PainterClass(idd, image, name, yearsOfLife, stile, likes, comm, dis)
        return result
    }
    @SuppressLint("Range")
    fun getPainterByName(name: String):String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM painters WHERE name = '$name'", null)
        var idd = 0
        if (cursor.moveToFirst()){
            idd = cursor.getInt(cursor.getColumnIndex("id"))
        }
        cursor.close()
        return idd.toString()
    }
    @SuppressLint("Range")
    fun getPainterById(idd: Int):String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM painters WHERE id = '$idd'", null)
        var name = ""
        if (cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex("name"))
        }
        cursor.close()
        return name
    }
    @SuppressLint("Range")
    fun getLikeCount(idd: Int): String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT likes FROM painters WHERE id = '$idd'", null)
        var likes = 0
        if (cursor.moveToFirst()){
            likes = cursor.getInt(cursor.getColumnIndex("likes"))
        }
        cursor.close()
        return likes.toString()
    }
    @SuppressLint("Range")
    fun getCommCount(idd: Int): String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT comm FROM painters WHERE id = '$idd'", null)
        var comm = 0
        if (cursor.moveToFirst()){
            comm = cursor.getInt(cursor.getColumnIndex("comm"))
        }
        cursor.close()
        return comm.toString()
    }
    @SuppressLint("Range")
    fun getDisCount(idd: Int): String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT dis FROM painters WHERE id = '$idd'", null)
        var dis = 0
        if (cursor.moveToFirst()){
            dis = cursor.getInt(cursor.getColumnIndex("dis"))
        }
        cursor.close()
        return dis.toString()
    }
    fun chooseLikePainter(userId: Int, painterId: Int, reaction: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("userId", userId)
        values.put("painterId", painterId)
        values.put("reaction", reaction)
        db.insert("reactionPainter", null, values)
        db.close()
    }
    fun updateLikePainter(userId: Int, painterId: Int, reaction: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE reactionPainter SET reaction = '$reaction' WHERE userId = '$userId' and painterId = '$painterId'")
        db.close()
    }
    fun findUserReactOnPainter(userId: Int, painterId: Int):Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM reactionPainter WHERE userId = '$userId' AND painterId = '$painterId'", null)
        return cursor.moveToFirst()
    }
    @SuppressLint("Range")
    fun getUserReactOnPainter(userId: Int, painterId: Int): Int{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT reaction FROM reactionPainter WHERE userId = '$userId' AND painterId = '$painterId'", null)
        var reaction = 0
        if (cursor.moveToFirst()){
            reaction = cursor.getInt(cursor.getColumnIndex("reaction"))
        }
        cursor.close()
        return reaction
    }
    fun incLike (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE painters SET likes = likes + 1 WHERE id = '$idd'")
        db.close()
    }
    fun incComm (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE painters SET comm = comm + 1 WHERE id = '$idd'")
        db.close()
    }
    fun incDis (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE painters SET dis = dis + 1 WHERE id = '$idd'")
        db.close()
    }
    fun decLike (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE painters SET likes = likes - 1 WHERE id = '$idd'")
        db.close()
    }
    fun decDis (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE painters SET dis = dis - 1 WHERE id = '$idd'")
        db.close()
    }
    /////////ХУДОЖНИКИ//////

    /////////КАРТИНЫ//////
    @SuppressLint("Range")
    fun getPicturesByPainterId(painterId: String): ArrayList<Picture> {
        val db = this.readableDatabase
        val picturesList = ArrayList<Picture>()
        val cursor = db.rawQuery("SELECT * FROM pictures WHERE idPainter = ?", arrayOf(painterId))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val idPainter = cursor.getInt(cursor.getColumnIndex("idPainter"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val text = cursor.getString(cursor.getColumnIndex("text"))
                val price = cursor.getInt(cursor.getColumnIndex("price"))
                val likes = cursor.getInt(cursor.getColumnIndex("likes"))
                val dislikes = cursor.getInt(cursor.getColumnIndex("dislikes"))
                val comments = cursor.getInt(cursor.getColumnIndex("comments"))
                picturesList.add(Picture(id, idPainter, image, title, description, text, price, likes , dislikes, comments))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return picturesList
    }
    @SuppressLint("Range")
    fun addPicture(picture: Picture){
        val values = ContentValues()
        values.put("id", picture.id)
        values.put("idPainter", picture.idPainter)
        values.put("image", picture.image)
        values.put("title", picture.title)
        values.put("description", picture.desc)
        values.put("text", picture.text)
        values.put("price", picture.price)
        values.put("likes", picture.likes)
        values.put("dislikes", picture.dislikes)
        values.put("comments", picture.comments)
        val db = this.writableDatabase
        db.insert("pictures", null, values)
        db.close()
    }
    @SuppressLint("Range")
    fun getField(idPicture: Int, field: String): String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $field FROM pictures WHERE id = ?", arrayOf(idPicture.toString()))
        var result = ""
        if (cursor.moveToFirst()){
            result = cursor.getString(cursor.getColumnIndex(field))
        }
        cursor.close()
        return result
    }
    fun chooseReactionPicture(userId: Int, pictureId: Int, reaction: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("userId", userId)
        values.put("pictureId", pictureId)
        values.put("reaction", reaction)
        db.insert("reactionPicture", null, values)
        db.close()
    }
    fun updateReactionPicture(userId: Int, pictureId: Int, reaction: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE reactionPicture SET reaction = '$reaction' WHERE userId = '$userId' and pictureId = '$pictureId'")
        db.close()
    }
    fun changeReactionPicture(pictureId: Int, field: String, sign: String){
        val db = this.writableDatabase
        db.execSQL("UPDATE pictures SET $field = $field $sign 1 WHERE id = '$pictureId'")
        db.close()
    }
    fun findUserReactOnPicture(userId: Int, pictureId: Int):Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM reactionPicture WHERE userId = '$userId' AND pictureId = '$pictureId'", null)
        return cursor.moveToFirst()
    }
    @SuppressLint("Range")
    fun getUserReactOnPicture(userId: Int, pictureId: Int): Int{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT reaction FROM reactionPicture WHERE userId = '$userId' AND pictureId = '$pictureId'", null)
        var reaction = 0
        if (cursor.moveToFirst()){
            reaction = cursor.getInt(cursor.getColumnIndex("reaction"))
        }
        cursor.close()
        return reaction
    }
    @SuppressLint("Range")
    fun getFieldValue(pictureId: Int, field: String): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $field FROM pictures WHERE id = '$pictureId'", null)
        var value = 0
        if (cursor.moveToFirst()){
            value = cursor.getInt(cursor.getColumnIndex("$field"))
        }
        cursor.close()
        return value.toString()
    }
    @SuppressLint("Range")
    fun getAllCommPicture(pictureId: Int): List<commentPicture> {
        val db = this.readableDatabase
        val commentsList = mutableListOf<commentPicture>()
        val cursor = db.rawQuery("SELECT * FROM commentsPicture WHERE idPicture = ?", arrayOf(pictureId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idPicture = cursor.getInt(cursor.getColumnIndex("idPicture"))
                val idUser = cursor.getInt(cursor.getColumnIndex("idUser"))
                val comment = cursor.getString(cursor.getColumnIndex("comment"))
                commentsList.add(commentPicture(idPicture, idUser, comment))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return commentsList
    }
    fun addCommPicture(comm: commentPicture) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("idPicture", comm.idPicture)
        values.put("idUser", comm.idUser)
        values.put("comment", comm.comment)
        db.insert("commentsPicture", null, values)
        db.close()
    }
    /////////КАРТИНЫ//////

    ////////ThreeActions//////
    @SuppressLint("Range")
    fun getReactedPainterIds(userId: Int, reaction: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT painterId FROM reactionPainter WHERE userId = ? AND reaction = ?", arrayOf(userId.toString(), reaction.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("painterId")))
            } while (cursor.moveToNext())
        }
        cursor.close()

        if (ids.isEmpty()) {
            Log.d("getReactedPainterIds", "No IDs found for user $userId and reaction $reaction")
        }

        return ids
    }
    @SuppressLint("Range")
    fun getReactedPictureIds(userId: Int, reaction: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT pictureId FROM reactionPicture WHERE userId = ? AND reaction = ?",
            arrayOf(userId.toString(), reaction.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("pictureId")))
            } while (cursor.moveToNext())
        }
        cursor.close()

        if (ids.isEmpty()) {
            Log.d("getReactedPainterIds", "No IDs found for user $userId and reaction $reaction")
        }

        return ids
    }
    @SuppressLint("Range")
    fun getPaintersByIds(ids: List<Int>): List<PainterClass> {
        val db = this.readableDatabase
        val painters = mutableListOf<PainterClass>()
        if (ids.isEmpty()) {
            Log.d("getPaintersByIds", "No IDs provided")
            return painters
        }

        val idsString = ids.joinToString(",")
        val cursor = db.rawQuery("SELECT * FROM painters WHERE id IN ($idsString)", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val yearsOfLife = cursor.getString(cursor.getColumnIndex("yearsOfLife"))
                val stile = cursor.getString(cursor.getColumnIndex("stile"))
                val likes = cursor.getInt(cursor.getColumnIndex("likes"))
                val comm = cursor.getInt(cursor.getColumnIndex("comm"))
                val dis = cursor.getInt(cursor.getColumnIndex("dis"))
                painters.add(PainterClass(id, image, name, yearsOfLife, stile, likes, comm, dis))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return painters
    }
    @SuppressLint("Range")
    fun getPicturesByIds(ids: List<Int>): List<Picture> {
        val db = this.readableDatabase
        val pictures = mutableListOf<Picture>()
        if (ids.isEmpty()) return pictures

        val idsString = ids.joinToString(",")
        val cursor = db.rawQuery("SELECT * FROM pictures WHERE id IN ($idsString)", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val idPainter = cursor.getInt(cursor.getColumnIndex("idPainter"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val text = cursor.getString(cursor.getColumnIndex("text"))
                val price = cursor.getInt(cursor.getColumnIndex("price"))
                val likes = cursor.getInt(cursor.getColumnIndex("likes"))
                val dislikes = cursor.getInt(cursor.getColumnIndex("dislikes"))
                val comments = cursor.getInt(cursor.getColumnIndex("comments"))
                pictures.add(Picture(id, idPainter, image, title, description, text, price, likes, dislikes, comments))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pictures
    }
    ////////ThreeActions//////

    @SuppressLint("Range")
    fun findIsIzbr(idUser: Int, idd: Int, field: String, field2: String): Int{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT reaction FROM $field WHERE $field2 = '$idd' and userId = '$idUser'", null)
        var reaction = 0
        if (cursor.moveToFirst()){
            reaction = cursor.getInt(cursor.getColumnIndex("reaction"))
        }
        cursor.close()
        return reaction
    }
    fun changeStatusIzbr(idUser: Int, idd: Int, field: String, field2: String, reaction: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE $field SET reaction = $reaction WHERE $field2 = '$idd' and userId = '$idUser'")
        db.close()

    }
    fun chooseStatusIzbr(userId: Int, itemId: Int, reaction: Int, field: String, field2: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("userId", userId)
        values.put("$field2", itemId)
        values.put("reaction", reaction)
        db.insert("$field", null, values)
        db.close()
    }
    fun checkStatusIzbr(userId: Int, itemId: Int, field: String, field2: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $field WHERE userId = '$userId' AND $field2 = '$itemId'", null)
        return cursor.moveToFirst()
    }

    @SuppressLint("Range")
    fun getIzbrPainterIds(userId: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT painterId FROM izbrPainter WHERE userId = ? AND reaction = 1", arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("painterId")))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ids
    }
    @SuppressLint("Range")
    fun getIzbrPictureIds(userId: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT pictureId FROM izbrPicture WHERE userId = ? AND reaction = 1", arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("pictureId")))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ids
    }

}