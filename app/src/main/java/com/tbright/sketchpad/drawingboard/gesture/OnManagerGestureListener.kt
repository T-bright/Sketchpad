package com.tbright.sketchpad.drawingboard.gesture

import android.view.MotionEvent

interface OnManagerGestureListener {
    fun onDown(e: MotionEvent): Boolean
    fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float)
}