package com.example.desafio2salva.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.desafio2salva.Model.ProviderType
import com.example.desafio2salva.Model.Usuario
import com.example.desafio2salva.R
import com.example.desafio2salva.Model.UsuarioGuardado.Companion.user
import com.example.desafio2salva.Utils.Auxiliar.listaEventos
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
companion object {
    val db = FirebaseFirestore.getInstance()
    val REQUEST_CODE_LOCATION = 0

}

    //Atributos necesarios para el login con Google.
    private var lat = 0.0
    private var lon = 0.0
    private var RC_SIGN_IN = 1
    private var usuario = Usuario ("",0.0,0.0,false,false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar?.hide()
        listaEventos = arrayListOf()

        //sessionIniciada()

        //Con esto lanzamos eventos personalizados a GoogleAnalytics que podemos ver en nuestra consola de FireBase.
        val analy: FirebaseAnalytics= FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Integración completada")
        analy.logEvent("InitScreen",bundle)



    }



    fun Registrar (view: View) {

        if (txtPass.text.isNotEmpty() && txtUser.text.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtUser.text.toString(),txtPass.text.toString()).addOnCompleteListener {
                if (it.isSuccessful){
                    if (checkSesionIni.isChecked) {
                        session(false, false, false)
                    }
                    db.collection("users").document(txtUser.text.toString()).set(
                        hashMapOf(
                            "pass" to txtPass.text.toString(),
                            "verificado" to false,
                            "admin" to false,
                            "lat" to lat,
                            "lon" to lon
                        )
                    )

                    irLogin(it.result?.user?.email?:"", ProviderType.BASIC, false, false)  //Esto de los interrogantes es por si está vacío el email.
                } else {
                    showAlert()
                }
            }
        }
    }


    fun Login (view:View) {
        if (txtUser.text.isNotEmpty() && txtPass.text.isNotEmpty()) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtUser.text.toString(), txtPass.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful) {
                        db.collection("users").document(txtUser.text.toString()).get().addOnSuccessListener {

                            var verificado = it.get("verificado") as Boolean
                            var admin = it.get("admin") as Boolean

                        if (checkSesionIni.isChecked) {
                            session(admin, verificado, false)
                        }
                        irLogin(txtUser.text.toString(), ProviderType.BASIC, admin, verificado)  //Esto de los interrogantes es por si está vacío el email.
                        }
                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Algo ha fallado en el login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun LoginGoogle (view: View) {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.request_id_token)) //Esto se encuentra en el archivo google-services.json: client->oauth_client -> client_id
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this,googleConf) //Este será el cliente de autenticación de Google.
        googleClient.signOut() //Con esto salimos de la posible cuenta  de Google que se encuentre logueada.
        val signInIntent = googleClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Si la respuesta de esta activity se corresponde con la inicializada es que viene de la autenticación de Google.
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                if (account != null) {
                    val credential: AuthCredential= GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            db.collection("users").document(account.email.toString()).get().addOnSuccessListener {
                                try {
                                    var admin: Boolean=it.get("admin") as Boolean
                                    var verificado: Boolean=it.get("verificado") as Boolean
                                    irLogin(account.email ?: "", ProviderType.GOOGLE, admin, verificado)  //Esto de los interrogantes es por si está vacío el email.
                                } catch (e: Exception) {
                                    Toast.makeText(this, "crear cuenta", Toast.LENGTH_SHORT).show()
                                    db.collection("users").document(account.email.toString()).set(
                                        hashMapOf(
                                            "pass" to txtPass.text.toString(),
                                            "verificado" to false,
                                            "admin" to false,
                                            "lat" to lat,
                                            "lon" to lon
                                        )
                                    )
                                    irLogin(account.email ?: "", ProviderType.GOOGLE, false, false)
                                }
                            }
                        } else showAlert()
                    }
                }
            } catch (e: ApiException) {
                Log.w("Salva", "Google sign in failed", e)
                showAlert()
            }
        }
    }


    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuairo")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog= builder.create()
        dialog.show()
    }


    //*********************************************************************************
    private fun irLogin(email:String, provider: ProviderType, isAdmin : Boolean, verificado : Boolean){

        txtUser.text.clear()
        txtPass.text.clear()

        usuario.verificado = verificado
        usuario.admin = isAdmin
        usuario.email = email
        val intent : Intent

        if (isAdmin) {
             intent = Intent(this, LoginAdminActivity::class.java)
        } else {
             intent = Intent(this, LoginUserActivity::class.java)
        }
        intent.putExtra("user", usuario)
        startActivity(intent)
    }



    // provider si es false se guarda como BASIC
    private fun session(isAdmin: Boolean, verificado: Boolean, provider: Boolean) {
            user.saveCorreo(txtUser.text.toString())
            user.savePwd(txtPass.text.toString())
            user.saveIsAdmin(isAdmin)
            user.saveVerificado(verificado)
            user.saveProvider(provider)
            user.saveRecordar(checkSesionIni.isChecked)
    }

    private fun sessionIniciada () {
        if (user.getRecor()) {
            db.collection("users").document(user.getCorreo()).get().addOnSuccessListener {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getCorreo(), user.getPwd()).addOnCompleteListener {
                    if (user.getProvider()) {

                        irLogin(user.getCorreo(), ProviderType.BASIC, user.getIsAdmin(), user.getVerificado())

                    } else irLogin(user.getCorreo(), ProviderType.GOOGLE, user.getIsAdmin(), user.getVerificado())
                }
            }
        }
    }


}