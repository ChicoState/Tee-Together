package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        val profileUsername = findViewById<TextView>(R.id.profileUsername)
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        val btnPreviousGames = findViewById<Button>(R.id.btnPreviousGames)
        val btnNewScorecard = findViewById<Button>(R.id.btnNewScorecard)

        // Set the username
        val user = auth.currentUser
        profileUsername.text = user?.displayName ?: "No Name"

        // Sign out button click listener
        signOutButton.setOnClickListener {
            auth.signOut()
            navigateToLogin()
        }

        // Previous Games button click listener
        btnPreviousGames.setOnClickListener {
            val intent = Intent(this, PreviousGamesActivity::class.java)
            startActivity(intent)
        }

        // Start New Scorecard button click listener
        btnNewScorecard.setOnClickListener {
            val intent = Intent(this, ScoreCardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
