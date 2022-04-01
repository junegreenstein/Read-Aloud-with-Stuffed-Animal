package com.example.readaloudwithstuffedanimal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class StoryTitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_title)

        val btnNext: ImageButton = findViewById(R.id.btn_next)
        btnNext.setOnClickListener {
            val intent = Intent(this, StoryContentActivity::class.java)
            startActivity(intent)
        }
    }


}