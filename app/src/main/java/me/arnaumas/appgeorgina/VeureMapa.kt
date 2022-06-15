package me.arnaumas.appgeorgina

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap

class VeureMapa : Fragment() {

    private var mMap: GoogleMap? = null
    private val PERMIS_LOCALITZACIO_PRECISA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_actualitzar_ubicacio)
        /*
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        carregarMarcadors()
        */
    }


}
