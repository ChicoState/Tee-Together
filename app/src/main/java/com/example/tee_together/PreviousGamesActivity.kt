package com.example.tee_together

import android.os.Bundle
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity


class PreviousGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard_result)
        val tableLayoutContainer = findViewById<TableLayout>(R.id.scorecard_table)
        var scoreCardResultHandler = ScoreCardResultHandler()
        scoreCardResultHandler.createResult(intent.getStringExtra("player_names"), intent.getIntArrayExtra("strokes_per_holes"), tableLayoutContainer, this)
    }
}