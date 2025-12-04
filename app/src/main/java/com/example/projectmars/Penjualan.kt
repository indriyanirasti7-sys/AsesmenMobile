package com.example.projectmars

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Penjualan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        // Nama item dan harga
        val itemNames = arrayListOf("Sushi", "Pizza", "Nasi Goreng", "Croisant", "Ramen")
        val itemPrices = arrayListOf(50000, 45000, 60000, 55000, 70000)

        // EditText tiap item
        val qtyEditTexts = listOf(
            findViewById<EditText>(R.id.et_qty_1),
            findViewById<EditText>(R.id.et_qty_2),
            findViewById<EditText>(R.id.et_qty_3),
            findViewById<EditText>(R.id.et_qty_4),
            findViewById<EditText>(R.id.et_qty_5)
        )

        // TextView total
        val tvTotalPrice = findViewById<TextView>(R.id.tv_total_price)

        // Fungsi update total
        fun updateTotal() {
            var total = 0
            for (i in qtyEditTexts.indices) {
                val qty = qtyEditTexts[i].text.toString().toIntOrNull() ?: 0
                total += qty * itemPrices[i]
            }
            tvTotalPrice.text = "Rp $total"
        }

        // Pasang TextWatcher untuk setiap EditText agar total otomatis update
        qtyEditTexts.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { updateTotal() }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // Tombol Checkout
        val btnCheckout = findViewById<Button>(R.id.btn_checkout)
        btnCheckout.setOnClickListener {
            // Ambil jumlah
            val itemQtys = qtyEditTexts.map { it.text.toString().toIntOrNull() ?: 0 }

            // Hanya kirim item yang jumlah > 0
            val filteredNames = arrayListOf<String>()
            val filteredPrices = arrayListOf<Int>()
            val filteredQtys = arrayListOf<Int>()

            for (i in itemNames.indices) {
                if (itemQtys[i] > 0) {
                    filteredNames.add(itemNames[i])
                    filteredPrices.add(itemPrices[i])
                    filteredQtys.add(itemQtys[i])
                }
            }

            val customerName = "Customer"
            val tableNumber = "Meja 5"

            // Intent ke transaksi
            val intent = Intent(this, transaksi::class.java)
            intent.putExtra("customer", customerName)
            intent.putExtra("table", tableNumber)
            intent.putStringArrayListExtra("itemNames", filteredNames)
            intent.putIntegerArrayListExtra("itemPrices", filteredPrices)
            intent.putIntegerArrayListExtra("itemQtys", filteredQtys)
            startActivity(intent)
        }

        // Inisialisasi total awal
        updateTotal()
    }
}
