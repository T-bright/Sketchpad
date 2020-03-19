package com.tbright.sketchpad.drawingboard.gesture

interface OnRotateGestureListener {
    fun onRotateBegin()

    fun onRotate(degrees: Float, focusX: Float, focusY: Float)

    fun onRotateEnd()
}