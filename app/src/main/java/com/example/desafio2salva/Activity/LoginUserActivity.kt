package com.example.desafio2salva.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2salva.Activity.LoginUserActivity.Companion.conUser
import com.example.desafio2salva.Activity.LoginUserActivity.Companion.mGoogleApiClient
import com.example.desafio2salva.Activity.MainActivity.Companion.REQUEST_CODE_LOCATION
import com.example.desafio2salva.Adapter.RecyclerEvento
import com.example.desafio2salva.Model.Usuario
import com.example.desafio2salva.R
import com.example.desafio2salva.Utils.Auxiliar.getEventos
import com.example.desafio2salva.Utils.Auxiliar.listaEventos
import com.example.desafio2salva.Utils.Auxiliar.miAdapterEvento
import com.example.desafio2salva.Utils.Auxiliar.modificaUsuario
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login_user.*

class LoginUserActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks {
    companion object {
        lateinit var conUser: AppCompatActivity
        lateinit var mGoogleApiClient : GoogleApiClient
    }
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        supportActionBar?.hide()

        conUser=this

        var us: Usuario=intent.getSerializableExtra("user") as Usuario

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (us.verificado) {
            getEventos(false)
        } else {
            Toast.makeText(this, R.string.smsUsuarioSinActivar, Toast.LENGTH_LONG).show()
        }

        recyclerEventos.setHasFixedSize(true)
        recyclerEventos.layoutManager=LinearLayoutManager(this)
        miAdapterEvento=RecyclerEvento(this, listaEventos, us.email)
        recyclerEventos.adapter=miAdapterEvento

        recyclerEventos.isEnabled=us.verificado


        mGoogleApiClient=GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()

        enableLocation()


    }


    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()

    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient.disconnect()

    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(conUser, "Solo queda saber como coger mis coordenadas", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(conUser, R.string.smsSinPermisoLocalizacion, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        mGoogleApiClient.connect()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.disconnect()
    }


}

    // Manda la ubicación
    // ************************************************************
    // ************************************************************
    // ************************************************************
    // ************************************************************
    fun isLocationPermissionGranted () = ContextCompat.checkSelfPermission(
        conUser, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED



    private fun requestLocationPermission () {
        if (ActivityCompat.shouldShowRequestPermissionRationale(conUser, Manifest. permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(conUser, R.string.smsSinPermisoLocalizacion, Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(conUser, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    private fun enableLocation () {
        if (isLocationPermissionGranted()) {
            getLocalizacion()
        } else {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocalizacion() {
        var mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mLastLocation != null) {
            Toast.makeText(conUser, mLastLocation.longitude.toString()+" "+ mLastLocation.latitude.toString(), Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(conUser, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
        }
    }
