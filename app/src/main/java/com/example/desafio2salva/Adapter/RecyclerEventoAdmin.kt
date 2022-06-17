package com.example.desafio2salva.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2salva.Activity.MainActivity.Companion.db
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.listaEventos
import com.example.desafio2salva.Utils.Auxiliar.modificaEvento


class RecyclerEventoAdmin (var context: AppCompatActivity, var evento:ArrayList<Evento>) : RecyclerView.Adapter<RecyclerEventoAdmin.ViewHolder>(){


    override fun getItemCount(): Int {
        return this.evento?.size!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.evento_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = evento[position]
        holder.bind(item, context, this)
    }



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txtNombreUserCard)
        val fecha = view.findViewById<TextView>(R.id.txtLatUserCard)
        val hora = view.findViewById<TextView>(R.id.txtInfoEditEvento)
        val numAsistentes = view.findViewById<TextView>(R.id.txtAsistentesEventoCard)
        val chkActivado = view.findViewById<CheckBox>(R.id.chkVerificado)

        fun bind(e: Evento, context: AppCompatActivity, adaptador: RecyclerEventoAdmin) {

            chkActivado.text = ("Act/Des")
            chkActivado.isChecked = e.estado
            nombre.text = e.nombre
            fecha.text = e.fecha
            hora.text = e.hora
            numAsistentes.text = "Participantes: "+ e.asistentes.size.toString()


            itemView.setOnClickListener{

                if(chkActivado.isChecked) {
                    dialogDesactivar(context, e ,  false)
                } else {
                    dialogActivar(context, e ,  true)
                }
            }

            itemView.setOnLongClickListener {
                dialogBorrado(context, adaptador, e)
                true
            }

        }

        private fun dialogActivar (context: AppCompatActivity, e : Evento,  estado: Boolean) {
            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.smsActivarEnc)
                .setPositiveButton(R.string.btnActivar) { view, _ ->
                    // Hacer el ActivarDesactivar
                    e.estado = estado
                    chkActivado.isChecked = estado
                    modificaEvento(e, true)

                    view.dismiss()
                }
                .setNegativeButton(R.string.btnCancelar) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()

        }
        private fun dialogDesactivar (context: AppCompatActivity, e : Evento, estado: Boolean) {
            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.smsDesactivarEnc)
                .setPositiveButton(R.string.btnDesactivar) { view, _ ->
                    // Hacer el ActivarDesactivar
                    e.estado = estado
                    chkActivado.isChecked = estado
                    modificaEvento(e, true)

                    view.dismiss()
                }
                .setNegativeButton(R.string.btnCancelar) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()

        }


        private fun dialogBorrado (context: AppCompatActivity, adaptador: RecyclerEventoAdmin, e :Evento) {
            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.smsBorrarEvento)
                .setPositiveButton(R.string.btnSi) { view, _ ->
                    // Hacer el borrado
                    db.collection("evento").document(e.id).delete()
                    listaEventos.remove(e)
                    adaptador.notifyItemRemoved(adapterPosition)
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