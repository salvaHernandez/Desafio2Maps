package com.example.desafio2salva.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.desafio2salva.Activity.MainActivity.Companion.db
import com.example.desafio2salva.R
import com.example.desafio2salva.picker.DatePickerFragment
import com.example.desafio2salva.picker.TimePickerFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_evento.*

class EventoActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private lateinit var map: GoogleMap
    private var latE: Double = 38.970923405116544
    private var lonE: Double = -3.9315805626847045


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)
        supportActionBar?.hide()
        createFragment()

        txtFechaEvento.setOnClickListener {
            showDatePickerDialog()
        }

        txtHoraEvento.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun onTimeSelected(time:String) {
        txtHoraEvento.setText("Hora: $time")
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment {onTimeSelected(it)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment {day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){

        var dia = day.toString()
        var mes = (month + 1).toString()
        if (day < 10) {
            dia = "0"+day.toString()
        }
        if (month < 10) {
            mes = "0"+(month + 1).toString()
        }

        txtFechaEvento.setText("" +dia+ "/" +mes+ "/" +year)
    }





    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setOnMapLongClickListener(this)
        val coordenadas = LatLng(38.970923405116544, -3.9315805626847045)
        createMarker(coordenadas)
    }

    private fun createMarker(coor: LatLng) {
        val marker: MarkerOptions = MarkerOptions().position(coor).title("mi posi")
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coor, 12f), 3000,null)
    }

    override fun onMapLongClick(p0: LatLng) {
        Toast.makeText(this, "Nuevo marcador"+p0.toString(), Toast.LENGTH_SHORT).show()
        val marker: MarkerOptions = MarkerOptions().position(p0).title("Nuevo marcador")

        latE = p0.latitude
        lonE = p0.longitude

        map.clear()
        map.addMarker(marker)
    }

    fun guardarEvento (view: View) {

        var asistentes = ArrayList<String>()

        var evento = hashMapOf (
                "asistentes" to asistentes,
                "nombre" to txtNombreEvento.text.toString(),
                "fecha" to txtFechaEvento.text,
                "hora" to txtHoraEvento.text,
                "lat" to latE,
                "lon" to lonE,
                "estado" to true
                )

        if (!txtFechaEvento.text.trim().equals("") && !txtHoraEvento.text.trim().equals("") && !txtNombreEvento.text.trim().equals("")) {
            db.collection("evento").add(evento).addOnSuccessListener {
                Toast.makeText(this, "Evento Añadido Correctamente", Toast.LENGTH_SHORT).show()

                volver()
                // Salir de la ventana evento
            }
            .addOnFailureListener {
                Toast.makeText(this, "Problema al registrar Evento", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun volver(){

        val intent = Intent()
        intent.putExtra("eventoCreado", true)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}