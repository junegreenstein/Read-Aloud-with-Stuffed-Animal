package com.example.readaloudwithstuffedanimal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import java.io.File

class StoryContentActivity : AppCompatActivity() {
    // Activity layout views.
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var editStoryContent: EditText
    private lateinit var textPageNumber: TextView

    // Page number.
    private var pageNumber: Int = 0

    // Story array.
    private val story = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_content)

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Get story title and author name from previous Activity.
        val storyTitle: String = intent.getStringExtra(StoryTitleActivity.STORY_TITLE).toString()
        val authorName: String = intent.getStringExtra(StoryTitleActivity.AUTHOR_NAME).toString()

        // Set story title in Activity layout.
        val textStoryTitle: TextView = findViewById(R.id.text_story_title)
        textStoryTitle.text = storyTitle

        // Activity layout views.
        val btnFinish: Button = findViewById(R.id.btn_finish)
        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)
        editStoryContent = findViewById(R.id.edit_story_content)
        textPageNumber = findViewById(R.id.text_page_number)

        // Update current page.
        updatePage(true)

        // Next button listener.
        btnNext.setOnClickListener {
            // Get story content.
            val storyContent: String = editStoryContent.text.toString()

            // Ensure story content is not blank.
            if (storyContent.isNotBlank()) {
                // Add the page.
                if (story.size == pageNumber - 1) {
                    story.add(storyContent.trim())
                } else {
                    story[pageNumber - 1] = storyContent.trim()
                }

                // Update the next page.
                updatePage(true)
            } else {
                Toast.makeText(this, R.string.invalid_story_content, Toast.LENGTH_SHORT).show()
            }
        }

        // Previous button listener.
        btnPrevious.setOnClickListener {
            // Get story content.
            val storyContent: String = editStoryContent.text.toString()

            // Ensure story content is not blank.
            if (storyContent.isNotBlank()) {
                // Add the page.
                if (story.size == pageNumber - 1) {
                    story.add(storyContent.trim())
                } else {
                    story[pageNumber - 1] = storyContent.trim()
                }
            }

            // Update the next page.
            updatePage(false)
        }

        // Finish story button listener.
        btnFinish.setOnClickListener {
            // Get story content.
            val storyContent: String = editStoryContent.text.toString()

            // Ensure story content is not blank.
            if (storyContent.isNotBlank()) {
                // Add the page.
                if (story.size == pageNumber - 1) {
                    story.add(storyContent.trim())
                } else {
                    story[pageNumber - 1] = storyContent.trim()
                }
            }

            Log.i("TAG", story.toString())

            // Set unique story ID and write to internal storage.
            // Create directory.
            val storyIDs = File(this.filesDir, "storyIDs")
            storyIDs.mkdirs()

            // Create file if it does not exist.
            val file = File(storyIDs, "storyIDs.txt")
            if (!file.exists())
                file.createNewFile()

            // Set story ID.
            val lines: List<String> = file.readText().trim().split(" ")

            var storyID = "0"

            // Increment story id.
            if (lines.size - 1 != 0 || !lines[0].equals("")) {

                storyID = (lines[lines.size - 1].toInt() + 1).toString()

            }

            // Write to file.
            file.appendText("$storyID ")


            // Save story title and author name to SharedPreferences.
            val sharedPreferences = getSharedPreferences(storyID, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(StoryTitleActivity.STORY_TITLE, storyTitle)
            editor.putString(StoryTitleActivity.AUTHOR_NAME, authorName)

            // Save story content to SharedPreferences.
            val gson = Gson()
            editor.putString(STORY_CONTENT, gson.toJson(story))
            editor.apply()

            val intent = Intent(this, DrawPictureActivity::class.java)
            intent.putExtra(STORY_ID, storyID)
            startActivity(intent)
        }
    }

    private fun updatePage(nextPage: Boolean) {
        // Increment page number and update display.
        if (nextPage) pageNumber++ else pageNumber--
        textPageNumber.text = pageNumber.toString()

        // Reset story content.
        if (story.size >= pageNumber) {
            val storyContent: String = story[pageNumber - 1]
            editStoryContent.setText(storyContent)
        } else {
            editStoryContent.setText("")
        }

        // Hide previous button if on the first page. Display it otherwise.
        if (pageNumber == FIRST_PAGE) {
            btnPrevious.visibility = View.GONE
        } else {
            btnPrevious.visibility = View.VISIBLE
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val STORY_CONTENT = "STORY_CONTENT"
        const val STORY_ID = "STORY_ID"
    }
}