package com.example.desafio2salva.Model

import android.app.Application

class UsuarioGuardado  : Application() {
    companion object{
        lateinit var user: User
    }

    override fun onCreate() {
        super.onCreate()
        user = User(applicationContext)
    }
}

