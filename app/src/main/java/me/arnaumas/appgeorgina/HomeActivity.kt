package me.arnaumas.appgeorgina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType {
    BASIC
}

private lateinit var dbUsuaris: DatabaseReference

class HomeActivity : AppCompatActivity() {

    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Setup
        var bundle = intent.extras
        email = bundle?.getString("email")
        //val provider = bundle?.getString("provider")
        setup()

    }

    private fun setup() {
        title = "Inici"

        btnXats.setOnClickListener {
            val intent = Intent(this, VeureXats::class.java)
            intent.putExtra("user", email)
            startActivity(intent)
        }

        btnActualitzarUbicacio.setOnClickListener {
            val intent = Intent(this, ActualitzarUbicacio::class.java)
            // centre Catalunya
            intent.putExtra("lat", "41.261163")
            intent.putExtra("lon", "1.170191")
            startActivity(intent)
        }

        btnVeureMapa.setOnClickListener {
            val intent = Intent(this, VeureMapaHome::class.java)
            startActivity(intent)
        }

        btnTancarSessio.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        //val uid = FirebaseAuth.getInstance().currentUser?.uid
        //tvUID.text = uid

        /*
        btnActualitzar.setOnClickListener {
            val dbRef = Firebase.database("https://login-43efd-default-rtdb.europe-west1.firebasedatabase.app").getReference("usuaris")

            if(uid != null && etCodiPostal.text.isNotEmpty()) {
                val u = Usuari()
                u.poble = etCodiPostal.text.toString()

                // Obtenir latitud i longitud a partir del codi postal
                dbRef.child(uid).setValue(u)
            }
        }
         */



    }

}