package com.example.projectmars

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class transaksi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        // Views
        val tvCustomerName = findViewById<TextView>(R.id.tv_customer_name)
        val tvTableNumber = findViewById<TextView>(R.id.tv_table_number)
        val llOrderItems = findViewById<LinearLayout>(R.id.ll_order_items)
        val templateRow = findViewById<LinearLayout>(R.id.template_row)
        val tvTotal = findViewById<TextView>(R.id.tv_total)

        // Ambil data dari Intent
        val customerName = intent.getStringExtra("customer") ?: "-"
        val tableNumber = intent.getStringExtra("table") ?: "-"
        val itemNames = intent.getStringArrayListExtra("itemNames") ?: arrayListOf()
        val itemPrices = intent.getIntegerArrayListExtra("itemPrices") ?: arrayListOf()
        val itemQtys = intent.getIntegerArrayListExtra("itemQtys") ?: arrayListOf()

        // Set customer info
        tvCustomerName.text = "Nama: $customerName"
        tvTableNumber.text = "Meja: $tableNumber"

        // Hitung total keseluruhan
        var totalAll = 0

        for (i in itemNames.indices) {
            val qty = itemQtys[i]
            if (qty > 0) {
                // Hitung subtotal
                val subtotal = qty * itemPrices[i]
                totalAll += subtotal

                // Buat row baru dari template
                val row = LinearLayout(this)
                row.orientation = LinearLayout.HORIZONTAL
                row.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                row.setPadding(8, 8, 8, 8)

                // Item name
                val tvName = TextView(this)
                tvName.text = itemNames[i]
                tvName.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)

                // Quantity
                val tvQty = TextView(this)
                tvQty.text = qty.toString()
                tvQty.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                tvQty.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

                // Harga
                val tvPrice = TextView(this)
                tvPrice.text = "Rp ${itemPrices[i]}"
                tvPrice.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                tvPrice.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END

                // Subtotal
                val tvSubtotal = TextView(this)
                tvSubtotal.text = "Rp $subtotal"
                tvSubtotal.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                tvSubtotal.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END

                // Tambahkan ke row
                row.addView(tvName)
                row.addView(tvQty)
                row.addView(tvPrice)
                row.addView(tvSubtotal)

                // Tambahkan row ke container
                llOrderItems.addView(row)
            }
        }

        // Tampilkan total keseluruhan
        tvTotal.text = "Total: Rp $totalAll"

        // Tombol kembali
        findViewById<TextView>(R.id.btn_back_home)?.setOnClickListener {
            finish()
        }
    }
}
