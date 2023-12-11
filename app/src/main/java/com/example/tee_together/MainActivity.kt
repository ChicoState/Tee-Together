package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// The default landing page of our application
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Grab the create account and login widgets
        val button: Button = findViewById(R.id.button) // Create Account button id
        val button2: Button = findViewById(R.id.button2) // Login Button

        // Set a click listener for the Create Account button, starts the create account activity
        button.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)  // Start CreateAccountActivity
        }
        // Set a click listener on Login Button, starts the login activity
        button2.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
