package com.example.readaloudwithstuffedanimal

import android.content.Context
import android.graphics.*
import android.icu.util.Measure
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.readaloudwithstuffedanimal.DrawPictureActivity.Companion.paintBrush
import com.example.readaloudwithstuffedanimal.DrawPictureActivity.Companion.path

class PaintView : View {

    var params : ViewGroup.LayoutParams? = null

    companion object {
        var pathList = ArrayList<Path>()
        var colorList = ArrayList<Int>()
        var strokeList = ArrayList<Float>()

        var currentColor = Color.BLACK
        var currentStroke = 4f

        lateinit var bitmap : Bitmap
        lateinit var bitmapCanvas : Canvas
    }

    val bitmapPaint = Paint(Paint.DITHER_FLAG)


    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        paintBrush.isAntiAlias = true
        paintBrush.color = currentColor
        paintBrush.style = Paint.Style.STROKE
        paintBrush.strokeJoin = Paint.Join.ROUND
        paintBrush.strokeCap = Paint.Cap.ROUND

        viewTreeObserver.addOnGlobalLayoutListener {
            var parent = parent as View

            Log.i("STUFF", parent.width.toString() + "; " + parent.height.toString())

            bitmap = Bitmap.createBitmap(parent.width, parent.height, Bitmap.Config.ARGB_8888)
            bitmapCanvas = Canvas(bitmap)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y

        // Handles touching and dragging to draw, adds it to paths
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentColor)
                strokeList.add(currentStroke)
            }
            else -> return false
        }

        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        bitmapCanvas.drawColor(Color.WHITE)

        // Draw paths
        for (i in pathList.indices) {
            paintBrush.setColor(colorList[i])
            paintBrush.strokeWidth = strokeList[i]
            bitmapCanvas.drawPath(pathList[i], paintBrush)
        }

        canvas.drawBitmap(bitmap, 0f, 0f, bitmapPaint)
        canvas.restore()
    }
}