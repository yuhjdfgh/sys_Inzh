package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class painterAdapter(var painters: List<PainterClass>, var context: Context):RecyclerView.Adapter<painterAdapter.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.painterImg)
        val name:TextView = view.findViewById(R.id.painterName)
        val years:TextView = view.findViewById(R.id.painterYears)
        val style:TextView = view.findViewById(R.id.painterStyle)
        val butt: Button = view.findViewById(R.id.painterBut)
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

//        holder.butt.setOnClickListener{
//            val intent = Intent(context, picturePodrob::class.java)
//
//            intent.putExtra("pictureTitle", pictures[position].title)
//            intent.putExtra("pictureText", pictures[position].text)
//            intent.putExtra("pictureText", pictures[position].text)
//            //intent.putExtra("pictureImg", imageID)
//
//            context.startActivity(intent)
//        }

    }

}