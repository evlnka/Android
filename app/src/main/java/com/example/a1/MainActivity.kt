package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var bGoToCalculator: Button
    private lateinit var bGoToPlayer: Button
    private lateinit var bGoToLocation: Button  //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bGoToCalculator = findViewById(R.id.Calculator)
        bGoToPlayer = findViewById(R.id.MP3player)
        bGoToLocation = findViewById(R.id.Location)

        bGoToCalculator.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        bGoToPlayer.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }

        bGoToLocation.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
    }
}
