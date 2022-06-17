package com.example.desafio2salva.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.intentFoto


class RecyclerHomeAdmin (var context: AppCompatActivity, var evento:ArrayList<Evento>) : RecyclerView.Adapter<RecyclerHomeAdmin.ViewHolder>(){


    override fun getItemCount(): Int {
        return this.evento?.size!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.evento_card, parent, false), context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = evento[position]
        holder.bind(item, context, this)
    }



    class ViewHolder(view: View, context: AppCompatActivity): RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txtNombreUserCard)
        val fecha = view.findViewById<TextView>(R.id.txtLatUserCard)
        val hora = view.findViewById<TextView>(R.id.txtInfoEditEvento)
        val numAsistentes = view.findViewById<TextView>(R.id.txtAsistentesEventoCard)
        val chkActivado = view.findViewById<CheckBox>(R.id.chkVerificado)

        fun bind(e: Evento, context: AppCompatActivity, adaptador: RecyclerHomeAdmin) {

            chkActivado.text = ("Act/Des")
            chkActivado.isChecked = e.estado
            nombre.text = e.nombre
            fecha.text = e.fecha
            hora.text = e.hora
            numAsistentes.text = "Participantes: "+ e.asistentes.size.toString()


            itemView.setOnClickListener {

                intentFoto.putExtra("evento", e)
                context.startActivity(intentFoto)

            }

        }
    }
}