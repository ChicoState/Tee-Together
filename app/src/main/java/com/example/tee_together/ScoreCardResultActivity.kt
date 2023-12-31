package com.example.tee_together

import android.content.Context
import com.google.firebase.firestore.FieldValue
import android.os.Bundle
import android.util.Log
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

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName ?: "Anonymous"  // Use the display name from FirebaseAuth

        // Retrieve holes and user display names from the intent
        val holes = intent.getSerializableExtra("hole_data") as? ArrayList<HoleData>
        val userDisplayNames = intent.extras?.getSerializable("user_display_names") as? Map<String, String> ?: emptyMap()
        // Added this to hold all names in match
        val allPlayerNames = userDisplayNames.values.toList() // Create a list of all player names

        // Initialize the table layout container and the result handler
        val tableLayoutContainer = findViewById<TableLayout>(R.id.scorecard_table)
        val scoreCardResultHandler = ScoreCardResultHandler()

        // Create the result table
        scoreCardResultHandler.createResult(displayName, holes, userDisplayNames, tableLayoutContainer, this)

        // Set up the navigation back button
        val navigateBack = findViewById<BottomAppBar>(R.id.bottomAppBarScorecardResult)
        navigateBack.setOnClickListener {
            finish()
        }

        // Set up the save button with its click listener
        val saveButton = findViewById<Button>(R.id.save_scorecard_button)
        saveButton.setOnClickListener {
            saveButton.isEnabled = false
            scoreCardResultHandler.saveScorecardToFirestore(allPlayerNames, holes, this)
        }
    }
}

class ScoreCardResultHandler(){

    private val db = FirebaseFirestore.getInstance()

    fun saveScorecardToFirestore(playerNames: List<String>, holes: ArrayList<HoleData>?, context: ScoreCardResultActivity) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { firebaseUser ->
            // Create a map for each user's complete scorecard
            val userScorecards = mutableMapOf<String, ArrayList<Map<String, Any>>>()

            holes?.forEach { holeData ->
                holeData.userScores.forEach { (userId, userHoleData) ->
                    if (!userScorecards.containsKey(userId)) {
                        userScorecards[userId] = arrayListOf()
                    }
                    val holeMap = hashMapOf(
                        "fir" to userHoleData.fir,
                        "gir" to userHoleData.gir,
                        "par" to userHoleData.par,
                        "score" to userHoleData.score,
                    )
                    userScorecards[userId]?.add(holeMap)
                }
            }

            // Save each user's scorecard
            userScorecards.forEach { (userId, holesData) ->
                val scorecardData = hashMapOf(
                    "playerNames" to playerNames,
                    "holes" to holesData,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("users").document(userId)
                    .collection("games").add(scorecardData)
                    .addOnSuccessListener {
                        context.runOnUiThread {
                            Toast.makeText(context, "Scorecard saved successfully for user: $userId", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        context.runOnUiThread {
                            Toast.makeText(context, "Failed to save scorecard for user: $userId. Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } ?: run {
            Toast.makeText(context, "Not signed in!", Toast.LENGTH_SHORT).show()
        }
    }


    fun createResult(playerNames: String?, holes: ArrayList<HoleData>?, userDisplayNames: Map<String, String>, container: TableLayout, context: Context) {
        if (holes == null || holes.isEmpty()) return
        val uniqueUserIds = holes.flatMap { it.userScores.keys }.distinct()

        val headerRow = TableRow(context)
        headerRow.gravity = Gravity.CENTER
        val header = TextView(context)
        header.text = "Hole/User"
        header.setPadding(8)
        header.setBackgroundResource(R.drawable.cell_shape)
        headerRow.addView(header)

        uniqueUserIds.forEach { userId ->
            val userNameTextView = TextView(context)
            val displayName = userDisplayNames[userId] ?: "Unknown" // Use display name if available, else "Unknown"
            userNameTextView.text = displayName
            userNameTextView.setPadding(8)
            userNameTextView.setBackgroundResource(R.drawable.cell_shape)
            headerRow.addView(userNameTextView)
        }
        container.addView(headerRow)

        // For each hole, create a row
        holes.forEachIndexed { index, holeData ->
            val holeRow = TableRow(context)
            holeRow.gravity = Gravity.CENTER

            // Add hole number
            val holeNumberTextView = TextView(context)
            holeNumberTextView.text = "Hole ${index + 1}"
            holeNumberTextView.setPadding(8)
            holeNumberTextView.setBackgroundResource(R.drawable.cell_shape)
            holeRow.addView(holeNumberTextView)

            // For each user, add their score, par, FIR, and GIR in this hole
            uniqueUserIds.forEach { userId ->
                val userHoleData = holeData.userScores[userId]
                val scoreInfoTextView = TextView(context)
                // Format the user's score, par, FIR, and GIR
                scoreInfoTextView.text = formatUserHoleData(userHoleData)
                scoreInfoTextView.setPadding(8)
                scoreInfoTextView.setBackgroundResource(R.drawable.cell_shape)
                holeRow.addView(scoreInfoTextView)
            }

            container.addView(holeRow)

        }
    }
    private fun formatUserHoleData(userHoleData: UserHoleData?): String {
        // This method returns a formatted string of user's score, par, FIR, and GIR
        // Example: "Score: 4, Par: 3, FIR: Yes, GIR: No"
        userHoleData?.let {
            return "Score: ${it.score}, Par: ${it.par}, FIR: ${if (it.fir) "Yes" else "No"}, GIR: ${if (it.gir) "Yes" else "No"}"
        }
        return "N/A"
    }
}

