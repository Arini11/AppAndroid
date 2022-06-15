package me.arnaumas.appgeorgina

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ActualitzarUbicacio : FragmentActivity(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private var mMap: GoogleMap? = null
    private val PERMIS_LOCALITZACIO_PRECISA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_actualitzar_ubicacio)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val pos = LatLng(intent.getStringExtra("lat")!!.toDouble(), intent.getStringExtra("lon")!!.toDouble())
        //afegirMarcador(pos, intent.getStringExtra("id").toString())

        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        val camPos = CameraPosition.Builder()
            .target(pos)
            .zoom(17f)
            .bearing(0f)
            .tilt(0f)
            .build()

        val camUpdate = CameraUpdateFactory.newCameraPosition(camPos)
        mMap!!.animateCamera(camUpdate)

        mMap!!.setOnMapClickListener(this)
        mMap!!.setOnMapLongClickListener(this)
        habilitaLocalitzacio()
    }

    private fun afegirMarcador(latitudLongitud: LatLng, titol: String) {
        mMap!!.addMarker(
            MarkerOptions().position(latitudLongitud).title(titol)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
    }

    fun habilitaLocalitzacio() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            mMap!!.isMyLocationEnabled = true
        } else {
            // Demanem a l'usuari que ens doni permís per localitzar-se a ell mateix
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMIS_LOCALITZACIO_PRECISA
            )
        }
    }

    override fun onMapClick(latLng: LatLng) {
    }

    override fun onMapLongClick(latLng: LatLng) {
        // Actualitzar ubicació firebase

        //popup per confirmar canvi d'ubicació
        popup(latLng)


    }

    private fun popup(latLng: LatLng) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("ACTUALITZAR UBICACIÓ")
        //set message for alert dialog
        builder.setMessage("Estàs segur que vols actualitzar la teva ubicació?")
        builder.setIcon(android.R.drawable.ic_menu_mylocation)

        //performing positive action
        builder.setPositiveButton("Sí"){dialogInterface, which ->

            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
            //uid usuari actual
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            //ref firebase usuaris
            val dbRef = Firebase.database("https://login-43efd-default-rtdb.europe-west1.firebasedatabase.app").getReference("usuaris")

            //actualitzar dades firebase
            dbRef.child(uid.toString()).child("lat").setValue(latLng.latitude)
            dbRef.child(uid.toString()).child("lon").setValue(latLng.longitude)
            dbRef.child(uid.toString()).child("nom").setValue("")
            dbRef.child(uid.toString()).child("cognom").setValue("")
            dbRef.child(uid.toString()).child("edat").setValue("")
            dbRef.child(uid.toString()).child("sexe").setValue("")

            //notificar usuari
            Toast.makeText(this,
                "Ubicació Actualitzada!",
                Toast.LENGTH_LONG).show();

            //sortir de l'intent per evitar que es faci spam del canvi d'ubicació
            this.finish()


        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}