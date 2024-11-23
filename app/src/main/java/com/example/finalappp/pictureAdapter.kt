package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class pictureAdapter(var pictures: List<Picture>, var context: Context):RecyclerView.Adapter<pictureAdapter.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.pictureImg)
        val title:TextView = view.findViewById(R.id.pictureTitle)
        val desc:TextView = view.findViewById(R.id.pictureDesc)
        val price:TextView = view.findViewById(R.id.picturePrice)
        val butt: Button = view.findViewById(R.id.pictureBut)
        val like: ImageButton = view.findViewById(R.id.imgLikePicture)
        val dis: ImageButton = view.findViewById(R.id.imgDisPicture)
        val comm: ImageButton = view.findViewById(R.id.imgCommPicture)
        val countLike:TextView = view.findViewById(R.id.countLikePicture)
        val countDis:TextView = view.findViewById(R.id.countDisPicture)
        val countComm:TextView = view.findViewById(R.id.countCommPicture)
        val izbr:ImageButton = view.findViewById(R.id.imgIzbrPicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pictureinlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pictures.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.title.text = pictures[position].title
        holder.desc.text = pictures[position].desc
        holder.price.text = pictures[position].price.toString()
        holder.countLike.text = pictures[position].likes.toString()
        holder.countComm.text = pictures[position].comments.toString()
        holder.countDis.text = pictures[position].dislikes.toString()
        val imageID = context.resources.getIdentifier(pictures[position].image, "drawable", context.packageName)
        holder.image.setImageResource(imageID)

        val db = DataBase(context, null)
        val sharedPrefs = context.getSharedPreferences("CurrentLogin", Context.MODE_PRIVATE)
        val currLogin: String = sharedPrefs.getString("loginKey", "").toString()

        if (db.checkStatusIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId")){
            if (db.findIsIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId") == 1){
                Toast.makeText(context, "Уже было", Toast.LENGTH_SHORT).show()
                holder.izbr.setImageResource(R.drawable.heartizbr)
            }
            else if (db.findIsIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId") == 0){
                Toast.makeText(context, "Уже было", Toast.LENGTH_SHORT).show()
                holder.izbr.setImageResource(R.drawable.heartclear)
            }
        }
        else{
            Toast.makeText(context, "Еще не было", Toast.LENGTH_SHORT).show()
            holder.izbr.setImageResource(R.drawable.heartclear)
        }

        holder.butt.setOnClickListener{
            val intent = Intent(context, picturePodrob::class.java)
            intent.putExtra("pictureId", pictures[position].id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        holder.like.setOnClickListener(){
            val db = DataBase(context, null)
            val currId: Int = db.getUserByName(currLogin)
            if  (db.findUserReactOnPicture(currId, pictures[position].id)){ // если была поставлена реакция
                if (db.getUserReactOnPicture(currId, pictures[position].id) == 1){ // если лайк
                    db.updateReactionPicture(currId, pictures[position].id, 0)
                    db.changeReactionPicture(pictures[position].id, "likes", "-")
                    holder.countLike.text = db.getFieldValue(pictures[position].id, "likes")
                }
                else if (db.getUserReactOnPicture(currId, pictures[position].id) == -1){ // если дизлайк
                    db.updateReactionPicture(currId, pictures[position].id, 1)
                    db.changeReactionPicture(pictures[position].id, "dislikes", "-")
                    db.changeReactionPicture(pictures[position].id, "likes", "+")
                    holder.countLike.text = db.getFieldValue(pictures[position].id, "likes")
                    holder.countDis.text = db.getFieldValue(pictures[position].id, "dislikes")
                }
                else{
                    db.updateReactionPicture(currId, pictures[position].id, 1)
                    db.changeReactionPicture(pictures[position].id, "likes", "+")
                    holder.countLike.text = db.getFieldValue(pictures[position].id, "likes")
                }

            }
            else { // не было реакции
                db.chooseReactionPicture(currId, pictures[position].id, 1)
                db.changeReactionPicture(pictures[position].id, "likes", "+")
                holder.countLike.text = db.getFieldValue(pictures[position].id, "likes")
            }
        }

        holder.dis.setOnClickListener(){
            val db = DataBase(context, null)
            val currId: Int = db.getUserByName(currLogin)
            if  (db.findUserReactOnPicture(currId, pictures[position].id)){ // если была поставлена реакция
                if (db.getUserReactOnPicture(currId, pictures[position].id) == -1){ // если дизлайк
                    db.updateReactionPicture(currId, pictures[position].id, 0)
                    db.changeReactionPicture(pictures[position].id, "dislikes", "-")
                    holder.countDis.text = db.getFieldValue(pictures[position].id, "dislikes")
                }
                else if (db.getUserReactOnPicture(currId, pictures[position].id) == 1){ // если лайк
                    db.updateReactionPicture(currId, pictures[position].id, -1)
                    db.changeReactionPicture(pictures[position].id, "likes", "-")
                    db.changeReactionPicture(pictures[position].id, "dislikes", "+")
                    holder.countLike.text = db.getFieldValue(pictures[position].id, "likes")
                    holder.countDis.text = db.getFieldValue(pictures[position].id, "dislikes")
                }
                else{
                    db.updateReactionPicture(currId, pictures[position].id, -1)
                    db.changeReactionPicture(pictures[position].id, "dislikes", "+")
                    holder.countDis.text = db.getFieldValue(pictures[position].id, "dislikes")
                }

            }
            else { // не было реакции
                db.chooseReactionPicture(currId, pictures[position].id, -1)
                db.changeReactionPicture(pictures[position].id, "dislikes", "+")
                holder.countDis.text = db.getFieldValue(pictures[position].id, "dislikes")
            }
        }

        holder.comm.setOnClickListener(){
            val intent = Intent(context, commentActivity::class.java)
            intent.putExtra("picture", pictures[position].id)
            val aaa = "2"
            intent.putExtra("whichComm", aaa)
            intent.putExtra("strToBack", (context as AppCompatActivity)::class.java.name)
            Toast.makeText(context, "$aaa", Toast.LENGTH_SHORT).show()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        holder.izbr.setOnClickListener {
            val db = DataBase(context, null)
            if (db.checkStatusIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId")){
                val isIzbr = db.findIsIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId")
                if (isIzbr == 1){
                    Toast.makeText(context, db.getUserByName(currLogin).toString() + "   " + pictures[position].id.toString() + "   1", Toast.LENGTH_SHORT).show()
                    db.changeStatusIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId", 0)
                    holder.izbr.setImageResource(R.drawable.heartclear)
                }
                else if (isIzbr == 0){
                    Toast.makeText(context, db.getUserByName(currLogin).toString() + "   " + pictures[position].id.toString() + "   2", Toast.LENGTH_SHORT).show()
                    db.changeStatusIzbr(db.getUserByName(currLogin), pictures[position].id, "izbrPicture", "pictureId", 1)
                    holder.izbr.setImageResource(R.drawable.heartizbr)
                }
            }
            else{
                db.chooseStatusIzbr(db.getUserByName(currLogin), pictures[position].id, 1,"izbrPicture", "pictureId")
                Toast.makeText(context, db.getUserByName(currLogin).toString() + "   " + pictures[position].id.toString() + "   3", Toast.LENGTH_SHORT).show()
                holder.izbr.setImageResource(R.drawable.heartizbr)
            }
        }

    }
}