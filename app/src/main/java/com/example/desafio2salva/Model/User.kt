package com.example.desafio2salva.Model

import android.content.Context

class User (val context: Context) {

    val SHARED_NAME = "bdAguacate"
    val SHARED_CORREO = "correo"
    val SHARED_PWD = "pass"
    val SHARED_ADMIN = "admin"
    val SHARED_VERIFICADO = "verificado"
    val SHARED_PROVIDER = "provider"
    val SHARED_RECOR = "recor"



    //CREAMOS LA BASE DE DATOS
    val storage = context.getSharedPreferences(SHARED_NAME, 0)


    /*
     * FUN GUARDA VALORES
     */
    fun saveCorreo(correo:String){
        storage.edit().putString(SHARED_CORREO,correo).apply()
    }

    fun savePwd(pwd:String){
        storage.edit().putString(SHARED_PWD,pwd).apply()
    }

    fun saveIsAdmin(admin:Boolean){
        storage.edit().putBoolean(SHARED_ADMIN, admin).apply()
    }

    fun saveVerificado(verificado:Boolean){
        storage.edit().putBoolean(SHARED_VERIFICADO, verificado).apply()
    }

    fun saveProvider(prov:Boolean){
        storage.edit().putBoolean(SHARED_PWD, prov).apply()
    }

    fun saveRecordar(recor:Boolean){
        storage.edit().putBoolean(SHARED_RECOR, recor).apply()
    }


    /*
     * FUN DEVUELVE VALORES
     */

    fun getCorreo():String{
        return storage.getString(SHARED_CORREO, "")!!
    }

    fun getPwd():String{
        return  storage.getString(SHARED_PWD, "")!!
    }

    fun getIsAdmin():Boolean{
        return  storage.getBoolean(SHARED_ADMIN, false)
    }

    fun getVerificado():Boolean{
        return  storage.getBoolean(SHARED_VERIFICADO, false)
    }

    fun getProvider():Boolean{
        return  storage.getBoolean(SHARED_PROVIDER, false)!!
    }

    fun getRecor():Boolean{
        return  storage.getBoolean(SHARED_RECOR, false)
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}