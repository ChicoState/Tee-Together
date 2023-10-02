package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val button: Button = findViewById(R.id.button) // Create Account button id

        // Set a click listener for the Create Account button
        button.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)  // Start CreateAccountActivity
        }
    }
}
