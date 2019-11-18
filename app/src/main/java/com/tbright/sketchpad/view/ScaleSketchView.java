package com.tbright.sketchpad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScaleSketchView extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {

    private PathView pathView;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector mGestureDetector;

    public ScaleSketchView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScaleSketchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScaleSketchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        pathView = new PathView(context);
        addView(pathView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                pathView.setX(pathView.getX() + newPointer.x - mOldPointer.x);
//                pathView.setY(pathView.getY() + newPointer.y - mOldPointer.y);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return scaleGestureDetector.onTouchEvent(event) | mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor*detector.getCurrentSpan(),scaleFactor*detector.getCurrentSpan(),detector.getFocusX(),detector.getFocusY());
        matrix.postTranslate(detector.getCurrentSpanX(), detector.getCurrentSpanY());
        pathView.getMatrix().postConcat(matrix);
        pathView.invalidate();
//        pathView.setScaleX(pathView.getScaleX() * scaleFactor);
//        pathView.setScaleY(pathView.getScaleY() * scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


    public void setBackgroundBitmap(Bitmap resultBimtap) {
        pathView.setBackgroundBitmap(resultBimtap);
    }


}
