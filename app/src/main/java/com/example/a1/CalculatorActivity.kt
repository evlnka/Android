package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var input: EditText
    private lateinit var result: TextView
    private lateinit var plus: Button
    private lateinit var minus: Button
    private lateinit var multiply: Button
    private lateinit var division: Button
    private lateinit var clear: Button
    private lateinit var equals: Button
    private lateinit var exitButton: Button

    private var num1: Float? = null
    private var operation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        input = findViewById(R.id.input)
        result = findViewById(R.id.result)
        plus = findViewById(R.id.plus)
        minus = findViewById(R.id.minus)
        multiply = findViewById(R.id.multiply)
        division = findViewById(R.id.division)
        clear = findViewById(R.id.clear)
        equals = findViewById(R.id.equals)
        exitButton = findViewById(R.id.exit)

        plus.setOnClickListener(this)
        minus.setOnClickListener(this)
        multiply.setOnClickListener(this)
        division.setOnClickListener(this)
        clear.setOnClickListener(this)
        equals.setOnClickListener(this)

        exitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.plus, R.id.minus, R.id.multiply, R.id.division -> {
                if (num1 == null) {
                    num1 = input.text.toString().toFloatOrNull()
                    operation = (view as Button).text.toString()
                    input.append(" $operation ")
                }
            }
            R.id.equals -> {
                val num2 = input.text.toString().split(" ").lastOrNull()?.toFloatOrNull()
                if (num1 != null && num2 != null) {
                    result.text = when (operation) {
                        "+" -> (num1!! + num2).toString()
                        "-" -> (num1!! - num2).toString()
                        "*" -> (num1!! * num2).toString()
                        "/" -> if (num2 != 0f) (num1!! / num2).toString() else "Ошибка"
                        else -> ""
                    }
                }
            }
            R.id.clear -> {
                input.text.clear()
                result.text = "Результат: "
                num1 = null
                operation = null
            }
        }
    }
}