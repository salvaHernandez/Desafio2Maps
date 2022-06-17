package com.example.desafio2salva.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.desafio2salva.Activity.MainActivity.Companion.db
import com.example.desafio2salva.Model.ProviderType
import com.example.desafio2salva.R
import com.example.desafio2salva.picker.DatePickerFragment
import com.example.desafio2salva.picker.TimePickerFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_evento.*
import kotlinx.android.synthetic.main.activity_main.*

class EventoActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private lateinit var map: GoogleMap
    private var latE: Double = 38.970923405116544
    private var lonE: Double = -3.9315805626847045

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)
        supportActionBar?.hide()
//        createMapFragment()
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
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coor, 16f), 3000,null)
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

/*
    /**
     * Esta función nos devolverá un objeto GoogleMap que será muy útil, es por ello que debemos guardarlo en una variable. ¿Cómo lo
     * haremos? Muy sencillo, crearemos una variable en la parte superior de la clase y le asignaremos el objeto GoogleMap cuando lo
     * recibamos.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //Se pueden seleccionar varios tiops de mapas:
        //  None --> no muestra nada, solo los marcadores. (MAP_TYPE_NONE)
        //  Normal --> El mapa por defecto. (MAP_TYPE_NORMAL)
        //  Satélite --> Mapa por satélite.  (MAP_TYPE_SATELLITE)
        //  Híbrido --> Mapa híbrido entre Normal y Satélite. (MAP_TYPE_HYBRID) Muestra satélite y mapas de carretera, ríos, pueblos, etc... asociados.
        //  Terreno --> Mapa de terrenos con datos topográficos. (MAP_TYPE_TERRAIN)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setOnPoiClickListener(this)
        map.setOnMapLongClickListener (this)
        map.setOnMarkerClickListener(this)
//        enableMyLocation() //--> Hanilita, pidiendo permisos, la localización actual.
        createMarker() //--> Nos coloca varios marcadores en el mapa y nos coloca en el CIFP Virgen de Gracia con un Zoom.
        //irubicacioActual() //--> Nos coloca en la ubicación actual directamente. Comenta createMarker par ver esto.
        pintarRutaAlCentro()
        pintarCirculoCentro()
    }

    /**
     * Nos coloca en la ubicación actual.
     */
    @SuppressLint("MissingPermission")
    private fun irubicacioActual() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val miUbicacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latLng = LatLng(miUbicacion!!.latitude, miUbicacion.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f)) //--> Mueve la cámara a esa posición, sin efecto. El valor real indica el nivel de Zoom, de menos a más.
    }

    /**
     * Método en el que crearemos algunos marcadores de ejemplo.
     */
    private fun createMarker() {
        val markerCIFP = LatLng(38.69332,-4.10860)
        /*
        Los markers se crean de una forma muy sencilla, basta con crear una instancia de un objeto LatLng() que recibirá dos
        parámetros, la latitud y la longitud. Yo en este ejemplo he puesto las coordenadas de mi playa favorita.
        */
        //map.addMarker(MarkerOptions().position(markerCIFP).title("Mi CIFP favorito!"))
        //Si queremos cambiar el color del icono, en este caso azul cyan, con un subtexto.
        map.addMarker(
            MarkerOptions().position(markerCIFP).title("Mi CIFP favorito!").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).snippet("CIFP Virgen de Gracia"))


        val paris = LatLng(48.86028, 2.29496)
        //map.addMarker(MarkerOptions().position(paris).title("Paris").icon(sizeIcon(R.drawable.paris)))
        /*
        Otros atributos útiles:
            alpha(0.4f) --> Con un valor real indica la semitransparencia del icono.
            draggable(true)  --> Permite arrastralo.
            snippet("Otro texto") --> Añade un subtexto al título.
         */
        map.addMarker(MarkerOptions().position(paris).title("Paris").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).alpha(0.8f).draggable(true))


        /*
        La función animateCamera() recibirá tres parámetros:

            Un CameraUpdateFactory que a su vez llevará otros dos parámetros, el primero las coordenadas donde queremos hacer zoom
                y el segundo valor (es un float) será la cantidad de zoom que queremos hacer en dichas coordenadas.
            La duración de la animación en milisegundos, por ejemplo 4000 milisegundos son 4 segundos.
            Un listener que no vamos a utilizar, simplemente añadimos null.
         */
        //------------ Zoom hacia un marcador ------------
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(markerCIFP, 18f),
            4000,
            null
        )

        //Esto la mueve sin efecto zoom.
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerCIFP, 18f))
    }

    /**
     * Con este método vamos a ajustar el tamaño de todos los iconos que usemos en los marcadores.
     */
    fun sizeIcon(idImage:Int): BitmapDescriptor {
        val altura = 60
        val anchura = 60

        var draw = ContextCompat.getDrawable(this,idImage) as BitmapDrawable
        val bitmap = draw.bitmap  //Aquí tenemos la imagen.

        //Le cambiamos el tamaño:
        val smallBitmap = Bitmap.createScaledBitmap(bitmap, anchura, altura, false)
        return BitmapDescriptorFactory.fromBitmap(smallBitmap)

    }


    /**
     * Asociamos el fragmento al mapa y lo invocamos para que se cargue de forma asíoncrona.
     * Cuando se cargue se lanzará el método: onMapReady
     */
    fun createMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
        /*
        getMapAsync(this) necesita que nuestra activity implemente la función onMapReady() y para ello tenemos que añadir la interfaz
        OnMapReadyCallback.
         */
    }


    /**
     * función que usaremos a lo largo de nuestra app para comprobar si el permiso ha sido aceptado o no.

    fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    /**
     * función que primero compruebe si el mapa ha sido inicializado, si no es así saldrá de la función gracias
     * a la palabra return, si por el contrario map ya ha sido inicializada, es decir que el mapa ya ha cargado,
     * pues comprobaremos los permisos.
     */
    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }
*/


    /**
     * Método que solicita los permisos.
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }


    /**
     * Se dispara cuando pulsamos la diana que nos centra en el mapa (punto negro, arriba a la derecha).
     */
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Se dispara cuando pulsamos en nuestra localización exacta donde estámos ahora (punto azul).
     */
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }


    /**
     * Dibuja una línea recta desde nuestra ubicación actual al CIFP Virgen de Gracia.
     */
    @SuppressLint("MissingPermission")
    fun pintarRutaAlCentro(){
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val miUbicacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latLng = LatLng(miUbicacion!!.latitude, miUbicacion.longitude)
        val markerCIFP = LatLng(38.69332,-4.10860)

        map.addPolyline(PolylineOptions().run{
            add(latLng, markerCIFP)
            color(Color.BLUE)
            width(9f)
        })

        val loc1 = Location("")
        loc1.latitude = latLng.latitude
        loc1.longitude = latLng.longitude
        val loc2 = Location("")
        loc2.latitude = markerCIFP.latitude
        loc2.longitude = markerCIFP.longitude
        val distanceInMeters = loc1.distanceTo(loc2)
        Log.e("Fernando", distanceInMeters.toString())
    }

    /**
     * Dibuja una línea recta desde nuestra ubicación actual al CIFP Virgen de Gracia.
     */
    fun pintarCirculoCentro(){
        val markerCIFP = LatLng(38.69332,-4.10860)

        map.addCircle(CircleOptions().run{
            center(markerCIFP)
            radius(9.0)
            strokeColor(Color.BLUE)
            fillColor(Color.GREEN)
        })
    }


    /**
     * Con el parámetro podremos obtener información del punto de interés. Este evento se lanza cuando pulsamos en un POI.
     */
    override fun onPoiClick(p0: PointOfInterest) {
        Toast.makeText(this@EventoActivity, "Pulsado.", Toast.LENGTH_LONG).show()
        val dialogBuilder = AlertDialog.Builder(this@EventoActivity)
        dialogBuilder.run {
            setTitle("Información del lugar.")
            setMessage("Id: " + p0!!.placeId + "\nNombre: " + p0!!.name + "\nLatitud: " + p0!!.latLng.latitude.toString() + " \nLongitud: " + p0.latLng.longitude.toString())
            setPositiveButton("Aceptar"){ dialog: DialogInterface, i:Int ->
                Toast.makeText(this@EventoActivity, "Salir", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.create().show()
    }


    /**
     * Con el parámetro crearemos un marcador nuevo. Este evento se lanzará al hacer un long click en alguna parte del mapa.
     */
    override fun onMapLongClick(p0: LatLng) {
        map.addMarker(MarkerOptions().position(p0!!).title("Nuevo marcador"))
    }


    /**
     * Este evento se lanza cuando hacemos click en un marcador.
     */
    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(this, "Estás en ${p0!!.title}, ${p0!!.position}", Toast.LENGTH_SHORT).show()
        return true;

    }

 */

}