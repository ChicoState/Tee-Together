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
        var scoreCardResultHandler = ScoreCardResultHandler()

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName ?: "Anonymous"  // Use the display name from FirebaseAuth

        val holes = intent.getSerializableExtra("hole_data") as? ArrayList<HoleData>

        scoreCardResultHandler.createResult(displayName, holes, tableLayoutContainer, this)

        // For the user, push this data back onto firebase for this game
        val navigateBack = findViewById<BottomAppBar>(R.id.bottomAppBarScorecardResult)
        navigateBack.setOnClickListener{
            finish()
        }
        val saveButton = findViewById<Button>(R.id.save_scorecard_button)
        saveButton.setOnClickListener {
            saveButton.isEnabled = false
            scoreCardResultHandler.saveScorecardToFirestore(user?.displayName, holes, this)
        }
    }
}

class ScoreCardResultHandler(){

    private val db = FirebaseFirestore.getInstance()

    fun saveScorecardToFirestore(playerNames: String?, holes: ArrayList<HoleData>?, context: ScoreCardResultActivity) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { firebaseUser ->
            // Prepare the data map with the timestamp
            val scorecardData = hashMapOf(
                "playerNames" to playerNames,
                "holes" to ArrayList<Map<String, Any>>() ,
                "timestamp" to FieldValue.serverTimestamp() // Utilizing FieldValue.serverTimestamp() directly
            )

            holes?.forEach { holeData ->
                val holeMap = hashMapOf(
                    "score" to holeData.score,
                    "fir" to holeData.fir,
                    "gir" to holeData.gir
                )
                (scorecardData["holes"] as ArrayList<Map<String, Any>>)?.add(holeMap)
            }

            // Save the data in Firestore
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

    fun createResult(playerNames: String?, holes: ArrayList<HoleData>?, container: TableLayout, context: Context){

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
        player.text="$playerNames"
        player.setPadding(8)
        player.setBackgroundResource(R.drawable.cell_shape)

        players.addView(header)
        players.addView(player)
        container.addView(players)

        //Create recorded strokes per hole
        var count = 1
        if (holes != null) {
            for (holeData in holes) {
                val hole = TableRow(context)
                hole.gravity = Gravity.CENTER

                // Hole number
                val holeNumber = TextView(context)
                holeNumber.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                holeNumber.setBackgroundResource(R.drawable.cell_shape)
                holeNumber.text = "Hole $count"
                count += 1
                holeNumber.setPadding(8)

                // Score
                val scorePerHole = TextView(context)
                scorePerHole.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                scorePerHole.text = "Score: ${holeData.score}"
                scorePerHole.setPadding(8)
                scorePerHole.setBackgroundResource(R.drawable.cell_shape)

                // FIR (Fairway in Regulation)
                val firStatus = TextView(context)
                firStatus.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                firStatus.text = "FIR: ${if (holeData.fir) "Yes" else "No"}"
                firStatus.setPadding(8)
                firStatus.setBackgroundResource(R.drawable.cell_shape)

                // GIR (Green in Regulation)
                val girStatus = TextView(context)
                girStatus.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                girStatus.text = "GIR: ${if (holeData.gir) "Yes" else "No"}"
                girStatus.setPadding(8)
                girStatus.setBackgroundResource(R.drawable.cell_shape)

                // Add views to the TableRow
                hole.addView(holeNumber)
                hole.addView(scorePerHole)
                hole.addView(firStatus)
                hole.addView(girStatus)

                // Add TableRow to the TableLayout
                container.addView(hole)
            }
        }
    }
}