package me.arnaumas.appgeorgina

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MapaManager: OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerClickListener {

    private val PERMIS_LOCALITZACIO_PRECISA = 1
    private var mMap: GoogleMap? = null
    private lateinit var catalunya: LatLng
    private lateinit var context: Context
    private lateinit var mDatabase: DatabaseReference

    constructor(context: Context, mDatabase: DatabaseReference){
        this.context = context
        this.mDatabase = mDatabase
    }

    override fun onMapReady(googleMap: GoogleMap) {


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        mMap = googleMap
        mDatabase.child("usuaris").child(uid!!).get().addOnSuccessListener {

            mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID

            val lat = it.child("lat").getValue(Double::class.java)!!
            val lon = it.child("lon").getValue(Double::class.java)!!
            catalunya = LatLng(lat,lon)

            val camPos = CameraPosition.Builder()
                .target(catalunya)
                .zoom(12f)
                .bearing(0f)
                .tilt(0f)
                .build()

            val camUpdate = CameraUpdateFactory.newCameraPosition(camPos)
            mMap!!.animateCamera(camUpdate)
            mMap!!.setOnMapClickListener(this)
            mMap!!.setOnMapLongClickListener(this)
            mMap!!.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(context))
            mMap!!.setOnMarkerClickListener(this)
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        //habilitaLocalitzacio()
    }

    fun afegirMarcador(
        latitudLongitud: LatLng,
        titol: String,
        sexe: String,
        edat: String,
        key: String?
    ) {
        var marcador = mMap!!.addMarker(
            MarkerOptions()
                .position(latitudLongitud)
                .title(titol)
                .snippet(sexe+"\n"+edat+" anys")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        marcador?.tag = key
    }

    fun habilitaLocalitzacio() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            mMap!!.isMyLocationEnabled = true
        } else {
            // Demanem a l'usuari que ens doni permís per localitzar-se a ell mateix
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMIS_LOCALITZACIO_PRECISA
            )
        }
    }

    override fun onMarkerClick(marcador: Marker): Boolean {
        Toast.makeText(
            context,
            "${marcador.tag}",
            Toast.LENGTH_SHORT
        ).show()

        //VeureUsuari
        val intent = Intent(context, VeureUsuari::class.java)
        intent.putExtra("uid", marcador.tag.toString())
        context.startActivity(intent)

        return false
    }

    override fun onMapClick(latLng: LatLng) {
    }

    override fun onMapLongClick(latLng: LatLng) {
    }
}