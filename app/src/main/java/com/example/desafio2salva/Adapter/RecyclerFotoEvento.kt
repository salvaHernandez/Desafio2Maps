package com.example.desafio2salva.Adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2salva.R


class RecyclerFotoEvento (var context: AppCompatActivity, var fotos: ArrayList<Bitmap>) : RecyclerView.Adapter<RecyclerFotoEvento.ViewHolder>(){


    override fun getItemCount(): Int {
        return this.fotos?.size!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.foto_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = fotos[position]
        holder.bind(item)
    }



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imgView = view.findViewById<ImageView>(R.id.imgEvento)

        fun bind(foto: Bitmap) {

            imgView.setImageBitmap(foto)

        }
    }
}