package com.example.tee_together

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import android.widget.CheckBox
import android.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import android.text.TextUtils
import com.google.firebase.firestore.FirebaseFirestore


class ScoreCardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var friendEmails = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val currentUserID = currentUser?.uid ?: "Anonymous"
        val currentUserName = currentUser?.displayName ?: "Anonymous User" // Default to "Anonymous User" if displayName is not available

        val scorecardHandler = ScoreCardHandler(currentUserID, currentUserName)
        val addHoleButton = findViewById<ImageButton>(R.id.add_hole)
        val containerScores = findViewById<LinearLayout>(R.id.scores_for_holes)
        val changeToResultButton = findViewById<ImageButton>(R.id.change_to_result_scorecard)
        val backToProfileButton = findViewById<ImageButton>(R.id.back_to_profile)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val addEmailButton = findViewById<Button>(R.id.addEmailButton)


        fun updateScorecard(container: LinearLayout, context: Context) {
            container.removeAllViews()
            scorecardHandler.createNewHole(container, context) // Call on the scorecardHandler instance

            container.post {
                container.invalidate()
            }
        }

        fun findUsersByEmails(container: LinearLayout, context: Context) {
            val db = FirebaseFirestore.getInstance()
            friendEmails.forEach { email ->
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        var userAdded = false
                        for (document in documents) {
                            val userId = document.id
                            val displayName = document.getString("displayName") ?: "Unknown"
                            if (!scorecardHandler.users.contains(userId)) {
                                scorecardHandler.addUser(userId, displayName)
                                userAdded = true
                            }
                        }
                        if (userAdded) {
                            updateScorecard(container, context) // Call a method to refresh UI
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to query users: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }




        addEmailButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty() && isValidEmail(email)) {
                friendEmails.add(email)
                emailInput.text.clear()
                findUsersByEmails(containerScores, this)  // Call this after adding an email
                Toast.makeText(this, "Friend added: $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            }
        }


        addHoleButton.setOnClickListener {
            scorecardHandler.createNewHole(containerScores, this)
        }

        changeToResultButton.setOnClickListener {
            val name = intent.getStringExtra("username")
            if (scorecardHandler.getStrokesForHoles().isNotEmpty()) {
                val intent = Intent(this, ScoreCardResultActivity::class.java)
                intent.putExtra("player_names", name)
                intent.putExtra("hole_data", ArrayList(scorecardHandler.getStrokesForHoles()))
                intent.putExtra("user_display_names", HashMap(scorecardHandler.userDisplayNames)) // Pass the display names
                startActivity(intent)
            }
        }
        backToProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
class ScoreCardHandler(private val currentUser: String, private val currentUserName: String) {

    private var holeCount = 0
    private var holes = mutableListOf<HoleData>()
    var users = mutableListOf<String>() // Initialize with current user
    var userDisplayNames = mutableMapOf<String, String>() // Map to hold userId + displayName
    private val userHoleDataMap = mutableMapOf<String, HoleData>()
    fun addUser(userId: String, displayName: String) {
        if (!users.contains(userId)) {
            users.add(userId)
            userDisplayNames[userId] = displayName

            // Add the new user's data to all existing holes
            holes.forEach { holeData ->
                holeData.userScores[userId] = UserHoleData(0, 0, false, false)
            }
        }
    }


    init {
        addUser(currentUser, currentUserName) // Add the current user on initialization
    }

    fun createNewHole(container: LinearLayout, context: Context) {
        val userHoleDataMap = users.associateWith { UserHoleData(0, 0, false, false) }
            .toMutableMap() // Convert to MutableMap
        val newHoleData = HoleData(userHoleDataMap)
        holes.add(newHoleData)


        val holeLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            setPadding(16)
        }

        val holeNumberTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = "Hole ${holeCount + 1}"
            setPadding(8)
            setTextColor(Color.WHITE)
        }

        holeLayout.addView(holeNumberTextView)

        users.forEach { userId ->
            val userScoreLayout = LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }

            val displayName = userDisplayNames[userId] ?: userId // Get our display name
            val playerNameTextView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = displayName
                setPadding(8)
                setTextColor(Color.WHITE)
            }

            val scoreTextView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Score: 0"
                setPadding(8)
                setTextColor(Color.WHITE)
            }

            val incrementButton = ImageButton(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setImageResource(R.drawable.baseline_add_24)
                setOnClickListener {
                    val currentScore = newHoleData.userScores[userId]?.score ?: 0
                    newHoleData.userScores[userId]?.score = currentScore + 1
                    scoreTextView.text = "Score: ${currentScore + 1}"
                }
            }

            val decrementButton = ImageButton(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setImageResource(R.drawable.baseline_remove_24)
                setOnClickListener {
                    val currentScore = newHoleData.userScores[userId]?.score ?: 0
                    if (currentScore > 0) {
                        newHoleData.userScores[userId]?.score = currentScore - 1
                        scoreTextView.text = "Score: ${currentScore - 1}"
                    }
                }
            }

            val firCheckbox = CheckBox(context).apply {
                text = "FIR"
                setOnCheckedChangeListener { _, isChecked ->
                    newHoleData.userScores[userId]?.fir = isChecked
                }
            }

            val girCheckbox = CheckBox(context).apply {
                text = "GIR"
                setOnCheckedChangeListener { _, isChecked ->
                    newHoleData.userScores[userId]?.gir = isChecked
                }
            }

            userScoreLayout.addView(playerNameTextView)
            userScoreLayout.addView(scoreTextView)
            userScoreLayout.addView(incrementButton)
            userScoreLayout.addView(decrementButton)
            userScoreLayout.addView(firCheckbox)
            userScoreLayout.addView(girCheckbox)

            holeLayout.addView(userScoreLayout)
        }

        container.addView(holeLayout)
        holeCount++
    }
    fun getStrokesForHoles(): MutableList<HoleData> {
        return holes
    }
}