package com.example.desafio2salva.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2salva.Activity.LoginAdminActivity.Companion.con
import com.example.desafio2salva.Adapter.RecyclerHomeAdmin
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.listaEventos
import com.example.desafio2salva.Utils.Auxiliar.listaEventosHoy
import com.example.desafio2salva.Utils.Auxiliar.miAdapterEventosHoy
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //  return super.onCreateView(inflater, container, savedInstanceState) {
        val view=inflater?.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerHomeAdmin.setHasFixedSize(true)
        recyclerHomeAdmin.layoutManager = LinearLayoutManager(view.context)
        miAdapterEventosHoy = RecyclerHomeAdmin (con, listaEventosHoy)
        recyclerHomeAdmin.adapter = miAdapterEventosHoy

        txtMostrarTodos.setOnClickListener {
            miAdapterEventosHoy = RecyclerHomeAdmin (con, listaEventos)
            recyclerHomeAdmin.adapter = miAdapterEventosHoy
        }
    }
}