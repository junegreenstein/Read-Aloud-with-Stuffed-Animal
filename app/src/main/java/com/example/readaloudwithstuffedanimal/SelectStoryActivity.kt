package com.example.readaloudwithstuffedanimal

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class SelectStoryActivity : AppCompatActivity() {

    private lateinit var storyList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_story)

        // Check user permissions.
        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                ReadStoryTitleActivity.PERMISSIONS,
                ReadStoryTitleActivity.REQUEST_CODE
            )
        }

        // Check if the app is freshly installed.
        val storyIDs = File(this.filesDir, "storyIDs")
        val file = File(storyIDs, "storyIDs.txt")

        // If so, remove old cover images.
        if (!file.exists()) {
            val images =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            // TODO: Delete old cover images.
        }

        // Disable toolbar.
        supportActionBar?.hide()

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Activity layout views.
        val fab: FloatingActionButton = findViewById(R.id.fab)
        storyList = findViewById(R.id.storyList)

        // Floating action button listener.
        fab.setOnClickListener {
            val intent = Intent(this, StoryTitleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Get story id file
        val storyIDs = File(this.filesDir, "storyIDs")

        val titles: MutableList<String> = mutableListOf()
        val authors: MutableList<String> = mutableListOf()
        val ids: MutableList<String> = mutableListOf()

        val file = File(storyIDs, "storyIDs.txt")
        // Get story ids to populate old stories if the file exists (i.e. an old story has been made before)
        if (file.exists()) {
            val lines: List<String> = file.readText().trim().split(" ")
            for (id in lines) {
                val sharedPrefs = getSharedPreferences(id, Context.MODE_PRIVATE)
                if (sharedPrefs.contains(StoryTitleActivity.STORY_TITLE)) {
                    titles.add(sharedPrefs.getString(StoryTitleActivity.STORY_TITLE, "")!!)
                }
                if (sharedPrefs.contains(StoryTitleActivity.AUTHOR_NAME)) {
                    authors.add(sharedPrefs.getString(StoryTitleActivity.AUTHOR_NAME, "")!!)
                }
                ids.add(id)
            }
        }

        // Create custom Adapter and ViewHolder
        class StoryListAdapter :
            RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val title: TextView = itemView.findViewById(R.id.storyItemTitle)
                val author: TextView = itemView.findViewById(R.id.storyItemAuthor)
                var id: String? = null

                init {
                    itemView.setOnClickListener {
                        intent =
                            Intent(this@SelectStoryActivity, ReadStoryTitleActivity::class.java)
                        intent.putExtra(StoryTitleActivity.STORY_TITLE, title.text)
                        intent.putExtra(StoryContentActivity.STORY_ID, id)
                        startActivity(intent)
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val itemView =
                    LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)

                return ViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.title.text = titles[position]
                holder.author.text = authors[position]
                holder.id = ids[position]
            }

            override fun getItemCount(): Int {
                return titles.size
            }
        }

        // Setup RecyclerView
        storyList.layoutManager = LinearLayoutManager(this)
        storyList.adapter = StoryListAdapter()
    }
}