package com.example.desafio2salva.Model

import java.io.Serializable

data class Evento(var id: String, var asistentes: ArrayList<String>, var nombre:String, var fecha:String, var hora:String, var lat:Double, var lon:Double, var estado: Boolean):Serializable
