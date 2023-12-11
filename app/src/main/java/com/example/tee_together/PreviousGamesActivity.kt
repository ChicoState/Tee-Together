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
import com.google.firebase.firestore.Query // Add this import statement


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

class PreviousGamesBuilder() {
    fun buildActivity(container: LinearLayout, context: Context) {
        val auth = FirebaseAuth.getInstance()
        val userid = auth.uid.toString()
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(userid)
            .collection("games")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot) {
                    val game = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        setPadding(16, 16, 16, 16)
                        setBackgroundResource(R.drawable.cell_shape)
                    }

                    val date = TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = doc.getDate("timestamp").toString()
                    }

                    val holeDataList = doc.get("holes") as? ArrayList<HashMap<String, Any>>
                    var totalScore = 0
                    var totalPar = 0

                    holeDataList?.let { list ->
                        for (holeDataMap in list) {
                            val userScoresMap = holeDataMap["userScores"] as? Map<String, Map<String, Any>>
                            userScoresMap?.values?.forEach { userDataMap ->
                                val score = (userDataMap["score"] as? Number)?.toInt() ?: 0
                                val par = (userDataMap["par"] as? Number)?.toInt() ?: 0
                                totalScore += par
                                totalPar += par
                            }
                        }
                    }

                    val finalScore = TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = "Final Score: $totalScore Par: $totalPar"
                        setPadding(16, 0, 0, 0)
                    }

                    game.addView(date)
                    game.addView(finalScore)
                    container.addView(game)
                }
            }
    }
}
