package com.tbright.sketchpad.drawingboard.gesture

import android.content.Context
import android.util.Log
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.view.GestureDetectorCompat

class GestureDetectorManager(var context: Context) : SimpleOnGestureListener(), OnScaleGestureListener, OnTouchListener, OnRotateGestureListener {
    private val TAG = "GestureDetectorManager"
    private var onManagerGestureListener: OnManagerGestureListener? = null
    private var mGestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, this)
    private var mScaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, this)
    private var mRotateGestureDetector: RotateGestureDetector = RotateGestureDetector(this)

    fun setOnManagerGestureListener(onManagerGestureListener: OnManagerGestureListener?) {
        this.onManagerGestureListener = onManagerGestureListener
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        var onTouch = false
        onTouch = onTouch or mGestureDetector.onTouchEvent(event)
        onTouch = onTouch or mScaleGestureDetector.onTouchEvent(event)
        onTouch = onTouch or mRotateGestureDetector.onTouchEvent(event)
        return onTouch
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return v?.onTouchEvent(event) == true
    }

    //    1. 用户轻触触摸屏
    override fun onDown(e: MotionEvent): Boolean {
        onManagerGestureListener?.onDown(e)
        log("onDown")
        return super.onDown(e)
    }

    //2. 用户轻触触摸屏，尚未松开或拖动
    override fun onShowPress(e: MotionEvent?) {
        log("onShowPress")
        super.onShowPress(e)
    }

    // 3. 用户长按触摸屏
    override fun onLongPress(e: MotionEvent?) {
        log("onLongPress")
        super.onLongPress(e)
    }

    // 4. 用户轻击屏幕后抬起
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        log("onSingleTapUp")
        return super.onSingleTapUp(e)
    }


    // 5. 用户按下触摸屏 & 拖动
    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        if(e2.pointerCount == 1){
            log("onScroll :e1-->${e1.x}     e2-->${e2.x}")
            onManagerGestureListener?.onScroll(e1, e2, distanceX, distanceY)
        }

        return true
    }

    //6. 用户按下触摸屏、快速移动后松开
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        log("onFling")
        return super.onFling(e1, e2, velocityX, velocityY)
    }

    //单击事件
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        log("onSingleTapConfirmed")
        return super.onSingleTapConfirmed(e)
    }

    //双击事件
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        log("onDoubleTap")
        return super.onDoubleTap(e)
    }

    //双击间隔中发生的动作,指触发onDoubleTap后，在双击之间发生的其它动作，包含down、up和move事件；
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        log("onDoubleTapEvent")
        return super.onDoubleTapEvent(e)
    }

    //------------------------------------------scale-------------------------------------------------------
    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        log("onScaleBegin")
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        log("onScaleEnd")
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        log("onScale")
        return true
    }

    //------------------------------------------rotate-------------------------------------------------------
    override fun onRotateBegin() {
        log("onRotateBegin")
    }

    override fun onRotate(degrees: Float, focusX: Float, focusY: Float) {
        log("onRotate")
    }

    override fun onRotateEnd() {
        log("onRotateEnd")
    }


    private fun log(message: String) {
        Log.e(TAG, message)
    }
}