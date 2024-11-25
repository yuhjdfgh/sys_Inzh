package com.example.finalappp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.finalappp.classes.CommentClass
import com.example.finalappp.classes.PainterClass
import com.example.finalappp.classes.Picture
import com.example.finalappp.classes.User
import com.example.finalappp.classes.commentPicture

class DataBase(val context: Context, val factory: CursorFactory?):
    SQLiteOpenHelper(context, "nameDataBase", factory, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, mail TEXT, password TEXT)")

        db.execSQL("CREATE TABLE Painters (id INTEGER PRIMARY KEY, image TEXT, name TEXT, yearsOfLife TEXT, stile TEXT, likes INTEGER, comm INTEGER, dis INTEGER)")
        db.execSQL("CREATE TABLE Pictures (id INTEGER PRIMARY KEY, painter_id INTEGER, image TEXT, title TEXT, description TEXT, text TEXT, price INTEGER, likes INTEGER, dislikes INTEGER, comments INTEGER)")
        db.execSQL("CREATE TABLE PainterComments (id INTEGER PRIMARY KEY AUTOINCREMENT, painter_id INTEGER, user_id INTEGER, comment TEXT)")
        db.execSQL("CREATE TABLE PictureComments (id INTEGER PRIMARY KEY AUTOINCREMENT, picture_id INTEGER, user_id INTEGER, comment TEXT)")
        db!!.execSQL("CREATE TABLE PainterReaction (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, painter_id INTEGER, reaction INTEGER)") // 1 0 -1
        db!!.execSQL("CREATE TABLE PictureReaction (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, picture_id INTEGER, reaction INTEGER)")
        db!!.execSQL("CREATE TABLE PainterFavorites (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, painter_id INTEGER, reaction INTEGER)")
        db!!.execSQL("CREATE TABLE PictureFavorites (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, picture_id INTEGER, reaction INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS Users")
        db.execSQL("DROP TABLE IF EXISTS Painters")
        db.execSQL("DROP TABLE IF EXISTS Pictures")
        db.execSQL("DROP TABLE IF EXISTS PainterComments")
        db.execSQL("DROP TABLE IF EXISTS PictureComments")
        db.execSQL("DROP TABLE IF EXISTS PainterReaction")
        db.execSQL("DROP TABLE IF EXISTS PictureReaction")
        db.execSQL("DROP TABLE IF EXISTS PainterFavorites")
        db.execSQL("DROP TABLE IF EXISTS PictureFavorites")
        onCreate(db)
    }

    /////////ПОЛЬЗОВАТЕЛИ//////
    @SuppressLint("Range")
    fun getUserByName(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM Users WHERE login = ?", arrayOf(name))
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
        val cursor = db.rawQuery("SELECT login FROM Users WHERE id = ?", arrayOf(userId.toString()))
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
        db.insert("Users", null, values)
        db.close()
    }
    fun checkUser(login: String, password: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM Users WHERE login = '$login' AND password = '$password'", null)
        return result.moveToFirst()
    }
    fun checkLogin(login: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM Users WHERE login = '$login'", null)
        return result.moveToFirst()
    }
    fun checkMail(mail: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM Users WHERE mail = '$mail'", null)
        return result.moveToFirst()
    }
    /////////ПОЛЬЗОВАТЕЛИ//////

    /////////ХУДОЖНИКИ//////
    @SuppressLint("Range")
    fun getAllCommPainter(painterId: Int): List<CommentClass> {
        val db = this.readableDatabase
        val commentsList = mutableListOf<CommentClass>()
        val cursor = db.rawQuery("SELECT * FROM PainterComments WHERE painter_id = ?", arrayOf(painterId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idPainter = cursor.getInt(cursor.getColumnIndex("painter_id"))
                val idUser = cursor.getInt(cursor.getColumnIndex("user_id"))
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
        values.put("painter_id", comm.idPainter)
        values.put("user_id", comm.idUser)
        values.put("comment", comm.comment)
        db.insert("PainterComments", null, values)
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
        db.insert("Painters", null, values)
        db.close()
    }
    fun checkPainter(namePainter: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM Painters WHERE name = '$namePainter'", null)
        return result.moveToFirst()
    }
    @SuppressLint("Range")
    fun getPainter(idd: Int): PainterClass {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, image, name, yearsOfLife, stile, likes, comm, dis FROM Painters WHERE id = '$idd'", null)
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
        val cursor = db.rawQuery("SELECT id FROM Painters WHERE name = '$name'", null)
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
        val cursor = db.rawQuery("SELECT name FROM Painters WHERE id = '$idd'", null)
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
        val cursor = db.rawQuery("SELECT likes FROM Painters WHERE id = '$idd'", null)
        var likes = 0
        if (cursor.moveToFirst()){
            likes = cursor.getInt(cursor.getColumnIndex("likes"))
        }
        cursor.close()
        return likes.toString()
    }
    @SuppressLint("Range")
    fun getDisCount(idd: Int): String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT dis FROM Painters WHERE id = '$idd'", null)
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
        values.put("user_id", userId)
        values.put("painter_id", painterId)
        values.put("reaction", reaction)
        db.insert("PainterReaction", null, values)
        db.close()
    }
    fun updateLikePainter(userId: Int, painterId: Int, reaction: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE PainterReaction SET reaction = '$reaction' WHERE user_id = '$userId' and painter_id = '$painterId'")
        db.close()
    }
    fun findUserReactOnPainter(userId: Int, painterId: Int):Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM PainterReaction WHERE user_id = '$userId' AND painter_id = '$painterId'", null)
        return cursor.moveToFirst()
    }
    @SuppressLint("Range")
    fun getUserReactOnPainter(userId: Int, painterId: Int): Int{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT reaction FROM PainterReaction WHERE user_id = '$userId' AND painter_id = '$painterId'", null)
        var reaction = 0
        if (cursor.moveToFirst()){
            reaction = cursor.getInt(cursor.getColumnIndex("reaction"))
        }
        cursor.close()
        return reaction
    }
    fun incLike (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE Painters SET likes = likes + 1 WHERE id = '$idd'")
        db.close()
    }
    fun incComm (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE Painters SET comm = comm + 1 WHERE id = '$idd'")
        db.close()
    }
    fun incDis (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE Painters SET dis = dis + 1 WHERE id = '$idd'")
        db.close()
    }
    fun decLike (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE Painters SET likes = likes - 1 WHERE id = '$idd'")
        db.close()
    }
    fun decDis (idd: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE Painters SET dis = dis - 1 WHERE id = '$idd'")
        db.close()
    }
    /////////ХУДОЖНИКИ//////

    /////////КАРТИНЫ//////
    @SuppressLint("Range")
    fun getPicturesByPainterId(painterId: String): ArrayList<Picture> {
        val db = this.readableDatabase
        val picturesList = ArrayList<Picture>()
        val cursor = db.rawQuery("SELECT * FROM Pictures WHERE painter_id = ?", arrayOf(painterId))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val idPainter = cursor.getInt(cursor.getColumnIndex("painter_id"))
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
        values.put("painter_id", picture.idPainter)
        values.put("image", picture.image)
        values.put("title", picture.title)
        values.put("description", picture.desc)
        values.put("text", picture.text)
        values.put("price", picture.price)
        values.put("likes", picture.likes)
        values.put("dislikes", picture.dislikes)
        values.put("comments", picture.comments)
        val db = this.writableDatabase
        db.insert("Pictures", null, values)
        db.close()
    }
    @SuppressLint("Range")
    fun getField(idPicture: Int, field: String): String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $field FROM Pictures WHERE id = ?", arrayOf(idPicture.toString()))
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
        values.put("user_id", userId)
        values.put("picture_id", pictureId)
        values.put("reaction", reaction)
        db.insert("PictureReaction", null, values)
        db.close()
    }
    fun updateReactionPicture(userId: Int, pictureId: Int, reaction: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE PictureReaction SET reaction = '$reaction' WHERE user_id = '$userId' and picture_id = '$pictureId'")
        db.close()
    }
    fun changeReactionPicture(pictureId: Int, field: String, sign: String){
        val db = this.writableDatabase
        db.execSQL("UPDATE Pictures SET $field = $field $sign 1 WHERE id = '$pictureId'")
        db.close()
    }
    fun findUserReactOnPicture(userId: Int, pictureId: Int):Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM PictureReaction WHERE user_id = '$userId' AND picture_id = '$pictureId'", null)
        return cursor.moveToFirst()
    }
    @SuppressLint("Range")
    fun getUserReactOnPicture(userId: Int, pictureId: Int): Int{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT reaction FROM PictureReaction WHERE user_id = '$userId' AND picture_id = '$pictureId'", null)
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
        val cursor = db.rawQuery("SELECT $field FROM Pictures WHERE id = '$pictureId'", null)
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
        val cursor = db.rawQuery("SELECT * FROM PictureComments WHERE picture_id = ?", arrayOf(pictureId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idPicture = cursor.getInt(cursor.getColumnIndex("picture_id"))
                val idUser = cursor.getInt(cursor.getColumnIndex("user_id"))
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
        values.put("picture_id", comm.idPicture)
        values.put("user_id", comm.idUser)
        values.put("comment", comm.comment)
        db.insert("PictureComments", null, values)
        db.close()
    }
    /////////КАРТИНЫ//////

    ////////ThreeActions//////
    @SuppressLint("Range")
    fun getReactedPainterIds(userId: Int, reaction: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT painter_id FROM PainterReaction WHERE user_id = ? AND reaction = ?", arrayOf(userId.toString(), reaction.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("painter_id")))
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
            "SELECT picture_id FROM PictureReaction WHERE user_id = ? AND reaction = ?",
            arrayOf(userId.toString(), reaction.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("picture_id")))
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
        val cursor = db.rawQuery("SELECT * FROM Painters WHERE id IN ($idsString)", null)
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
        val cursor = db.rawQuery("SELECT * FROM Pictures WHERE id IN ($idsString)", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val idPainter = cursor.getInt(cursor.getColumnIndex("painter_id"))
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
    fun findIsFav(idUser: Int, idd: Int, field: String, field2: String): Int{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT reaction FROM $field WHERE $field2 = '$idd' and user_id = '$idUser'", null)
        var reaction = 0
        if (cursor.moveToFirst()){
            reaction = cursor.getInt(cursor.getColumnIndex("reaction"))
        }
        cursor.close()
        return reaction
    }
    fun changeStatusFav(idUser: Int, idd: Int, field: String, field2: String, reaction: Int){
        val db = this.writableDatabase
        db.execSQL("UPDATE $field SET reaction = $reaction WHERE $field2 = '$idd' and user_id = '$idUser'")
        db.close()

    }
    fun chooseStatusFav(userId: Int, itemId: Int, reaction: Int, field: String, field2: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("user_id", userId)
        values.put("$field2", itemId)
        values.put("reaction", reaction)
        db.insert("$field", null, values)
        db.close()
    }
    fun checkStatusFav(userId: Int, itemId: Int, field: String, field2: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $field WHERE user_id = '$userId' AND $field2 = '$itemId'", null)
        return cursor.moveToFirst()
    }

    @SuppressLint("Range")
    fun getFavPainterIds(userId: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT painter_id FROM PainterFavorites WHERE user_id = ? AND reaction = 1", arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("painter_id")))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ids
    }
    @SuppressLint("Range")
    fun getFavPictureIds(userId: Int): List<Int> {
        val db = this.readableDatabase
        val ids = mutableListOf<Int>()
        val cursor = db.rawQuery(
            "SELECT picture_id FROM PictureFavorites WHERE user_id = ? AND reaction = 1", arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndex("picture_id")))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ids
    }
}