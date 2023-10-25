package com.example.tee_together

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
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
        var scorecardHandler = ScoreCardHandler()
        val addHole = findViewById<ImageButton>(R.id.add_hole)
        val containerScores = findViewById<LinearLayout>(R.id.scores_for_holes)
        val changeToResult = findViewById<ImageButton>(R.id.change_to_result_scorecard)
        addHole.setOnClickListener {
            scorecardHandler.createNewHole(containerScores, this)
        }
        changeToResult.setOnClickListener(View.OnClickListener {
            val name = intent.getStringExtra("username")
            if (scorecardHandler.getStrokesForHoles().toIntArray().isNotEmpty()) {
                val intent = Intent(this, ScoreCardResultActivity::class.java)
                intent.putExtra("player_names", name)
                intent.putExtra(
                    "strokes_per_holes",
                    scorecardHandler.getStrokesForHoles().toIntArray()
                )
                startActivity(intent)
            }
        })
    }
}

class ScoreCardHandler{
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
        Log.d("Hello", "hello $holeCount")
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
        val index = holeCount - 1
        decrementButton.setOnClickListener {
            if (scores[index] > 0){
                scores[index] -= 1
            }
            val score = scores[index]
            holeScore.text = "Score: $score"
        }
        incrementButton.setOnClickListener {
            scores[index] += 1
            val score = scores[index]
            holeScore.text = "Score: $score"
        }

        newScore.addView(holeNumber)
        newScore.addView(incrementButton)
        newScore.addView(decrementButton)
        newScore.addView(holeScore)
    }
    fun getStrokesForHoles(): MutableList<Int> {
        return scores;
    }
}