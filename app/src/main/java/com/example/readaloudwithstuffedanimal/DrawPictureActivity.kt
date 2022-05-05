package com.example.readaloudwithstuffedanimal

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.readaloudwithstuffedanimal.PaintView.Companion.bitmap
import com.example.readaloudwithstuffedanimal.PaintView.Companion.currentColor
import com.example.readaloudwithstuffedanimal.PaintView.Companion.currentStroke
import java.io.OutputStream

class DrawPictureActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    companion object {
        var path = Path()
        var paintBrush = Paint()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_layout)

        // Set status bar color.
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        supportActionBar?.hide()

        // Setup color swapping buttons
        val blackBtn = findViewById<ImageButton>(R.id.blackColor)
        val redBtn = findViewById<ImageButton>(R.id.redColor)
        val orangeBtn = findViewById<ImageButton>(R.id.orangeColor)
        val yellowBtn = findViewById<ImageButton>(R.id.yellowColor)
        val greenBtn = findViewById<ImageButton>(R.id.greenColor)
        val blueLightBtn = findViewById<ImageButton>(R.id.blueLightColor)
        val blueDarkBtn = findViewById<ImageButton>(R.id.blueDarkColor)
        val purpleBtn = findViewById<ImageButton>(R.id.purpleColor)
        val whiteBtn = findViewById<ImageButton>(R.id.whiteColor)

        blackBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.black)
            setCurrentColor(paintBrush.color)
        }
        redBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.red)
            setCurrentColor(paintBrush.color)
        }
        orangeBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.orange)
            setCurrentColor(paintBrush.color)
        }
        yellowBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.yellow)
            setCurrentColor(paintBrush.color)
        }
        greenBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.green)
            setCurrentColor(paintBrush.color)
        }
        blueLightBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.blue_light)
            setCurrentColor(paintBrush.color)
        }
        blueDarkBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.blue_dark)
            setCurrentColor(paintBrush.color)
        }
        purpleBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.purple)
            setCurrentColor(paintBrush.color)
        }
        whiteBtn.setOnClickListener {
            paintBrush.color = ContextCompat.getColor(applicationContext, R.color.white)
            setCurrentColor(paintBrush.color)
        }

        // Setup size switching slider
        val sizePicker = findViewById<SeekBar>(R.id.sizePicker)
        sizePicker.setOnSeekBarChangeListener(this)

        val doneBtn = findViewById<Button>(R.id.doneBtn)
        doneBtn.setOnClickListener {
            val cv = ContentValues()

            val id = intent.getStringExtra(StoryContentActivity.STORY_ID)
            // file name
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, id + "_cover.png")

            // file type
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")

            // file location
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)

            try {
                val imageOutStream = contentResolver.openOutputStream(uri!!)

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)

                imageOutStream?.close()

                val intent = Intent(this, ReadStoryTitleActivity::class.java)
                intent.putExtra(StoryContentActivity.STORY_ID, id)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            } catch (e: Exception) {
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Change color
    private fun setCurrentColor(color: Int) {
        currentColor = color
        path = Path()
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        currentStroke = p1 / 10f
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        // When user first touches seekbar
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        path = Path()
    }
}