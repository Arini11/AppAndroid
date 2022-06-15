package me.arnaumas.appgeorgina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup
        setup()


    }

    private fun setup() {
        title = "Autenticació"
        etCorreu.setText("asd@gmail.com")
        etContrasenya.setText("arnaumas")

        btnRegistrar.setOnClickListener {
            if (!etCorreu.text.isEmpty() && !etContrasenya.text.isEmpty()) {
                Log.d("email", etCorreu.text.toString())
                Log.d("passwd", etContrasenya.text.toString())
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    etCorreu.text.toString(),
                    etContrasenya.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Log.w("EMAIL_PASSWORD", "signInWithEmail:failure", it.exception)
                        showAlert()
                    }
                }
            }
        }

        btnLogin.setOnClickListener {
            if (!etCorreu.text.isEmpty() && !etContrasenya.text.isEmpty()) {
                try {
                    Firebase.auth.signInWithEmailAndPassword(
                        etCorreu.text.toString(),
                        etContrasenya.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("ERROR", e.toString())
                }

            }
        }

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error d'autenticació d'usuari")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider)
        }
        startActivity(homeIntent)
    }
}
