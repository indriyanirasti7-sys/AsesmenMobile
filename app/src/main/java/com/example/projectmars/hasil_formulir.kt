package com.example.projectmars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.projectmars.R

class hasil_formulir : AppCompatActivity() {

    private lateinit var tvHasilNama: TextView
    private lateinit var tvHasilAlamat: TextView
    private lateinit var tvHasilHp: TextView
    private lateinit var tvHasilAgama: TextView
    private lateinit var tvHasilJk: TextView
    private lateinit var tvHasilHobi: TextView
    private lateinit var btnKembali: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_formulir)

        // Inisialisasi komponen
        initViews()

        // Terima data dari intent
        receiveData()

        // Setup button kembali
        btnKembali.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }
    }

    private fun initViews() {
        tvHasilNama = findViewById(R.id.tv_hasil_nama)
        tvHasilAlamat = findViewById(R.id.tv_hasil_alamat)
        tvHasilHp = findViewById(R.id.tv_hasil_hp)
        tvHasilAgama = findViewById(R.id.tv_hasil_agama)
        tvHasilJk = findViewById(R.id.tv_hasil_jk)
        tvHasilHobi = findViewById(R.id.tv_hasil_hobi)
        btnKembali = findViewById(R.id.btn_kembali)
    }

    private fun receiveData() {
        val intent = intent

        // Ambil data dari intent dan set ke TextView
        tvHasilNama.text = intent.getStringExtra("NAMA") ?: "-"
        tvHasilAlamat.text = intent.getStringExtra("ALAMAT") ?: "-"
        tvHasilHp.text = intent.getStringExtra("HP") ?: "-"
        tvHasilAgama.text = intent.getStringExtra("AGAMA") ?: "-"
        tvHasilJk.text = intent.getStringExtra("JENIS_KELAMIN") ?: "-"
        tvHasilHobi.text = intent.getStringExtra("HOBI") ?: "-"
    }
}