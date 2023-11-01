package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val scorecardButton: ImageButton = findViewById<ImageButton>(R.id.scorecardButton)
        scorecardButton.setOnClickListener {
            val intent = Intent(this, ScoreCardActivity::class.java)
            startActivity(intent)
        }
        val profileButton: ImageButton = findViewById<ImageButton>(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}