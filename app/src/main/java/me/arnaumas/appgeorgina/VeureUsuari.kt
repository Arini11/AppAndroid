package me.arnaumas.appgeorgina

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.veure_usuari.*

class VeureUsuari : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.veure_usuari)

        setup()
    }

    private fun setup() {
        title = "Perfil"


        val database = Firebase.database("https://login-43efd-default-rtdb.europe-west1.firebasedatabase.app")
        val uid = intent.getStringExtra("uid").toString()
        val myRef = database.getReference("usuaris")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (element in dataSnapshot.children) {
                    if(element.key.toString().equals(uid)) {
                        val nom = element.child("nom").getValue(String::class.java)
                        var cognom = element.child("cognom").getValue(String::class.java)
                        var sexe = element.child("sexe").getValue(String::class.java)
                        var edat = element.child("edat").getValue(String::class.java)

                        if(sexe.equals("home") || sexe.equals("Home")){
                            posarColorsHome()
                            ivAvatar.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.avatar_home))
                        } else {
                            ivAvatar.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.avatar_dona))

                        }
                        tvNomCognoms.text = nom + " " + cognom
                        //tvSexe.text = sexe
                        tvEdat.text = edat
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        myRef.addListenerForSingleValueEvent(postListener)

    }

    private fun posarColorsHome() {
        val masculi1 = ContextCompat.getColor(applicationContext, R.color.masculi1)
        val masculi2 = ContextCompat.getColor(applicationContext, R.color.masculi2)
        val clVureUsuari: ConstraintLayout = findViewById(R.id.clVureUsuari)
        val tvNomCognoms: TextView = findViewById(R.id.tvNomCognoms)
        val tvEdat: TextView = findViewById(R.id.tvEdat)
        //val tvSexe: TextView = findViewById(R.id.tvSexe)
        clVureUsuari.setBackgroundColor(masculi1)
        tvNomCognoms.setTextColor(masculi2)
        tvEdat.setTextColor(masculi2)
        //tvSexe.setTextColor(masculi2)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("ONBACK","finish")
        finish()
    }

}