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
        val layoutContainer = findViewById<LinearLayout>(R.id.previous_games)
        var previousGamesBuilder = PreviousGamesBuilder()
        previousGamesBuilder.buildActivity(layoutContainer, this)
        val previous = findViewById<ImageButton>(R.id.back_from_previous)
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

                val holeDataList = doc.get("holes") as ArrayList<HashMap<String, Any>>?

                val finalScore = TextView(context)
                finalScore.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // Sum up the scores from HoleData objects
                var count = 0
                var par = 0
                holeDataList?.let {
                    for (holeDataMap in it) {
                        val holeData = HoleData(
                            (holeDataMap["score"] as? Long)?.toInt() ?: 0,
                            (holeDataMap["par"] as? Long)?.toInt() ?: 0,
                            holeDataMap["fir"] as Boolean,
                            holeDataMap["gir"] as Boolean
                        )
                        count += holeData.score
                        par += holeData.par
                    }
                }

                finalScore.text = "Final Score: $count Par: $par"
                finalScore.setPadding(16, 0, 0, 0)

                game.addView(date)
                game.addView(finalScore)

                container.addView(game)
            }
        }
    }
}