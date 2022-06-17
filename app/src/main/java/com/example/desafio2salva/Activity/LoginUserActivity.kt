package com.example.desafio2salva.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2salva.Adapter.RecyclerEvento
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.getEventos
import com.example.desafio2salva.Utils.Auxiliar.listaEventos
import com.example.desafio2salva.Utils.Auxiliar.miAdapterEvento
import kotlinx.android.synthetic.main.activity_login_user.*

class LoginUserActivity : AppCompatActivity() {
companion object {
    lateinit var conUser : AppCompatActivity
}
    var userEmail : String = ""
    var verificado : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        supportActionBar?.hide()
        userEmail = intent.getStringExtra("email")!!
        verificado = intent.getBooleanExtra("verificado",false)

        conUser = this



        if (verificado) {
            getEventos(false)
        } else {
            Toast.makeText(this, R.string.smsUsuarioSinActivar, Toast.LENGTH_LONG).show()
        }

        recyclerEventos.setHasFixedSize(true)
        recyclerEventos.layoutManager = LinearLayoutManager(this)
        miAdapterEvento = RecyclerEvento(this, listaEventos, userEmail)
        recyclerEventos.adapter = miAdapterEvento

        recyclerEventos.isEnabled = verificado

    }

}