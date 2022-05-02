package com.example.readaloudwithstuffedanimal

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ReadStoryContentActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var readText: String? = null
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var textPageNumber: TextView
    private lateinit var storyContent: TextView
    private lateinit var content: ArrayList<String>
    private var pageNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_story_content)

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Activity layout views.
        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)
        textPageNumber = findViewById(R.id.text_page_number)
        val storyTitle: TextView = findViewById(R.id.text_story_title)
        storyContent = findViewById(R.id.text_story_content)

        // Initialize TextToSpeech class variable.
        textToSpeech = TextToSpeech(this, this)

        // Get story title and content from SharedPreferences.
        val id = intent.getStringExtra(StoryContentActivity.STORY_ID)
        val sp = getSharedPreferences(id, Context.MODE_PRIVATE)
        val title = sp.getString(StoryTitleActivity.STORY_TITLE, null)
        val json: String? = sp.getString(StoryContentActivity.STORY_CONTENT, null)
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        content = gson.fromJson(json, type)

        // Display title in layout.
        storyTitle.text = title

        // Update current page.
        updatePage(true)

        // Next button listener.
        btnNext.setOnClickListener {
            // Update the next page.
            updatePage(true)
        }

        // Previous button listener.
        btnPrevious.setOnClickListener {
            // Update the next page.
            updatePage(false)
        }
    }

    // Indicate completion of initialization.
    override fun onInit(status: Int) {
        // Check if initialization succeeded.
        if (status == TextToSpeech.SUCCESS) {
            read()
        }
    }

    // Read out the text.
    private fun read() {
        textToSpeech!!.speak(readText, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Manage what is displayed on each page.
    private fun updatePage(nextPage: Boolean) {
        // Increment page number and update display.
        if (nextPage) {
            pageNumber++
        } else {
            pageNumber--
        }

        textPageNumber.text = pageNumber.toString()

        // Display the content of each page of the story.
        if (pageNumber == content.size) {
            storyContent.text = content[pageNumber - 1]
            readText = content[pageNumber - 1] + " The End "
        } else {
            storyContent.text = content[pageNumber - 1]
            readText = content[pageNumber - 1]
        }

        // Let each page be read.
        textToSpeech = TextToSpeech(this, this)

        // Manage the visibility of the next and previous buttons based on page.
        if (pageNumber == StoryContentActivity.FIRST_PAGE && content.size == 1) {
            btnPrevious.visibility = View.GONE
            btnNext.visibility = View.GONE
        } else if (pageNumber == StoryContentActivity.FIRST_PAGE) {
            btnPrevious.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
        } else if (pageNumber == content.size) {
            btnPrevious.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
        } else {
            btnPrevious.visibility = View.VISIBLE
            btnNext.visibility = View.VISIBLE
        }
    }

    // Stop and shutdown TextToSpeech engine when activity is destroyed.
    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }
}