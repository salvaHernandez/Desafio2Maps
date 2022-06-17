package com.example.desafio2salva.Adapter

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.R


class RecyclerFotoEvento (var context: AppCompatActivity, var evento:Evento, var fotos: ArrayList<Bitmap>) : RecyclerView.Adapter<RecyclerFotoEvento.ViewHolder>(){


    override fun getItemCount(): Int {
        return this.fotos?.size!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.foto_card, parent, false), context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = fotos[position]
        holder.bind(item, context, this)
    }



    class ViewHolder(view: View, context: AppCompatActivity): RecyclerView.ViewHolder(view) {

        val imgView = view.findViewById<ImageView>(R.id.imgEvento)

        fun bind(foto: Bitmap, context: AppCompatActivity, adaptador: RecyclerFotoEvento) {

            imgView.setImageBitmap(foto)

        }
    }
}