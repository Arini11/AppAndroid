package me.arnaumas.appgeorgina

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VeureMapa : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerClickListener {

    private var mMap: GoogleMap? = null
    private val PERMIS_LOCALITZACIO_PRECISA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_actualitzar_ubicacio)
        /*val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
*/
        //carregarMarcadors()

    }

    private fun carregarMarcadors() {
        // Veure totes les ubicacions dels usuaris

        //ref firebase usuaris
        val mDatabase = Firebase.database("https://login-43efd-default-rtdb.europe-west1.firebasedatabase.app").getReference()
        mDatabase.child("usuaris").get()
            .addOnSuccessListener {
                for (element in it.children) {
                    var key = element.key
                    val lat = element.child("lat").getValue(Double::class.java)
                    val lon = element.child("lon").getValue(Double::class.java)
                    var nom = element.child("nom").getValue(String::class.java)
                    var cognom = element.child("cognom").getValue(String::class.java)
                    var sexe = element.child("sexe").getValue(String::class.java)
                    var edat = element.child("edat").getValue(String::class.java)

                    afegirMarcador(
                        LatLng(lat!!, lon!!),
                        nom + " " + cognom,
                        sexe.toString(),
                        edat.toString(),
                        key
                    )
                }
            }
            .addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID

        val catalunya = LatLng(41.7, 1.6)

        val camPos = CameraPosition.Builder()
            .target(catalunya)
            .zoom(8f)
            .bearing(0f)
            .tilt(0f)
            .build()

        val camUpdate = CameraUpdateFactory.newCameraPosition(camPos)
        mMap!!.animateCamera(camUpdate)
        mMap!!.setOnMapClickListener(this)
        mMap!!.setOnMapLongClickListener(this)
        mMap!!.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
        mMap!!.setOnMarkerClickListener(this)

        //habilitaLocalitzacio()
    }

    private fun afegirMarcador(
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
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            mMap!!.isMyLocationEnabled = true
        } else {
            // Demanem a l'usuari que ens doni permís per localitzar-se a ell mateix
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMIS_LOCALITZACIO_PRECISA
            )
        }
    }

    override fun onMarkerClick(marcador: Marker): Boolean {
        Toast.makeText(
            activity!!,
            "${marcador.tag}",
            Toast.LENGTH_SHORT
        ).show()

        //VeureUsuari
        val intent = Intent(activity!!, VeureUsuari::class.java)
        intent.putExtra("uid", marcador.tag.toString())
        startActivity(intent)

        return false
    }

    override fun onMapClick(latLng: LatLng) {
    }

    override fun onMapLongClick(latLng: LatLng) {
    }

}
