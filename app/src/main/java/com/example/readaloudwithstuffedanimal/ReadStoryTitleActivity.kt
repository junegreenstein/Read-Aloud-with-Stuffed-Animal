package com.example.readaloudwithstuffedanimal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ReadStoryTitleActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var readText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_story_title)

        // Activity layout views.
        val btnNext: ImageButton = findViewById(R.id.btn_next)
        val storyTitle: TextView = findViewById(R.id.read_story_title)
        val authorName: TextView = findViewById(R.id.read_author_name)

        // Initialize TextToSpeech class variable.
        textToSpeech = TextToSpeech(this, this, "com.google.android.tts")

        // Get title and author from SharedPreferences.
        val id = intent.getStringExtra(StoryContentActivity.STORY_ID)
        val sp = getSharedPreferences(id, Context.MODE_PRIVATE)
        val title = sp.getString(StoryTitleActivity.STORY_TITLE, null)
        val author = sp.getString(StoryTitleActivity.AUTHOR_NAME, null)

        // Display title and author in layout.
        storyTitle.text = title
        authorName.text = author

        // Set text to be read.
        val preStoryTitle = getString(R.string.read_title)
        val preStoryAuthor = getString(R.string.read_written_by)
        readText = preStoryTitle + " " + title + " ... " + preStoryAuthor + " " + author

        // Next button listener.
        btnNext.setOnClickListener {
            // Package the intent.
            val intent = Intent(this, ReadStoryContentActivity::class.java)
            intent.putExtra(StoryTitleActivity.STORY_TITLE, title)
            intent.putExtra(StoryContentActivity.STORY_ID, id)

            textToSpeech!!.stop()

            // Launch next Activity.
            startActivity(intent)
        }
    }

    // Indicate completion of initialization.
    override fun onInit(status: Int) {
        // Check if initialization succeeded.
        if (status == TextToSpeech.SUCCESS) {
            // Set the voice of TTS
            textToSpeech!!.setLanguage(Locale.US)

            var selected : Voice? = null
            for (v in textToSpeech!!.voices) {
                if (v.name == "en-us-x-iog-local") {
                    selected = v
                }
            }

            textToSpeech!!.setVoice(selected!!)

            read()
        }
    }

    // Read out the text.
    private fun read() {
        textToSpeech!!.speak(readText, TextToSpeech.QUEUE_FLUSH, null, "UNIQUE_UTTERANCE_ID")
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