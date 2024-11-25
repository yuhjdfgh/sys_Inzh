package com.example.finalappp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.finalappp.DataBase
import com.example.finalappp.R
import com.example.finalappp.activitys.commentActivity
import com.example.finalappp.activitys.mainStr
import com.example.finalappp.classes.PainterClass

class painterAdapter(var painters: List<PainterClass>, var context: Context):RecyclerView.Adapter<painterAdapter.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.painterImg)
        val name:TextView = view.findViewById(R.id.painterName)
        val years:TextView = view.findViewById(R.id.painterYears)
        val style:TextView = view.findViewById(R.id.painterStyle)
        val butt: Button = view.findViewById(R.id.painterBut)
        val like:ImageButton = view.findViewById(R.id.imgLike)
        val dis:ImageButton = view.findViewById(R.id.imgDis)
        val comm:ImageButton = view.findViewById(R.id.imgComm)
        val countLike:TextView = view.findViewById(R.id.countLike)
        val countDis:TextView = view.findViewById(R.id.countDis)
        val countComm:TextView = view.findViewById(R.id.countComm)
        val izbr:ImageButton = view.findViewById(R.id.imgIzbr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.painterinlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return painters.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = painters[position].name
        holder.years.text = painters[position].yearsOfLife
        holder.style.text = painters[position].stile.toString()
        val imageID = context.resources.getIdentifier(painters[position].image, "drawable", context.packageName)
        holder.image.setImageResource(imageID)
        holder.countLike.text = painters[position].likes.toString()
        holder.countComm.text = painters[position].comm.toString()
        holder.countDis.text = painters[position].dis.toString()
        val db = DataBase(context, null)

        val sharedPrefs = context.getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
        val currLogin: String = sharedPrefs.getString("loginKey", "").toString()

        if (db.checkStatusFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id")){
            if (db.findIsFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id") == 1){
                holder.izbr.setImageResource(R.drawable.heartizbr)
            }
            else if (db.findIsFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id") == 0){
                holder.izbr.setImageResource(R.drawable.heartclear)
            }
        }
        else{
            holder.izbr.setImageResource(R.drawable.heartclear)
        }

        holder.butt.setOnClickListener(){
            val db = DataBase(context, null)
            val sharedPrefs = context.getSharedPreferences("CurrentPainter", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("painterKey", db.getPainterByName(painters[position].name))
            editor.apply()
            val intent = Intent(context, mainStr::class.java)
            val who = db.getPainterByName(holder.name.text as String)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        holder.like.setOnClickListener(){
            val db = DataBase(context, null)
            val currId: Int = db.getUserByName(currLogin)
            if  (db.findUserReactOnPainter(currId, painters[position].id)){ // если была поставлена реакция
                if (db.getUserReactOnPainter(currId, painters[position].id) == 1){ // если лайк
                    db.updateLikePainter(currId, painters[position].id, 0)
                    db.decLike(painters[position].id)
                    holder.countLike.text = db.getLikeCount(painters[position].id)
                }
                else if (db.getUserReactOnPainter(currId, painters[position].id) == -1){ // если дизлайк
                    db.updateLikePainter(currId, painters[position].id, 1)
                    db.incLike(painters[position].id)
                    db.decDis(painters[position].id)
                    holder.countLike.text = db.getLikeCount(painters[position].id)
                    holder.countDis.text = db.getDisCount(painters[position].id)
                }
                else{
                    db.updateLikePainter(currId, painters[position].id, 1)
                    db.incLike(painters[position].id)
                    holder.countLike.text = db.getLikeCount(painters[position].id)
                }

            }
            else { // не было реакции
                db.chooseLikePainter(currId, painters[position].id, 1)
                db.incLike(painters[position].id)
                holder.countLike.text = db.getLikeCount(painters[position].id)
            }
        }

        holder.dis.setOnClickListener(){
            val db = DataBase(context, null)
            val currId: Int = db.getUserByName(currLogin)
            if  (db.findUserReactOnPainter(currId, painters[position].id)){ // если была поставлена реакция
                if (db.getUserReactOnPainter(currId, painters[position].id) == -1){ // если дизлайк
                    db.updateLikePainter(currId, painters[position].id, 0)
                    db.decDis(painters[position].id)
                    holder.countDis.text = db.getDisCount(painters[position].id)
                }
                else if (db.getUserReactOnPainter(currId, painters[position].id) == 1){ // если лайк
                    db.updateLikePainter(currId, painters[position].id, -1)
                    db.incDis(painters[position].id)
                    db.decLike(painters[position].id)
                    holder.countLike.text = db.getLikeCount(painters[position].id)
                    holder.countDis.text = db.getDisCount(painters[position].id)
                }
                else{
                    db.updateLikePainter(currId, painters[position].id, -1)
                    db.incDis(painters[position].id)
                    holder.countDis.text = db.getDisCount(painters[position].id)
                }

            }
            else { // не было реакции
                db.chooseLikePainter(currId, painters[position].id, -1)
                db.incDis(painters[position].id)
                holder.countDis.text = db.getDisCount(painters[position].id)
            }
        }

        holder.comm.setOnClickListener(){
            val intent = Intent(context, commentActivity::class.java)
            val db = DataBase(context, null)
            val who = db.getPainterByName(holder.name.text as String)
            intent.putExtra("painter", who)
            intent.putExtra("strToBack", (context as AppCompatActivity)::class.java.name)
            intent.putExtra("whichComm", "1")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        holder.izbr.setOnClickListener {
            val db = DataBase(context, null)
            if (db.checkStatusFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id")){
                val isIzbr = db.findIsFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id")
                if (isIzbr == 1){
                    db.changeStatusFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id", 0)
                    holder.izbr.setImageResource(R.drawable.heartclear)
                }
                else if (isIzbr == 0){
                    db.changeStatusFav(db.getUserByName(currLogin), painters[position].id, "PainterFavorites", "painter_id", 1)
                    holder.izbr.setImageResource(R.drawable.heartizbr)
                }
            }
            else{
                db.chooseStatusFav(db.getUserByName(currLogin), painters[position].id, 1,"PainterFavorites", "painter_id")
                holder.izbr.setImageResource(R.drawable.heartizbr)
            }
        }

    }
}