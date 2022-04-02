package com.example.readaloudwithstuffedanimal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class StoryContentActivity : AppCompatActivity() {
    // Activity layout views.
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var editStoryContent: EditText
    private lateinit var textPageNumber: TextView

    // Page number.
    private var pageNumber: Int = 0

    // TODO: Create Story singleton class so that this data is accessible between Activities.
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
            // TODO: Send story to ReadStoryTitleActivity.
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
    }
}