package com.example.readaloudwithstuffedanimal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.EditText
import androidx.core.content.ContextCompat

class ReadStoryContentActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var readText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_story_content)

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        textToSpeech = TextToSpeech(this, this)
        val sp = getPreferences(Context.MODE_PRIVATE)
        val content = sp.getString(StoryContentActivity.STORY_CONTENT, null)
        readText!!.setText(content)
    }

    private fun read(text: EditText?) {
        val text = readText!!.text
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_ADD, null,"")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            read(readText)
        }
    }
}
