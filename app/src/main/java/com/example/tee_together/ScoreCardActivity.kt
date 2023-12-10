package com.example.tee_together //LOOK AT THE COMMENTS AROUND LINE 40-50

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import android.widget.CheckBox
import android.widget.EditText
import java.io.Serializable
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
                    "hole_data",
                    ArrayList(scorecardHandler.getStrokesForHoles()) //I think this needs to be the full HoleData array holes
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
        holes.add(HoleData(0, 0,false, false))
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

        val parEditText = EditText(context)
        parEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        parEditText.hint = "Par"
        parEditText.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        parEditText.setPadding(16)

        val holeNumber = TextView(context)
        holeNumber.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holeNumber.setPadding(16)
        Log.d("Hello", "hello $holeCount")
        val holeToPrint = holeCount + 1
        holeNumber.text = "Hole $holeToPrint"
        holeCount+=1;
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
        val currScore = if (holes.isNotEmpty()) holes[holeCount - 1].score else 0
        //holeCount+=1;
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
        newScore.addView(firCheckbox)
        newScore.addView(girCheckbox)
        newScore.addView(parEditText)
        newScore.addView(decrementButton)
        newScore.addView(incrementButton)
        newScore.addView(holeScore)

        firCheckbox.setOnCheckedChangeListener { _, isChecked ->
            holes[index].fir = isChecked
        }

        girCheckbox.setOnCheckedChangeListener { _, isChecked ->
            holes[index].gir = isChecked
        }

        parEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val par = s.toString().toIntOrNull() ?: 0
                holes[holeCount - 1].par = par
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //not needed but you have to define these for some reason
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //not needed
            }
        })

    }
    fun getStrokesForHoles(): MutableList<HoleData> {
        return holes
    }
}