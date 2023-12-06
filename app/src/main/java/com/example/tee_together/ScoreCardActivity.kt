package com.example.tee_together //LOOK AT THE COMMENTS AROUND LINE 40-50

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
import android.widget.CheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScoreCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard)
        var scorecardHandler = ScoreCardHandler()
        val addHole = findViewById<ImageButton>(R.id.add_hole)
        val containerScores = findViewById<LinearLayout>(R.id.scores_for_holes)
        val changeToResult = findViewById<ImageButton>(R.id.change_to_result_scorecard)

        val scorecardButton: ImageButton = findViewById<ImageButton>(R.id.scorecardButton)
        scorecardButton.setOnClickListener {
            val intent = Intent(this, ScoreCardActivity::class.java)
            startActivity(intent)
        }
        val profileButton: ImageButton = findViewById<ImageButton>(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        addHole.setOnClickListener {
            scorecardHandler.createNewHole(containerScores, this)
        }
        changeToResult.setOnClickListener(View.OnClickListener {
            val name = intent.getStringExtra("username")
            if (scorecardHandler.getStrokesForHoles().isNotEmpty()) { //Same here
                val intent = Intent(this, ScoreCardResultActivity::class.java)
                intent.putExtra("player_names", name)
                intent.putExtra( //idk if this accepts a list of HoleData class objects
                    //possible solution is just splitting up the HoleData stuff into returning three separate lists and returning those
                    //i don't want to fuck it up and someone probably knows better than me how putExtra works
                    "strokes_per_holes",
                    scorecardHandler.getStrokesForHoles() //I think this needs to be the full HoleData array holes
                )
                startActivity(intent)
            }
        })
    }
}
class ScoreCardHandler{
    private var holeCount = 0
    private var holes = mutableListOf<HoleData>()

    fun createNewHole(container: LinearLayout, context: Context){
        holeCount += 1
        val newScore = LinearLayout(context)
        newScore.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        newScore.orientation = LinearLayout.HORIZONTAL
        newScore.setBackgroundResource(R.drawable.cell_shape)
        container.addView(newScore)

        val firCheckbox = CheckBox(context)
        firCheckbox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        firCheckbox.text = "FIR"
        firCheckbox.setPadding(16)

        val girCheckbox = CheckBox(context)
        girCheckbox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        girCheckbox.text = "GIR"
        girCheckbox.setPadding(16)

        newScore.addView(firCheckbox)
        newScore.addView(girCheckbox)

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
        val currScore = holes[holeCount - 1].score
        holeScore.text = "Score: $currScore"
        val index = holeCount - 1
        decrementButton.setOnClickListener {
            if (holes[index].score > 0){
                holes[index].score -= 1
            }
            val score = holes[index].score
            holeScore.text = "Score: $score"
        }
        incrementButton.setOnClickListener {
            holes[index].score += 1
            val score = holes[index].score
            holeScore.text = "Score: $score"
        }

        newScore.addView(holeNumber)
        newScore.addView(incrementButton)
        newScore.addView(decrementButton)
        newScore.addView(holeScore)
    }
    fun getStrokesForHoles(): MutableList<HoleData> {
        return holes;
    }
}