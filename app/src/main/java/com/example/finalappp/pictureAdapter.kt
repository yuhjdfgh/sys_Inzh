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

class pictureAdapter(var pictures: List<Picture>, var context: Context):RecyclerView.Adapter<pictureAdapter.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.pictureImg)
        val title:TextView = view.findViewById(R.id.pictureTitle)
        val desc:TextView = view.findViewById(R.id.pictureDesc)
        val price:TextView = view.findViewById(R.id.picturePrice)
        val butt: Button = view.findViewById(R.id.pictureBut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pictureinlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pictures.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = pictures[position].title
        holder.desc.text = pictures[position].desc
        holder.price.text = pictures[position].price.toString()

        val imageID = context.resources.getIdentifier(pictures[position].image, "drawable", context.packageName)
        holder.image.setImageResource(imageID)

        holder.butt.setOnClickListener{
            val intent = Intent(context, picturePodrob::class.java)

            intent.putExtra("pictureTitle", pictures[position].title)
            intent.putExtra("pictureText", pictures[position].text)
            intent.putExtra("pictureText", pictures[position].text)

            context.startActivity(intent)
        }

    }

}