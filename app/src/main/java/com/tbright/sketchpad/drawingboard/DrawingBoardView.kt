package com.tbright.sketchpad.drawingboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.tbright.sketchpad.drawingboard.gesture.GestureDetectorManager
import com.tbright.sketchpad.drawingboard.gesture.OnManagerGestureListener
import java.util.*

class DrawingBoardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr), OnManagerGestureListener {
    private val TAG = "DrawingBoardView"
    private var gestureDetectorManager: GestureDetectorManager = GestureDetectorManager(context)
    private var mPathList = LinkedList<Path>()
    private var mCorrectPaint: Paint? = null

    init {
        gestureDetectorManager.setOnManagerGestureListener(this)
        //老师批阅的时的批注画笔
        mCorrectPaint = Paint()
        mCorrectPaint?.run {
            isAntiAlias = true
            color = Color.parseColor("#ff0000")
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            style = Paint.Style.STROKE
            strokeWidth = 30f
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetectorManager.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(mPathList.isNotEmpty()){
            canvas.drawPath(mPathList.first,mCorrectPaint!!)
        }
    }

    private var preX = 0f
    private var preY = 0f

    override fun onDown(e: MotionEvent): Boolean {
        preX = e.x
        preY = e.y
        var drawPath = Path()
        mPathList.add(drawPath)
        drawPath.moveTo(preX, preY)
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float) {
        mPathList.first.quadTo(preX, preY, e2.x, e2.y)
        preX = e2.x
        preY = e2.y
        log("${e2.x}-----${e2.y}")
        invalidate()
    }
    private fun log(message: String) {
//        Log.e(TAG, message)
    }
}