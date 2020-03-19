package com.tbright.sketchpad.drawingboard.gesture

import android.view.MotionEvent
import com.tbright.sketchpad.drawingboard.gesture.OnRotateGestureListener

class RotateGestureDetector(var onRotateGestureListener: OnRotateGestureListener) {

    fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

}