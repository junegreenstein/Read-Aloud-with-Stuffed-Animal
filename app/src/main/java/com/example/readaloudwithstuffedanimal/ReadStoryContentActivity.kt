package com.example.readaloudwithstuffedanimal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.util.ArrayList

class ReadStoryContentActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
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

        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)
        textPageNumber = findViewById(R.id.text_page_number)
        val storyTitle: TextView = findViewById(R.id.text_story_title)
        storyContent = findViewById(R.id.text_story_content)

        textToSpeech = TextToSpeech(this, this)
        val id = intent.getStringExtra(StoryContentActivity.STORY_ID)
        val sp = getSharedPreferences(id, Context.MODE_PRIVATE)
        val title = sp.getString(StoryTitleActivity.STORY_TITLE, null)

        val emptyList = Gson().toJson(ArrayList<String>())
        val key: String? = sp.getString(StoryContentActivity.STORY_CONTENT, null)
        val gson = Gson()
       // val json: String? = sp.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        content = gson.fromJson(key, type)

        //readText = content

        storyTitle.text = title
        //storyContent.text = content

        updatePage(true)

        btnNext.setOnClickListener {
            //storyContent.text = content
            updatePage(true)
        }

        btnPrevious.setOnClickListener {
            //storyContent.text = content
            updatePage(false)
        }
    }

    private fun read(text: String?) {
        textToSpeech!!.speak(readText, TextToSpeech.QUEUE_ADD, null,"")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            read(readText)
        }
    }

    private fun updatePage(nextPage: Boolean) {
        if (nextPage) {
            pageNumber++
        } else {
            pageNumber--
        }

        textPageNumber.text = pageNumber.toString()

        storyContent.text = content[pageNumber - 1]

        readText = content[pageNumber - 1]

        if (pageNumber == StoryContentActivity.FIRST_PAGE) {
            btnPrevious.visibility = View.GONE
        } else {
            btnPrevious.visibility = View.VISIBLE
        }
    }
}