package com.example.projectmars

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.ArithmeticException
import java.lang.NumberFormatException

class kalkulator : AppCompatActivity() {

    private lateinit var tvDisplay1: TextView // Layar utama (input/hasil saat ini)
    private lateinit var tvDisplay2: TextView // Layar riwayat (operasi yang sedang berlangsung)

    private var currentInput = "0"
    private var firstOperand = 0.0 // Operand pertama untuk perhitungan saat ini (atau hasil perhitungan sebelumnya)
    private var secondOperand = 0.0 // Operand kedua
    private var currentOperator = ""
    private var shouldResetInput = false // true jika input berikutnya harus menggantikan currentInput (setelah operator atau =)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kalkulator)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeCalculator()
    }

    private fun initializeCalculator() {
        initializeViews()
        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
    }

    private fun initializeViews() {
        tvDisplay1 = findViewById(R.id.tvDisplay1)
        tvDisplay2 = findViewById(R.id.tvDisplay2)

        // Set initial display
        tvDisplay1.text = currentInput
        tvDisplay2.text = ""
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                onNumberButtonClick(it as Button)
            }
        }
    }

    private fun setupOperatorButtons() {
        findViewById<Button>(R.id.btnTambah).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btnKurang).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btnKali).setOnClickListener { onOperatorClick("×") }
        findViewById<Button>(R.id.btnBagi).setOnClickListener { onOperatorClick("÷") }
        findViewById<Button>(R.id.btnSamaDengan).setOnClickListener { calculateResult() }
    }

    private fun setupFunctionButtons() {
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            clearAll()
        }

        findViewById<Button>(R.id.btnsatu).setOnClickListener { // Asumsi btnsatu adalah Backspace
            backspace()
        }

        findViewById<Button>(R.id.btnTitik).setOnClickListener {
            addDecimalPoint()
        }

        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener {
            toggleSign()
        }

        // Long press pada tombol = untuk checkout
        findViewById<Button>(R.id.btnSamaDengan).setOnLongClickListener {
            sendToCheckout()
            true
        }

        // Long press pada tombol Clear untuk reset total
        findViewById<Button>(R.id.btnClear).setOnLongClickListener {
            resetCalculator()
            true
        }
    }

    private fun onNumberButtonClick(button: Button) {
        val number = button.text.toString()

        if (shouldResetInput) {
            currentInput = "0" // Reset currentInput setelah operator atau =
            shouldResetInput = false
            // Penting: firstOperand sudah di-set di onOperatorClick, jadi tidak perlu diubah di sini
        }

        if (currentInput == "0" && number != ".") {
            currentInput = number
        } else if (currentInput == "-0" && number != ".") { // Mengatasi -0
            currentInput = if (currentInput.startsWith("-")) "-$number" else number
        } else {
            currentInput += number
        }

        updateDisplay()
    }

    private fun onOperatorClick(operator: String) {
        // 1. Jika sudah ada operator aktif, lakukan perhitungan sementara.
        // Ini memungkinkan perhitungan berantai seperti 2+3+4...
        if (currentOperator.isNotEmpty()) {
            performIntermediateCalculation()
        }

        try {
            // 2. Set firstOperand: Gunakan currentInput jika baru mulai operasi
            // atau jika input baru dimasukkan setelah perhitungan sebelumnya.
            if (!shouldResetInput) {
                firstOperand = currentInput.toDouble()
            }

            // 3. Set operator baru
            currentOperator = operator
            shouldResetInput = true // Input berikutnya akan menjadi secondOperand baru

            // 4. Update display: Tampilkan operand pertama dan operator baru
            // Jika tvDisplay2 kosong, tampilkan firstOperand + operator
            val currentDisplay2 = tvDisplay2.text.toString()
            if (currentDisplay2.isEmpty() || currentDisplay2.endsWith("=") || shouldResetInput) {
                tvDisplay2.text = "${formatResult(firstOperand)} $operator"
            } else {
                // Jika sudah ada operasi berlanjut, tambahkan operator baru
                tvDisplay2.text = "$currentDisplay2 $operator"
            }

            // Pastikan currentInput di tvDisplay1 menampilkan firstOperand
            currentInput = formatResult(firstOperand)
            updateDisplay()
        } catch (e: NumberFormatException) {
            showError("Invalid number")
        }
    }

    /**
     * Melakukan perhitungan saat operator kedua atau berikutnya ditekan
     * (e.g., A + B, lalu menekan - akan menghitung A+B, lalu menyimpan hasilnya sebagai A)
     */
    private fun performIntermediateCalculation() {
        if (currentOperator.isEmpty()) return

        try {
            secondOperand = currentInput.toDouble()
            var result = 0.0

            result = when (currentOperator) {
                "+" -> firstOperand + secondOperand
                "-" -> firstOperand - secondOperand
                "×" -> firstOperand * secondOperand
                "÷" -> {
                    if (secondOperand == 0.0) {
                        throw ArithmeticException("Division by zero")
                    }
                    firstOperand / secondOperand
                }
                else -> 0.0
            }

            // 1. Hasilnya menjadi firstOperand untuk operasi berikutnya
            firstOperand = result

            // 2. Update currentInput (tvDisplay1) dengan hasil sementara
            currentInput = formatResult(result)

            // 3. Update Display 2: Tambahkan operand kedua ke operasi yang sedang berlangsung
            // Contoh: "2 + " menjadi "2 + 3"
            val currentDisplay2 = tvDisplay2.text.toString()
            val operatorSymbol = currentDisplay2.last().toString()

            // Hapus operator lama, tambahkan operand, lalu biarkan onOperatorClick menambahkan operator baru
            tvDisplay2.text = currentDisplay2.substring(0, currentDisplay2.length - operatorSymbol.length) +
                    "${formatResult(secondOperand)}"


            shouldResetInput = true // Input berikutnya akan menjadi operand kedua yang baru

            updateDisplay()
        } catch (e: ArithmeticException) {
            showError("Cannot divide by zero")
        } catch (e: NumberFormatException) {
            showError("Invalid number")
        }
    }


    private fun calculateResult() {
        if (currentOperator.isEmpty()) return

        try {
            // Jika currentInput belum di-reset, secondOperand adalah currentInput
            if (shouldResetInput) {
                // Kasus: 2 + = (menggunakan secondOperand dari perhitungan terakhir)
                // Jika ini adalah perhitungan pertama (misal, 5 + =), maka secondOperand = 0,
                // tapi jika 5 + 3 =, lalu 8 + =, maka secondOperand tetap 3.
                // Untuk kasus sederhana (misal, 5 + =), kita anggap secondOperand = firstOperand
                if (tvDisplay2.text.toString().endsWith("=")) {
                    // Jika baru saja menekan =, gunakan hasil terakhir
                } else if (secondOperand == 0.0) {
                    secondOperand = firstOperand
                }
            } else {
                secondOperand = currentInput.toDouble()
            }

            var result = 0.0
            val operatorUsed = currentOperator

            result = when (operatorUsed) {
                "+" -> firstOperand + secondOperand
                "-" -> firstOperand - secondOperand
                "×" -> firstOperand * secondOperand
                "÷" -> {
                    if (secondOperand == 0.0) {
                        throw ArithmeticException("Division by zero")
                    }
                    firstOperand / secondOperand
                }
                else -> 0.0
            }

            // 1. Format hasil
            val resultFormatted = formatResult(result)

            // 2. Perbarui Display 2 dengan operasi lengkap
            // Contoh: "2 + 3 × 6" menjadi "2 + 3 × 6 ="
            tvDisplay2.text = tvDisplay2.text.toString() + " ${formatResult(secondOperand)} ="


            // 3. Reset status untuk perhitungan baru
            currentInput = resultFormatted
            currentOperator = "" // Hentikan operator
            firstOperand = result // Hasilnya disimpan jika user menekan operator lagi
            shouldResetInput = true // Siap menerima input baru

            updateDisplay()

        } catch (e: ArithmeticException) {
            showError("Cannot divide by zero")
        } catch (e: NumberFormatException) {
            showError("Invalid number")
        }
    }

    private fun formatResult(result: Double): String {
        return if (result % 1 == 0.0) {
            result.toLong().toString() // Gunakan toLong() untuk menghindari batas Int yang kecil
        } else {
            // Batasi hingga 10 digit desimal dan hapus trailing zeros
            String.format("%.10f", result).removeTrailingZeros()
        }
    }

    private fun clearAll() {
        currentInput = "0"
        firstOperand = 0.0
        secondOperand = 0.0
        currentOperator = ""
        shouldResetInput = false
        tvDisplay1.text = "0"
        tvDisplay2.text = ""
    }

    private fun resetCalculator() {
        clearAll()
        Toast.makeText(this, "Calculator reset", Toast.LENGTH_SHORT).show()
    }

    private fun backspace() {
        if (shouldResetInput) return // Jangan backspace jika input adalah hasil perhitungan

        if (currentInput.length > 1 && !(currentInput.length == 2 && currentInput.startsWith("-"))) {
            currentInput = currentInput.substring(0, currentInput.length - 1)
        } else {
            currentInput = "0"
        }
        updateDisplay()
    }

    private fun addDecimalPoint() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            updateDisplay()
        }
    }

    private fun toggleSign() {
        if (currentInput != "0") {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        tvDisplay1.text = currentInput

        // tvDisplay2 diupdate secara eksplisit di onOperatorClick, performIntermediateCalculation, dan calculateResult
    }

    private fun showError(message: String) {
        tvDisplay1.text = "Error"
        tvDisplay2.text = message
        currentInput = "0"
        currentOperator = ""
        shouldResetInput = true

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Fungsi untuk mengirim hasil ke CheckoutActivity
    private fun sendToCheckout() {
        val result = tvDisplay1.text.toString()

        // Validasi hasil
        if (result == "Error" || result == "0") {
            Toast.makeText(this, "Tidak ada hasil yang valid untuk checkout", Toast.LENGTH_SHORT).show()
            return
        }

        // Asumsi ada CheckoutActivity
        // val intent = Intent(this, CheckoutActivity::class.java)
        // intent.putExtra("CALCULATOR_RESULT", result)
        // startActivity(intent)

        Toast.makeText(this, "Checkout dengan hasil: $result", Toast.LENGTH_LONG).show()
    }

    // Extension function untuk menghapus nol trailing
    private fun String.removeTrailingZeros(): String {
        return if (this.contains(".")) {
            this.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
        } else {
            this
        }
    }
}