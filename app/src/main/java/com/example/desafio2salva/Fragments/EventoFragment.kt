package com.example.desafio2salva.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2salva.Activity.LoginAdminActivity.Companion.conLoginAdmin
import com.example.desafio2salva.Activity.LoginAdminActivity.Companion.intentCrearEvento
import com.example.desafio2salva.Adapter.RecyclerEventoAdmin
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.getEventos
import com.example.desafio2salva.Utils.Auxiliar.getEventosDeHoy
import com.example.desafio2salva.Utils.Auxiliar.listaEventos
import com.example.desafio2salva.Utils.Auxiliar.miAdapterEventoAdmin
import kotlinx.android.synthetic.main.fragment_evento.*

class EventoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater?.inflate(R.layout.fragment_evento, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEventosAdmin.setHasFixedSize(true)
        recyclerEventosAdmin.layoutManager = LinearLayoutManager(view.context)
        miAdapterEventoAdmin = RecyclerEventoAdmin(conLoginAdmin, listaEventos)
        recyclerEventosAdmin.adapter = miAdapterEventoAdmin


        floatingActionButton.setOnClickListener {
            resultLauncher.launch(intentCrearEvento)
        }
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getEventos(true)
            getEventosDeHoy()
        }
    }


}