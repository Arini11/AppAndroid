package me.arnaumas.appgeorgina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_veure_mapa_home.*


class VeureMapaHome : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veure_mapa_home)

        supportFragmentManager.beginTransaction()
            .add(R.id.vistaContingut, PrimerFragment() as Fragment, "frag")
            .commit()

        btnMapa.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("frag")

            fragment?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .add(R.id.vistaContingut, PrimerFragment() as Fragment, "frag")
                    .commit()
            }
        }

        btnLlista.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("frag")

            fragment?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .add(R.id.vistaContingut, SegonFragment() as Fragment, "frag")
                    .commit()
            }
        }
    }





}


