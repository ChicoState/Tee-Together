package com.example.tee_together

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PreviousGamesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_games)
        // Grab the linear layout widget that should be currently empty before builder called
        val layoutContainer = findViewById<LinearLayout>(R.id.previous_games)
        // Create an instance of our builder class
        var previousGamesBuilder = PreviousGamesBuilder()
        // Begin building the previous games layout and store into our container
        previousGamesBuilder.buildActivity(layoutContainer, this)
        // Grab the back button
        val previous = findViewById<ImageButton>(R.id.back_from_previous)
        // Listen to the back button, and move us back to Profile view if pressed
        previous.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}

class PreviousGamesBuilder(){
    fun buildActivity(container: LinearLayout, context: Context){
        var auth = FirebaseAuth.getInstance()
        var userid = auth.uid.toString()
        var db = FirebaseFirestore.getInstance()
        // Grab the previously stored games of current user
        db.collection("users").document(userid).collection("games").get().addOnSuccessListener {
                querySnapshot ->
            // for each previous game of user
            for (doc in querySnapshot){
                // Create a linearlayout to store current game in query
                val game = LinearLayout(context)
                game.orientation = LinearLayout.HORIZONTAL
                game.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                game.setPadding(16, 16, 16, 16)
                game.setBackgroundResource(R.drawable.cell_shape)
                // Make the date timestamp of game
                val date = TextView(context)
                date.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                date.text = doc.getDate("timestamp").toString()
                // Make the final score of game
                val finalScore = TextView(context)
                finalScore.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val strokes = doc.get("strokes") as List<Int>
                var count = 0
                // Tally up final score based on each hole score in the game
                for (stroke in strokes){
                    count += stroke
                }
                finalScore.text = "Final Score: $count"
                finalScore.setPadding(16, 0, 0 , 0)
                // Add each widget back to our linearlayout
                game.addView(date)
                game.addView(finalScore)
                container.addView(game)
            }
        }
    }
}