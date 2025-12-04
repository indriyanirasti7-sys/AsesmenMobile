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

class kalkulator : AppCompatActivity() {

    private lateinit var tvDisplay1: TextView
    private lateinit var tvDisplay2: TextView

    private var currentInput = "0"
    private var firstOperand = 0.0
    private var secondOperand = 0.0
    private var currentOperator = ""
    private var shouldResetInput = false

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

        findViewById<Button>(R.id.btnsatu).setOnClickListener {
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
            currentInput = "0"
            shouldResetInput = false
        }

        if (currentInput == "0") {
            currentInput = number
        } else {
            currentInput += number
        }

        updateDisplay()
    }

    private fun onOperatorClick(operator: String) {
        if (currentOperator.isNotEmpty()) {
            calculateResult()
        }

        try {
            firstOperand = currentInput.toDouble()
            currentOperator = operator
            shouldResetInput = true

            tvDisplay2.text = "$firstOperand $operator"
        } catch (e: NumberFormatException) {
            showError("Invalid number")
        }
    }

    private fun calculateResult() {
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

            // Format hasil
            currentInput = formatResult(result)
            tvDisplay2.text = "$firstOperand $currentOperator $secondOperand ="
            currentOperator = ""
            shouldResetInput = true
            updateDisplay()

        } catch (e: ArithmeticException) {
            showError("Cannot divide by zero")
        } catch (e: NumberFormatException) {
            showError("Invalid number")
        }
    }

    private fun formatResult(result: Double): String {
        return if (result % 1 == 0.0) {
            result.toInt().toString()
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
        if (currentInput.length > 1) {
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

        // Update display2 dengan operasi yang sedang berlangsung
        if (currentOperator.isNotEmpty() && !shouldResetInput) {
            tvDisplay2.text = "$firstOperand $currentOperator"
        }
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