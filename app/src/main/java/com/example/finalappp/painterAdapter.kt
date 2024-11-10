package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class painterAdapter(var painters: List<PainterClass>, var context: Context):RecyclerView.Adapter<painterAdapter.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.painterImg) //портрет
        val name:TextView = view.findViewById(R.id.painterName) // имя
        val years:TextView = view.findViewById(R.id.painterYears) // годы жизни
        val style:TextView = view.findViewById(R.id.painterStyle) // стиль работ
        val butt: Button = view.findViewById(R.id.painterBut) // кнопка на картины
        val like:ImageView = view.findViewById(R.id.imgLike) // лайк
        val dis:ImageView = view.findViewById(R.id.imgDis) // диз
        val comm:ImageView = view.findViewById(R.id.imgComm) // коммент
        val countLike:TextView = view.findViewById(R.id.countLike) // кол - во лайков
        val countDis:TextView = view.findViewById(R.id.countDis) // кол - во дизов
        val countComm:TextView = view.findViewById(R.id.countComm) // кол - во комментов
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

        holder.butt.setOnClickListener(){
            val intent = Intent(context, mainStr::class.java)
            var who: String = ""
            if (holder.name.text == "Константин Васильев"){ who = "1" }
            else if (holder.name.text == "Иван Шишкин"){ who = "2" }
            intent.putExtra("painter", who)
            context.startActivity(intent)
        }

        holder.like.setOnClickListener(){

        }

    }

}