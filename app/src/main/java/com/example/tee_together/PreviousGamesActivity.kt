package com.example.tee_together

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PreviousGamesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_games)
        val layoutContainer = findViewById<LinearLayout>(R.id.previous_games)
        var previousGamesBuilder = PreviousGamesBuilder()
        previousGamesBuilder.buildActivity(layoutContainer, this)
    }
}

class PreviousGamesBuilder(){
    fun buildActivity(container: LinearLayout, context: Context){
        var auth = FirebaseAuth.getInstance()
        var userid = auth.uid.toString()
        var db = FirebaseFirestore.getInstance()
        db.collection("users").document(userid).collection("games").get().addOnSuccessListener {
                querySnapshot ->
            for (doc in querySnapshot){
                val game = LinearLayout(context)
                game.orientation = LinearLayout.HORIZONTAL
                game.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                game.setPadding(16, 16, 16, 16)
                game.setBackgroundResource(R.drawable.cell_shape)

                val date = TextView(context)
                date.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                date.text = doc.getDate("timestamp").toString()

                val finalScore = TextView(context)
                finalScore.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val strokes = doc.get("strokes") as List<Int>
                var count = 0
                for (stroke in strokes){
                    count += stroke
                }
                finalScore.text = "Final Score: $count"
                finalScore.setPadding(16, 0, 0 , 0)
                game.addView(date)
                game.addView(finalScore)
                container.addView(game)
            }
        }
    }
}