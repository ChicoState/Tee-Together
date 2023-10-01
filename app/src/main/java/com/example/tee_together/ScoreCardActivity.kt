package com.example.tee_together

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScoreCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard)
        var scorecardHandler = ScoreCard()
        val addHole = findViewById<FloatingActionButton>(R.id.add_hole)
        val containerScores = findViewById<LinearLayout>(R.id.scores_for_holes)
        addHole.setOnClickListener {
            /*
            // Create a new widget for a players score
            val newScore = LinearLayout(this)
            newScore.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            newScore.orientation = LinearLayout.HORIZONTAL
            newScore.setBackgroundResource(R.drawable.cell_shape)
            containerScores.addView(newScore)
            val holeNumber = TextView(this)
            holeNumber.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            holeNumber.setPadding(16)
            val holeToPrint = holeCount + 1
            holeNumber.text = "Hole $holeToPrint"
            holeCount += 1
            val incrementButton = ImageButton(this)
            incrementButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            incrementButton.setImageResource(R.drawable.baseline_add_24)
            incrementButton.setPadding(10)
            val decrementButton = ImageButton(this)
            decrementButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            decrementButton.setImageResource(R.drawable.baseline_remove_24)
            decrementButton.setPadding(10)
            val holeScore = TextView(this)
            holeScore.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            holeScore.gravity = Gravity.END
            decrementButton.setPadding(10)
             */
            scorecardHandler.createNewHole(containerScores, this)
        }
    }
}

class ScoreCard{
    private var holeCount = 0
    private var scores = mutableListOf<Int>()

    fun createNewHole(container: LinearLayout, context: Context){
        scores.add(0)
        val newScore = LinearLayout(context)
        newScore.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        newScore.orientation = LinearLayout.HORIZONTAL
        newScore.setBackgroundResource(R.drawable.cell_shape)
        container.addView(newScore)
        val holeNumber = TextView(context)
        holeNumber.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holeNumber.setPadding(16)
        val holeToPrint = holeCount + 1
        holeNumber.text = "Hole $holeToPrint"
        holeCount += 1
        val incrementButton = ImageButton(context)
        incrementButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        incrementButton.setImageResource(R.drawable.baseline_add_24)
        incrementButton.setPadding(10)
        val decrementButton = ImageButton(context)
        decrementButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        decrementButton.setImageResource(R.drawable.baseline_remove_24)
        decrementButton.setPadding(10)
        val holeScore = TextView(context)
        holeScore.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holeScore.gravity = Gravity.END
        holeScore.setPadding(16)
        val currScore = scores[holeCount - 1]
        holeScore.text = "Score: $currScore"
        decrementButton.setOnClickListener {
            val index = holeCount - 1
            if (scores[index] > 0){
                scores[index] -= 1
            }
            val score = scores[index]
            holeScore.text = "Score: $score"
        }
        incrementButton.setOnClickListener {
            val index = holeCount - 1
            scores[index] += 1
            val score = scores[index]
            holeScore.text = "Score: $score"
        }

        newScore.addView(holeNumber)
        newScore.addView(incrementButton)
        newScore.addView(decrementButton)
        newScore.addView(holeScore)
    }
}