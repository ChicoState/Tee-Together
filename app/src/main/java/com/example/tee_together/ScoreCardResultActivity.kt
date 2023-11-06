package com.example.tee_together

import android.content.Context
import com.google.firebase.firestore.FieldValue
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.bottomappbar.BottomAppBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Button


class ScoreCardResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard_result)

        val tableLayoutContainer = findViewById<TableLayout>(R.id.scorecard_table)
        val scoreCardResultHandler = ScoreCardResultHandler()

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName ?: "Anonymous"  // Use the display name from FirebaseAuth

        val strokes = intent.getIntArrayExtra("strokes_per_holes")

        scoreCardResultHandler.createResult(displayName, strokes, tableLayoutContainer, this)

        val navigateBack = findViewById<BottomAppBar>(R.id.bottomAppBarScorecardResult)
        navigateBack.setOnClickListener {
            finish()
        }

        val saveButton = findViewById<Button>(R.id.save_scorecard_button)
        saveButton.setOnClickListener {
            saveButton.isEnabled = false
            scoreCardResultHandler.saveScorecardToFirestore(user?.displayName, strokes, this)
        }
    }

}

class ScoreCardResultHandler(){

    // Firestore database instance
    private val db = FirebaseFirestore.getInstance()

    fun saveScorecardToFirestore(playerNames: String?, strokes: IntArray?, context: ScoreCardResultActivity) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { firebaseUser ->
            // Prepare the data map with the timestamp
            val scorecardData = hashMapOf(
                "playerNames" to playerNames,
                "strokes" to strokes?.toList(),
                "timestamp" to FieldValue.serverTimestamp() // Utilizing FieldValue.serverTimestamp() directly
            )

            // Proceed to save the data in Firestore
            db.collection("users").document(firebaseUser.uid)
                .collection("games").add(scorecardData)
                .addOnSuccessListener {
                    // Handle success
                    context.runOnUiThread {
                        Toast.makeText(context, "Scorecard saved successfully.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    context.runOnUiThread {
                        Toast.makeText(context, "Failed to save scorecard: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } ?: run {
            // Handle case where user is not signed in
            Toast.makeText(context, "Not signed in!", Toast.LENGTH_SHORT).show()
        }
    }

    fun createResult(playerNames: String?, strokes: IntArray?, container: TableLayout, context: Context){

        // Begin by iterating through player names and adding each player
        val players = TableRow(context)
        players.gravity = Gravity.CENTER
        val header = TextView(context)
        header.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        header.text = "Players: "
        header.setPadding(8)
        header.setBackgroundResource(R.drawable.cell_shape)
        // For now, just one player :: TODO::REPLACE WITH ITERATION
        val player = TextView(context)
        player.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        val user = FirebaseAuth.getInstance().currentUser
        player.text=user?.displayName ?: "Anonymous"
        player.setPadding(8)
        player.setBackgroundResource(R.drawable.cell_shape)

        players.addView(header)
        players.addView(player)
        container.addView(players)

        //Create recorded strokes per hole
        var count = 1
        if (strokes != null) {
            for (stroke in strokes){
                val hole = TableRow(context)
                hole.gravity = Gravity.CENTER
                val holeNumber = TextView(context)
                holeNumber.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                holeNumber.setBackgroundResource(R.drawable.cell_shape)
                holeNumber.text = "Hole $count"
                count += 1
                holeNumber.setPadding(8)
                //TODO::Replace with for loop to iterate through each players score
                val strokePerPlayer = TextView(context)
                strokePerPlayer.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                strokePerPlayer.text="$stroke"
                strokePerPlayer.setPadding(8)
                strokePerPlayer.setBackgroundResource(R.drawable.cell_shape)
                hole.addView(holeNumber)
                hole.addView(strokePerPlayer)
                container.addView(hole)
            }
        }
    }
}