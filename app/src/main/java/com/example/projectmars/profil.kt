package com.example.projectmars

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class profil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val btnAlamat = findViewById<LinearLayout>(R.id.btnAlamat)
        val btnPendidikan = findViewById<LinearLayout>(R.id.btnPendidikan)
        val btnTelepon = findViewById<LinearLayout>(R.id.btnTelepon)
        val btnInstagram = findViewById<LinearLayout>(R.id.btnInstagram)

        // === TELEPON → WhatsApp ===
        btnTelepon.setOnClickListener {
            val nomor = "6281234567890" // ganti nomor kamu
            val url = "https://wa.me/$nomor"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        // === INSTAGRAM ===
        btnInstagram.setOnClickListener {
            val username = "sunflower_.seeker"
            val uri = Uri.parse("http://instagram.com/_u/$username")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.instagram.android")

            try {
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/$username")))
            }
        }

        // === ALAMAT → Google Maps ===
        btnAlamat.setOnClickListener {
            val alamat = "Jebres Pucangsawit Surakarta"
            val gmmIntentUri = Uri.parse("geo:0,0?q=$alamat")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        // === PENDIDIKAN → Google Maps ===
        btnPendidikan.setOnClickListener {
            val sekolah = "SMK N 6 Surakarta"
            val gmmIntentUri = Uri.parse("geo:0,0?q=$sekolah")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}
