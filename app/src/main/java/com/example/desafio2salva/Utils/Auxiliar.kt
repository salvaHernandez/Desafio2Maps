package com.example.desafio2salva.Utils

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.desafio2salva.Activity.MainActivity
import com.example.desafio2salva.Activity.MainActivity.Companion.db
import com.example.desafio2salva.Adapter.*
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.Model.Usuario
import com.example.desafio2salva.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

object Auxiliar {


    var listaImagenes : ArrayList<Bitmap> = arrayListOf()
    lateinit var listaUsuarios : ArrayList<Usuario>
    lateinit var listaEventos : ArrayList<Evento>
    lateinit var listaEventosHoy : ArrayList<Evento>
    lateinit var miAdapterEventoAdmin: RecyclerEventoAdmin
    lateinit var miAdapterEvento: RecyclerEvento
    lateinit var miAdapterUserAdmin: RecyclerUsuario
    lateinit var miAdapterFoto : RecyclerFotoEvento
    lateinit var miAdapterEventosHoy: RecyclerHomeAdmin
    lateinit var intentFoto : Intent

    private val storageRef = Firebase.storage.reference



    fun getBytes(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun CargarImagenes(nombre : String) {
        listaImagenes.clear()
        storageRef.child("$nombre/").listAll().addOnSuccessListener { lista ->
                for (i in lista.items) {
                    i.getBytes(1024 * 1024).addOnSuccessListener {
                        val img = getBitmap(it)!!
                        listaImagenes.add(img)
                        miAdapterFoto.notifyDataSetChanged()
                    }
                }
        }
    }


    fun SubirImg(image: Bitmap, nombre : String) {
        val imgRef = storageRef.child("$nombre/${Random.nextInt()}.jpg")
        imgRef.putBytes(getBytes(image)!!)
        CargarImagenes(nombre)
    }

    fun getBitmap(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }


    fun getEventos(adapterIsAdmin: Boolean) {
        listaEventos.clear()
        //Coger todos los elementos de la colección.

        if (adapterIsAdmin) {
            db.collection("evento").get().addOnSuccessListener { result ->

                var e : Evento

                for (document in result) {

                    var id = document.id

                    e = Evento(
                        id,
                        document.get("asistentes") as ArrayList<String>,
                        document.get("nombre") as String,
                        document.get("fecha") as String,
                        document.get("hora") as String,
                        document.get("lat") as Double,
                        document.get("lon") as Double,
                        document.get("estado") as Boolean
                    )
                    listaEventos.add(e)
                }
                miAdapterEventoAdmin.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error: ", exception)
            }
        } else {
            db.collection("evento").whereEqualTo("estado", true).get().addOnSuccessListener { result ->

                var e : Evento

                for (document in result) {

                    var id = document.id

                    e = Evento(
                        id,
                        document.get("asistentes") as ArrayList<String>,
                        document.get("nombre") as String,
                        document.get("fecha") as String,
                        document.get("hora") as String,
                        document.get("lat") as Double,
                        document.get("lon") as Double,
                        document.get("estado") as Boolean
                    )
                    listaEventos.add(e)
                }
                miAdapterEvento.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error: ", exception)
            }
        }
    }


    fun getEventosSinRecarga() {

        listaEventos.clear()
        //Coger todos los elementos de la colección.
        db.collection("evento").get().addOnSuccessListener { result ->

            var e : Evento

            for (document in result) {

                var id = document.id

                e = Evento(
                    id,
                    document.get("asistentes") as ArrayList<String>,
                    document.get("nombre") as String,
                    document.get("fecha") as String,
                    document.get("hora") as String,
                    document.get("lat") as Double,
                    document.get("lon") as Double,
                    document.get("estado") as Boolean
                )
                listaEventos.add(e)
            }
        }
        .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error: ", exception)
        }
    }


    fun getEventosDeHoy() {

       // Obtenemos la fecha del día de hoy
        var date : Date
        date = Calendar.getInstance().time
        var formato = SimpleDateFormat("dd/MM/yyyy")
        var fecha = formato.format(date)

        Log.d("Salva", fecha)

        listaEventosHoy.clear()
        //Coger todos los elementos de la colección.
        db.collection("evento").whereEqualTo("fecha", fecha).get().addOnSuccessListener { result ->

            var e : Evento

            for (document in result) {

                var id = document.id

                e = Evento(
                    id,
                    document.get("asistentes") as ArrayList<String>,
                    document.get("nombre") as String,
                    document.get("fecha") as String,
                    document.get("hora") as String,
                    document.get("lat") as Double,
                    document.get("lon") as Double,
                    document.get("estado") as Boolean
                )
                listaEventosHoy.add(e)
            }
            miAdapterEventosHoy.notifyDataSetChanged()
        }
        .addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error: ", exception)
        }
    }

    fun getUsuarios () {

        listaUsuarios.clear()
        //Coger todos los elementos de la colección.
        db.collection("users").get().addOnSuccessListener { result ->

            var u : Usuario

            for (document in result) {

                var id = document.id

                u = Usuario(
                    id,
                    document.get("lat") as Double,
                    document.get("lon") as Double,
                    document.get("admin") as Boolean,
                    document.get("verificado") as Boolean
                )
                listaUsuarios.add(u)
            }
        }
        .addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error: ", exception)
        }
    }

    fun modificaEvento (e:Evento, adapterIsAdmin: Boolean) {
        db.collection("evento").document(e.id).set(
            hashMapOf(
                "asistentes" to e.asistentes,
                "nombre" to e.nombre,
                "fecha" to e.fecha,
                "hora" to e.hora,
                "lat" to e.lat,
                "lon" to e.lon,
                "estado" to e.estado
            )
        )
        if (adapterIsAdmin) {
            miAdapterEventoAdmin.notifyDataSetChanged()
        } else miAdapterEvento.notifyDataSetChanged()
    }

    fun modificaUsuario (u:Usuario, usaAdapter:Boolean) {
        db.collection("users").document(u.email).set(
            hashMapOf(
                "admin" to u.admin,
                "lat" to u.lat,
                "lon" to u.lon,
                "verificado" to u.verificado
            )
        )
        if (usaAdapter) {
            miAdapterUserAdmin.notifyDataSetChanged()
        }
    }
}