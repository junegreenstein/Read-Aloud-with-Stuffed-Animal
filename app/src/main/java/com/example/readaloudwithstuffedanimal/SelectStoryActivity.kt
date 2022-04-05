package com.example.readaloudwithstuffedanimal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SelectStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_story)

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Activity layout views.
        val fab: FloatingActionButton = findViewById(R.id.fab)

        // Floating action button listener.
        fab.setOnClickListener {
            val intent = Intent(this, StoryTitleActivity::class.java)
            startActivity(intent)
        }

        // TODO: Populate recycler view with saved stories from SharedPreferences.
    }
}