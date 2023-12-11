package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Grab current firebaseauth instance
        auth = FirebaseAuth.getInstance()

        // Retrieve the following widgets
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Set a click listener on login button, try to login user if pressed
        loginButton.setOnClickListener {
            // Grab email and password strings from fields
            val email = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            // If email or password provided, try to login user
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else { // else make Toast widget to inform user
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        // If User not null, go to the profile page
        if (currentUser != null) {
            goToProfileActivity()
        }
    }

    private fun signInUser(email: String, password: String) {
        // Try to sign in user and go to profile page
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                goToProfileActivity()
            } else { // if unable to login, show the error and make a Toast to the user
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()  // Finish LoginActivity so it's removed from the back stack
    }
}
