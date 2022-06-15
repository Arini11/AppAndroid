package me.arnaumas.appgeorgina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


class VeureMapaHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veure_mapa_home)

        supportFragmentManager.beginTransaction().add(R.id.vistaContingut, VeureMapa() as Fragment)
            .commit()

    }
}

