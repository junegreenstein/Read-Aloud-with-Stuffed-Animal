package com.example.readaloudwithstuffedanimal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import android.widget.TextView
import android.content.Intent

class ReadStoryTitleActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var readText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_story_title)

        val btnNext: ImageButton = findViewById(R.id.btn_next)
        val storyTitle: TextView = findViewById(R.id.read_story_title)
        val authorName: TextView  = findViewById(R.id.read_author_name)

       textToSpeech = TextToSpeech(this, this)
        val id = intent.getStringExtra(StoryContentActivity.STORY_ID)
        val sp = getSharedPreferences(id, Context.MODE_PRIVATE)
        val title = sp.getString(StoryTitleActivity.STORY_TITLE, null)
        val author = sp.getString(StoryTitleActivity.AUTHOR_NAME, null)
        val preStoryTitle = " The title of the story is "
        val preStoryAuthor = " Written By "
        readText = preStoryTitle + title + preStoryAuthor + author
        storyTitle.text = title
        authorName.text = author

        btnNext.setOnClickListener {
            val intent = Intent(this, ReadStoryContentActivity::class.java)
            intent.putExtra(StoryTitleActivity.STORY_TITLE, title)
            intent.putExtra(StoryContentActivity.STORY_ID, id)
            startActivity(intent)
        }
    }

    private fun read() {
        textToSpeech!!.speak(readText, TextToSpeech.QUEUE_ADD, null,"")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            read()
        }
    }
}