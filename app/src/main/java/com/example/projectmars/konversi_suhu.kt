package com.example.projectmars // Ganti dengan nama paket Anda

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmars.R
import java.util.Locale

class konversi_suhu : AppCompatActivity() {

    // Deklarasi properti untuk View
    private lateinit var inputAngka1: EditText
    private lateinit var inputAngka2: EditText
    private lateinit var spinnerSuhu1: Spinner
    private lateinit var spinnerSuhu2: Spinner
    private lateinit var btnHitung: Button
    private lateinit var btnHapus: Button
    private lateinit var btnKembali: ImageView

    // Array placeholder untuk Spinner (Asumsi array/suhu)
    private val unitSuhu = arrayOf("Celsius", "Fahrenheit", "Reamur", "Kelvin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Gunakan nama layout yang benar jika Anda telah menyimpannya sebagai konversi_suhu.xml
        setContentView(R.layout.activity_konversi_suhu)

        // 1. Inisialisasi View
        inputAngka1 = findViewById(R.id.inputangka1)
        inputAngka2 = findViewById(R.id.inputangka2)
        spinnerSuhu1 = findViewById(R.id.spinner_suhu1)
        spinnerSuhu2 = findViewById(R.id.spinner_suhu2)
        btnHitung = findViewById(R.id.btnHitung)
        // btnCalculate di XML Anda berisi teks "Hapus", maka kita asumsikan ID-nya adalah btnCalculate
        btnHapus = findViewById(R.id.btnCalculate)
        btnKembali = findViewById(R.id.kembali)

        // 2. Setup Adapter untuk Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitSuhu)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSuhu1.adapter = adapter
        spinnerSuhu2.adapter = adapter

        // Set unit default (misalnya Celsius dan Fahrenheit)
        spinnerSuhu1.setSelection(0)
        spinnerSuhu2.setSelection(1)

        // Atur inputAngka2 sebagai non-editable karena ini adalah kolom hasil
        inputAngka2.isEnabled = false


        // 3. Listener Tombol Kembali
        btnKembali.setOnClickListener {
            // Logika untuk kembali ke Activity sebelumnya
            finish()
        }

        // 4. Listener Tombol Hitung
        btnHitung.setOnClickListener {
            hitungKonversi()
        }

        // 5. Listener Tombol Hapus
        btnHapus.setOnClickListener {
            // Menghapus isi dari kedua EditText
            inputAngka1.text.clear()
            inputAngka2.text.clear()
            // Mengembalikan Spinner ke pilihan default (opsional)
            spinnerSuhu1.setSelection(0)
            spinnerSuhu2.setSelection(1)
            Toast.makeText(this, "Input dibersihkan", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Fungsi yang dipanggil saat tombol Hitung ditekan.
     * Mengambil nilai dari inputAngka1, unit dari kedua Spinner, melakukan konversi,
     * dan menampilkan hasilnya di inputAngka2.
     */
    private fun hitungKonversi() {
        val inputStr = inputAngka1.text.toString()

        // Validasi input
        if (inputStr.isBlank()) {
            Toast.makeText(this, "Mohon masukkan angka suhu", Toast.LENGTH_SHORT).show()
            return
        }

        val value: Double
        try {
            value = inputStr.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Input harus berupa angka yang valid", Toast.LENGTH_SHORT).show()
            return
        }

        val fromUnit = spinnerSuhu1.selectedItem.toString()
        val toUnit = spinnerSuhu2.selectedItem.toString()

        if (fromUnit == toUnit) {
            // Jika unit yang dipilih sama
            inputAngka2.setText(String.format(Locale.US, "%.2f", value))
            return
        }

        // Lakukan konversi
        val result = convertTemperature(value, fromUnit, toUnit)

        // Tampilkan hasil, dibulatkan menjadi 2 desimal
        if (result.isNaN()) {
            inputAngka2.setText("ERROR")
            Toast.makeText(this, "Terjadi kesalahan konversi", Toast.LENGTH_SHORT).show()
        } else {
            // Menggunakan Locale.US untuk memastikan titik desimal (standard pemrograman)
            inputAngka2.setText(String.format(Locale.US, "%.2f", result))
        }
    }


    /**
     * Logika utama untuk konversi suhu.
     * Langkah 1: Konversi unit asal ke Celsius.
     * Langkah 2: Konversi dari Celsius ke unit tujuan.
     */
    private fun convertTemperature(value: Double, fromUnit: String, toUnit: String): Double {
        // 1. Konversi unit asal ke Celsius (base unit)
        val celsiusValue = when (fromUnit) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32) * 5.0 / 9.0
            "Reamur" -> value * 5.0 / 4.0
            "Kelvin" -> value - 273.15
            else -> Double.NaN // Unit tidak dikenal
        }

        // Jika konversi ke Celsius gagal, kembalikan NaN
        if (celsiusValue.isNaN()) return Double.NaN

        // 2. Konversi dari Celsius ke unit tujuan
        return when (toUnit) {
            "Celsius" -> celsiusValue
            "Fahrenheit" -> (celsiusValue * 9.0 / 5.0) + 32.0
            "Reamur" -> celsiusValue * 4.0 / 5.0
            "Kelvin" -> celsiusValue + 273.15
            else -> Double.NaN // Unit tidak dikenal
        }
    }
}