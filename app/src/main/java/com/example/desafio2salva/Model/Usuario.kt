package com.example.desafio2salva.Model
import java.io.Serializable
data class Usuario(var email: String, var lat:Double, var lon:Double, var admin: Boolean, var verificado: Boolean):Serializable
