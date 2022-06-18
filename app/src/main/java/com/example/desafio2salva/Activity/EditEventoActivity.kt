package com.example.desafio2salva.Activity

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2salva.Adapter.RecyclerFotoEvento
import com.example.desafio2salva.Model.Evento
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.CargarImagenes
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_evento.*
import java.lang.Exception
import com.example.desafio2salva.Utils.Auxiliar.SubirImg
import com.example.desafio2salva.Utils.Auxiliar.listaImagenes
import com.example.desafio2salva.Utils.Auxiliar.miAdapterFoto


class EditEventoActivity : AppCompatActivity() {
    lateinit var event : Evento
    private val CAMERA_REQUEST = 1888
    lateinit var miStorage : StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_evento)

        event = intent.getSerializableExtra("evento")!! as Evento

        txtNombreEditEvento.text = event.nombre
        txtInfoEditEvento.append("Fecha ${event.fecha} , ${event.hora}, Asistentes: ${event.asistentes.size}")
        swAct.isChecked = event.estado

        // Storage Fotos
        miStorage = FirebaseStorage.getInstance().getReference()



        recyclerFoto.setHasFixedSize(true)
        recyclerFoto.layoutManager = LinearLayoutManager(this)
        miAdapterFoto = RecyclerFotoEvento(this, listaImagenes)
        recyclerFoto.adapter = miAdapterFoto

        CargarImagenes(event.nombre)


    }

    fun addFoto (view:View) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

                var img: Bitmap = data?.extras?.get("data") as Bitmap
                SubirImg(img!!, event.nombre)

            }
        } catch (e: Exception) {
            Toast.makeText(this, "Problema con el acceso a la camara", Toast.LENGTH_SHORT).show()
        }
    }

}