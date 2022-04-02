package com.example.readaloudwithstuffedanimal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat

class StoryTitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_title)

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Activity layout views.
        val btnNext: ImageButton = findViewById(R.id.btn_next)
        val editStoryTitle: EditText = findViewById(R.id.edit_story_title)
        val editAuthorName: EditText = findViewById(R.id.edit_author_name)

        // Next Activity listener.
        btnNext.setOnClickListener {
            // Get story title and author name.
            val storyTitle: String = editStoryTitle.text.toString()
            val authorName: String = editAuthorName.text.toString()

            // Ensure story title and author name are not blank.
            if (storyTitle.isNotBlank() && authorName.isNotBlank()) {
                // Package the intent.
                val intent = Intent(this, StoryContentActivity::class.java)
                intent.putExtra(STORY_TITLE, storyTitle.trim())
                intent.putExtra(AUTHOR_NAME, authorName.trim())

                // Launch next Activity.
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.invalid_story_title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val STORY_TITLE = "STORY_TITLE"
        const val AUTHOR_NAME = "AUTHOR_NAME"
    }
}