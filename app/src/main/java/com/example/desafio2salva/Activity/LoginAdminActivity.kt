package com.example.desafio2salva.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.desafio2salva.Fragments.EventoFragment
import com.example.desafio2salva.Fragments.HomeFragment
import com.example.desafio2salva.R
import com.example.desafio2salva.Fragments.UsuarioFragment
import com.example.desafio2salva.Utils.Auxiliar.getEventosDeHoy
import com.example.desafio2salva.Utils.Auxiliar.getEventosSinRecarga
import com.example.desafio2salva.Utils.Auxiliar.getUsuarios
import com.example.desafio2salva.Utils.Auxiliar.intentFoto
import com.example.desafio2salva.Utils.Auxiliar.listaEventosHoy
import com.example.desafio2salva.Utils.Auxiliar.listaUsuarios
import kotlinx.android.synthetic.main.activity_login_admin.*
import kotlinx.android.synthetic.main.nav_header.*

class LoginAdminActivity : AppCompatActivity() {
companion object {
    lateinit var con : AppCompatActivity
    lateinit var intentCrearEvento: Intent
}

    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        toogle = ActionBarDrawerToggle(this, drawerLayoutAdmin, R.string.open_drawer, R.string.close_drawer)
        drawerLayoutAdmin.addDrawerListener(toogle)
        toogle.syncState()

        intentCrearEvento = Intent (this,EventoActivity::class.java)
        intentFoto = Intent (this,EditEventoActivity::class.java)

        listaUsuarios = arrayListOf()
        listaEventosHoy = arrayListOf()

        getEventosDeHoy()
        getEventosSinRecarga()
        getUsuarios()
        con = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, HomeFragment())
                        commit()
                    }
                }
                R.id.nav_usuario -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, UsuarioFragment())
                        commit()
                    }
                }
                R.id.nav_evento -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, EventoFragment())
                        commit()
                    }
                }
            }
            drawerLayoutAdmin.closeDrawer(GravityCompat.START)
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            txtEmailAdmin.setText(intent.getStringExtra("email")!!)

            return true
        }
        return super.onOptionsItemSelected(item)
    }

}