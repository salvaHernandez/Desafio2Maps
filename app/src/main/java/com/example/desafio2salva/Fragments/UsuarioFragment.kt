package com.example.desafio2salva.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2salva.Activity.LoginAdminActivity.Companion.con
import com.example.desafio2salva.Adapter.RecyclerUsuario
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.listaUsuarios
import com.example.desafio2salva.Utils.Auxiliar.miAdapterUserAdmin
import kotlinx.android.synthetic.main.fragment_usuario.*


class UsuarioFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater?.inflate(R.layout.fragment_usuario, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerUsuariosAdmin.setHasFixedSize(true)
        recyclerUsuariosAdmin.layoutManager = LinearLayoutManager(view.context)
        miAdapterUserAdmin = RecyclerUsuario (con, listaUsuarios)
        recyclerUsuariosAdmin.adapter = miAdapterUserAdmin
    }
}