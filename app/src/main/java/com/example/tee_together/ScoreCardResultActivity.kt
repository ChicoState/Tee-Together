package com.example.tee_together

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.bottomappbar.BottomAppBar

class ScoreCardResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard_result)
        val tableLayoutContainer = findViewById<TableLayout>(R.id.scorecard_table)
        var scoreCardResultHandler = ScoreCardResultHandler()
        scoreCardResultHandler.createResult(intent.getStringExtra("player_names"), intent.getIntArrayExtra("strokes_per_holes"), tableLayoutContainer, this)
        // For the user, push this data back onto firebase for this game
        val navigateBack = findViewById<BottomAppBar>(R.id.bottomAppBarScorecardResult)
        navigateBack.setOnClickListener{
            finish()
        }
    }
}

class ScoreCardResultHandler(){
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
        player.text="$playerNames"
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