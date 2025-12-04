package com.example.projectmars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.projectmars.R

class Formulir : AppCompatActivity() {

    private lateinit var inputNama: EditText
    private lateinit var inputAlamat: EditText
    private lateinit var inputHp: EditText
    private lateinit var spinnerAgama: Spinner
    private lateinit var radioGroupJk: RadioGroup
    private lateinit var checkboxMembaca: CheckBox
    private lateinit var checkboxMerajut: CheckBox
    private lateinit var checkboxOlahraga: CheckBox
    private lateinit var checkboxMelukis: CheckBox
    private lateinit var cardSubmit: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulir)

        // Inisialisasi komponen
        initViews()

        // Setup click listener untuk submit
        cardSubmit.setOnClickListener {
            submitForm()
        }
    }

    private fun initViews() {
        inputNama = findViewById(R.id.input_nama)
        inputAlamat = findViewById(R.id.input_alamat)
        inputHp = findViewById(R.id.input_hp)
        spinnerAgama = findViewById(R.id.spinner_agama)
        radioGroupJk = findViewById(R.id.radio_group_jk)
        checkboxMembaca = findViewById(R.id.checkbox_membaca)
        checkboxMerajut = findViewById(R.id.checkbox_merajut)
        checkboxOlahraga = findViewById(R.id.checkbox_olahraga)
        checkboxMelukis = findViewById(R.id.checkbox_melukis)
        cardSubmit = findViewById(R.id.card_button_submit)
    }

    private fun submitForm() {
        // Validasi form
        if (!validateForm()) {
            return
        }

        // Ambil data dari form
        val nama = inputNama.text.toString()
        val alamat = inputAlamat.text.toString()
        val hp = inputHp.text.toString()
        val agama = spinnerAgama.selectedItem.toString()
        val jenisKelamin = getSelectedJenisKelamin()
        val hobi = getSelectedHobi()

        // Kirim data ke activity hasil
        val intent = Intent(this, hasil_formulir::class.java).apply {
            putExtra("NAMA", nama)
            putExtra("ALAMAT", alamat)
            putExtra("HP", hp)
            putExtra("AGAMA", agama)
            putExtra("JENIS_KELAMIN", jenisKelamin)
            putExtra("HOBI", hobi)
        }
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        if (inputNama.text.toString().trim().isEmpty()) {
            showToast("Nama harus diisi")
            return false
        }
        if (inputAlamat.text.toString().trim().isEmpty()) {
            showToast("Alamat harus diisi")
            return false
        }
        if (inputHp.text.toString().trim().isEmpty()) {
            showToast("Nomor HP harus diisi")
            return false
        }
        if (radioGroupJk.checkedRadioButtonId == -1) {
            showToast("Pilih jenis kelamin")
            return false
        }
        return true
    }

    private fun getSelectedJenisKelamin(): String {
        return when (radioGroupJk.checkedRadioButtonId) {
            R.id.radio_laki -> "Laki-Laki"
            R.id.radio_perempuan -> "Perempuan"
            else -> "-"
        }
    }

    private fun getSelectedHobi(): String {
        val hobiList = mutableListOf<String>()

        if (checkboxMembaca.isChecked) hobiList.add("Membaca")
        if (checkboxMerajut.isChecked) hobiList.add("Merajut")
        if (checkboxOlahraga.isChecked) hobiList.add("Olahraga")
        if (checkboxMelukis.isChecked) hobiList.add("Melukis")

        return if (hobiList.isEmpty()) "Tidak ada hobi" else hobiList.joinToString(", ")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}