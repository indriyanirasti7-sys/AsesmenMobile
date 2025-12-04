package com.example.projectmars

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class exit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exit)

        // memunculkan dialog saat halaman dibuka
        showExitDialog()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showExitDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_exit, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnYa = dialogView.findViewById<Button>(R.id.ya)
        val btnTidak = dialogView.findViewById<Button>(R.id.tidak)

        // tombol keluar aplikasi
        btnYa.setOnClickListener {
            finishAffinity()
            dialog.dismiss()
        }

        // tombol batal → kembali ke halaman utama
        btnTidak.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, halaman_utama::class.java))
            finish()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}
