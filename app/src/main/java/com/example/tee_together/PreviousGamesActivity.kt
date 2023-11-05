package com.example.tee_together

import android.os.Bundle
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PreviousGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard_result)
        val tableLayoutContainer = findViewById<TableLayout>(R.id.scorecard_table)
        var scoreCardResultHandler = ScoreCardResultHandler()
        scoreCardResultHandler.createResult(intent.getStringExtra("player_names"), intent.getIntArrayExtra("strokes_per_holes"), tableLayoutContainer, this)
    }
}

class PreviousGameBuilder(){
    fun build(){
        val db = FirebaseFirestore.getInstance()
        var auth = FirebaseAuth.getInstance()
        var uid = auth.uid.toString()

        db.collection("users").document(uid).collection("games").get()
    }
}