package com.example.tee_together

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton



class ScoreCardActivity : AppCompatActivity() {
    private lateinit var scorecardHandler: ScoreCardHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard)

        // Create an instance of our scorecard handler to build up our view
        scorecardHandler = ScoreCardHandler()

        val addHole = findViewById<FloatingActionButton>(R.id.add_hole)
        val containerScores = findViewById<LinearLayout>(R.id.scores_for_holes)
        val changeToResult = findViewById<ImageButton>(R.id.change_to_result_scorecard)
        val goToProfile = findViewById<BottomAppBar>(R.id.bottomAppBar)
        // Listen on the add hole button and let our handler add a new hole on press
        addHole.setOnClickListener {
            scorecardHandler.createNewHole(containerScores, this)
        }
        // Listen on to the change to result button, and change to result view on press
        changeToResult.setOnClickListener {
            // Storing this information along on intent may not be necessary anymore
            //  This was a workaround to allow us to pass data from view to view before the database was set up
            val name = intent.getStringExtra("username")
            // As long as the scorecard isn't empty, goahead and start up the result view
            if (scorecardHandler.getStrokesForHoles().isNotEmpty()) {
                val intent = Intent(this, ScoreCardResultActivity::class.java)
                intent.putExtra("player_names", name)
                intent.putExtra(
                    "strokes_per_holes",
                    scorecardHandler.getStrokesForHoles().toIntArray()
                )
                startActivity(intent)
            }
        }
        // Listen on navigation button and move user back to profile view on press
        goToProfile.setNavigationOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}

class ScoreCardHandler {
    private var holeCount = 0
    private var scores = mutableListOf<Int>()
    private var maxHoles = 18

    fun createNewHole(container: LinearLayout, context: Context) {
        // Check if the hole count has reached the maximum
        if (holeCount >= maxHoles) {
            Toast.makeText(context, "Maximum number of holes reached.", Toast.LENGTH_SHORT).show()
            return // Stop the function from proceeding further
        }
        // Add a new score to list of scores
        scores.add(1)
        val newScore = LinearLayout(context)
        newScore.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        newScore.orientation = LinearLayout.HORIZONTAL
        newScore.setBackgroundResource(R.drawable.cell_shape)
        container.addView(newScore)
        // Create a new hole number text view
        val holeNumber = TextView(context)
        holeNumber.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holeNumber.setPadding(16)
        val holeToPrint = holeCount + 1
        holeNumber.text = "Hole $holeToPrint"
        holeCount += 1
        // Create our increment button widget
        val incrementButton = ImageButton(context)
        incrementButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        incrementButton.setImageResource(R.drawable.baseline_add_24)
        incrementButton.setPadding(10)
        // Create our decrement button widget
        val decrementButton = ImageButton(context)
        decrementButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        decrementButton.setImageResource(R.drawable.baseline_remove_24)
        decrementButton.setPadding(10)
        // Create score for current hole
        val holeScore = TextView(context)
        holeScore.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holeScore.gravity = Gravity.END
        holeScore.setPadding(16)
        val currScore = scores[holeCount - 1]
        holeScore.text = "Score: $currScore"
        // Get the index for score for this hole in scores
        val index = holeCount - 1
        // set a listener on decrement button, to decrement score
        decrementButton.setOnClickListener {
            if (scores[index] > 0) {
                scores[index] -= 1
            }
            val score = scores[index]
            holeScore.text = "Score: $score"
        }
        // set a listener on increment button, to increment score
        incrementButton.setOnClickListener {
            scores[index] += 1
            val score = scores[index]
            holeScore.text = "Score: $score"
        }
        // add newly created widgets to our linear layout
        newScore.addView(holeNumber)
        newScore.addView(incrementButton)
        newScore.addView(decrementButton)
        newScore.addView(holeScore)
    }

    fun getStrokesForHoles(): MutableList<Int> {
        return scores;
    }
}