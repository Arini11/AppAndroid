package me.arnaumas.appgeorgina

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_veure_mapa_home.*
import kotlinx.android.synthetic.main.fragment_actualitzar_ubicacio.*
import kotlinx.android.synthetic.main.fragment_actualitzar_ubicacio.view.*
import java.lang.Exception

class VeureMapa() : Fragment() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var mapaManager: MapaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDatabase = Firebase.database("https://login-43efd-default-rtdb.europe-west1.firebasedatabase.app").getReference()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_actualitzar_ubicacio, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapaManager = MapaManager(requireContext(), mDatabase)
        mapFragment!!.getMapAsync(mapaManager)

        carregarMarcadors()

        return view
    }

    private fun carregarMarcadors() {
        // Veure totes les ubicacions dels usuaris

        //ref firebase usuaris
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

                    mapaManager.afegirMarcador(
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

}
