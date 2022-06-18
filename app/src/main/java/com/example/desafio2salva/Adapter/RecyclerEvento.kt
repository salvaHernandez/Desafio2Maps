package com.example.desafio2salva.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.modificaEvento

class RecyclerEvento (var context: AppCompatActivity, var evento:ArrayList<Evento>, var usuario:String): RecyclerView.Adapter<RecyclerEvento.ViewHolder>() {


    override fun getItemCount(): Int {
        return this.evento?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.evento_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = evento[position]
        holder.bind(item, context, usuario)
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txtNombreUserCard)
        val fecha = view.findViewById<TextView>(R.id.txtLatUserCard)
        val hora = view.findViewById<TextView>(R.id.txtInfoEditEvento)
        val numAsistentes = view.findViewById<TextView>(R.id.txtAsistentesEventoCard)
        val chkAsiste = view.findViewById<CheckBox>(R.id.chkVerificado)

        fun bind(e: Evento, context: AppCompatActivity, usuario: String){

            chkAsiste.text = ("Asistencia")
            nombre.text = e.nombre
            fecha.text = e.fecha
            hora.text = e.hora
            numAsistentes.text = "Participantes: "+ e.asistentes.size.toString()

            chkAsiste.isChecked = e.asistentes.contains(usuario)


            itemView.setOnClickListener{
                if(chkAsiste.isChecked) {
                    SalirEvento(context, usuario, e)
                } else {
                    UnionEvento(context, usuario, e)
                }
            }

        }

        private fun UnionEvento(context: AppCompatActivity, usuario: String, e: Evento) {
            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.smsUnirEvento)
                .setPositiveButton(R.string.btnSi) { view, _ ->
                    //Entrar
                    chkAsiste.isChecked = true

                    e.asistentes.add(usuario)
                    modificaEvento(e, false)
                    view.dismiss()
                }
                .setNegativeButton(R.string.btnNo) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()

        }


        private fun SalirEvento(context: AppCompatActivity, usuario: String, e: Evento) {
            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.smsSalirEvento)
                .setPositiveButton(R.string.btnSi) { view, _ ->
                    // Salir
                    chkAsiste.isChecked = false
                    e.asistentes.remove(usuario)
                    modificaEvento(e, false)

                    view.dismiss()
                }
                .setNegativeButton(R.string.btnNo) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()
        }
    }
}