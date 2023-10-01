package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.hello_world)
        text.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ScoreCardActivity::class.java)
            startActivity(intent)
        })
    }
}