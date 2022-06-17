package com.example.desafio2salva.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2salva.Activity.MainActivity
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.Model.Usuario
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar
import com.example.desafio2salva.Utils.Auxiliar.listaUsuarios
import com.example.desafio2salva.Utils.Auxiliar.modificaUsuario


class RecyclerUsuario (var context: AppCompatActivity, var listaUser:ArrayList<Usuario>): RecyclerView.Adapter<RecyclerUsuario.ViewHolder>() {


    override fun getItemCount(): Int {
        return this.listaUser?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.usuario_card,parent,false), context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = listaUser[position]
            holder.bind(item, context, this)
    }


    class ViewHolder(view: View, context: AppCompatActivity): RecyclerView.ViewHolder(view) {

        val nombre=view.findViewById<TextView>(R.id.txtNombreUserCard)
        val verificado=view.findViewById<Switch>(R.id.swVerificado)


        fun bind(u: Usuario, context: AppCompatActivity, adaptador: RecyclerUsuario) {

            nombre.text=u.email
            verificado.isChecked = u.verificado


            itemView.setOnClickListener{
                if(verificado.isChecked) {
                    ModificarVerificacion(u, false)
                } else {
                    ModificarVerificacion(u, true)
                }
            }

            itemView.setOnLongClickListener ({
                BorrarUser(context, adaptador, u)
                true
            })

        }

        private fun ModificarVerificacion(u: Usuario, estado : Boolean) {
            verificado.isChecked = estado
            u.verificado = estado
            modificaUsuario(u)
        }

        private fun BorrarUser (context: AppCompatActivity, adaptador: RecyclerUsuario, u : Usuario) {
            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.smsBorrarUser)
                .setPositiveButton(R.string.btnSi) { view, _ ->
                    // Hacer el borrado
                    MainActivity.db.collection("users").document(u.email).delete()
                    listaUsuarios.remove(u)
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
