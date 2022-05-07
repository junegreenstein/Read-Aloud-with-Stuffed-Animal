package com.example.readaloudwithstuffedanimal

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.util.*


class ReadStoryTitleActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var readText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_story_title)

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Activity layout views.
        val btnNext: ImageButton = findViewById(R.id.btn_next)
        val storyTitle: TextView = findViewById(R.id.read_story_title)
        val authorName: TextView = findViewById(R.id.read_author_name)
        val coverArt: ImageView = findViewById(R.id.coverArtImage)

        // Initialize TextToSpeech class variable.
        textToSpeech = TextToSpeech(this, this, "com.google.android.tts")

        // Get title and author from SharedPreferences.
        val id = intent.getStringExtra(StoryContentActivity.STORY_ID)
        val sp = getSharedPreferences(id, Context.MODE_PRIVATE)
        val title = sp.getString(StoryTitleActivity.STORY_TITLE, null)
        val author = sp.getString(StoryTitleActivity.AUTHOR_NAME, null)

        // Get cover art.
        val path =
            getExternalFilesDir(null)!!.absolutePath
        val myPath = path + "/" + id + "_cover.png"

        val coverFile = File(myPath)

        if (coverFile.exists()) {
            val imageInStream = FileInputStream(coverFile)
            val bitmap = BitmapFactory.decodeStream(imageInStream)
            coverArt.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Cover not found", Toast.LENGTH_SHORT).show()
        }

        // Display title and author in layout.
        storyTitle.text = title
        authorName.text = author

        // Set text to be read.
        val preStoryTitle = getString(R.string.read_title)
        val preStoryAuthor = getString(R.string.read_written_by)
        readText = "$preStoryTitle $title ... $preStoryAuthor $author"

        // Next button listener.
        btnNext.setOnClickListener {
            // Package the intent.
            val intent = Intent(this, ReadStoryContentActivity::class.java)
            intent.putExtra(StoryTitleActivity.STORY_TITLE, title)
            intent.putExtra(StoryContentActivity.STORY_ID, id)

            textToSpeech!!.stop()

            // Launch next activity.
            startActivity(intent)
        }
    }

    // Indicate completion of initialization.
    override fun onInit(status: Int) {
        // Check if initialization succeeded.
        if (status == TextToSpeech.SUCCESS) {
            // Set the voice of TTS.
            textToSpeech!!.language = Locale.US

            var selected: Voice? = null
            for (v in textToSpeech!!.voices) {
                if (v.name == "en-us-x-iog-local") {
                    selected = v
                }
            }

            textToSpeech!!.voice = selected!!
            read()
        }
    }

    // Read out the text.
    private fun read() {
        textToSpeech!!.speak(readText, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Stop and shutdown TextToSpeech engine when activity is destroyed.
    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }

    // Go to Select Story screen
    override fun onBackPressed() {
        val intent = Intent(this, SelectStoryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}