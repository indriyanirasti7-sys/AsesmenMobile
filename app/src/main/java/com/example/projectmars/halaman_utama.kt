package com.example.projectmars

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class halaman_utama : AppCompatActivity() {

    private lateinit var cardFormulir: CardView
    private lateinit var cardKalkulator: CardView
    private lateinit var cardPenjualan: CardView
    private lateinit var cardSuhu: CardView
    private lateinit var cardProfil: CardView
    private lateinit var cardExit: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_halaman_utama)

        init()
        setupClickListeners()
        setupWindowInsets()
    }

    private fun init() {
        try {
            cardFormulir = findViewById(R.id.card_Formulir)
            cardKalkulator = findViewById(R.id.card_Kalkulator)
            cardPenjualan = findViewById(R.id.card_Penjualan)
            cardSuhu = findViewById(R.id.card_Suhu)
            cardProfil = findViewById(R.id.card_Profil)
            cardExit = findViewById(R.id.card_Exit)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: Beberapa view tidak ditemukan", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setupClickListeners() {
        cardFormulir.setOnClickListener {
            Toast.makeText(this@halaman_utama, "CardView Formulir Diklik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@halaman_utama, Formulir::class.java)
            startActivity(intent)
        }

        cardKalkulator.setOnClickListener {
            Toast.makeText(this@halaman_utama, "CardView Kalkulator Diklik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@halaman_utama, kalkulator::class.java)
            startActivity(intent)
        }

        cardPenjualan.setOnClickListener {
            Toast.makeText(this@halaman_utama, "CardView Penjualan Diklik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@halaman_utama, Penjualan::class.java) // PERBAIKAN DI SINI
            startActivity(intent)
        }

        cardSuhu.setOnClickListener {
            Toast.makeText(this@halaman_utama, "CardView Konversi Suhu Diklik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@halaman_utama, konversi_suhu::class.java)
            startActivity(intent)
        }

        cardProfil.setOnClickListener {
            Toast.makeText(this@halaman_utama, "CardView Profil Diklik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@halaman_utama, profil::class.java)
            startActivity(intent)
        }

        cardExit.setOnClickListener {
            Toast.makeText(this@halaman_utama, "CardView Exit Diklik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@halaman_utama, exit::class.java)
            startActivity(intent)
        }
    }

    // Navigasi aman (opsional, tidak digunakan saat ini)
    private fun safeNavigateTo(activityClass: Class<*>, activityName: String) {
        try {
            val intent = Intent(this, activityClass)
            startActivity(intent)
            Toast.makeText(this, "Membuka $activityName", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: $activityName tidak ditemukan", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}