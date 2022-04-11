package com.example.readaloudwithstuffedanimal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.EditText
import java.io.File

class ReadStoryTitleActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var readText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_story_title)

       textToSpeech = TextToSpeech(this, this)
        val sp = getPreferences(Context.MODE_PRIVATE)
        val title = sp.getString(StoryTitleActivity.STORY_TITLE, null)
        val author = sp.getString(StoryTitleActivity.AUTHOR_NAME, null)
        val ta = title + author
        readText!!.setText(ta)
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
