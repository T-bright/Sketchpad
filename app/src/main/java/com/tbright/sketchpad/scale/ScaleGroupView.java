package com.tbright.sketchpad.scale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tbright.sketchpad.R;

import static com.tbright.sketchpad.view.scaledrawinboard.TouchEventUtil.middleOfTwoFinger;
import static com.tbright.sketchpad.view.scaledrawinboard.TouchEventUtil.spacingOfTwoFinger;

public class ScaleGroupView extends FrameLayout {
    public ScaleGroupView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScaleGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScaleGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ImageView imageView = new ImageView(context);
        addView(imageView);
        Bitmap srcBitmapTeacherCorrect = BitmapFactory.decodeResource(getResources(), R.mipmap.aab);
        imageView.setImageBitmap(srcBitmapTeacherCorrect);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = 411;
        layoutParams.height = 600;
        imageView.setLayoutParams(layoutParams);
    }
    //记录位置
    private int mLastX;
    private int mLastY;
    private float mOldDistance;
    private PointF mOldPointer;
    //是不是多手指触摸屏幕
    private boolean isMoreFingers = false;
    private float scaleFactor;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (ev.getPointerCount() > 1) {
                    mOldDistance = spacingOfTwoFinger(ev);
                    mOldPointer = middleOfTwoFinger(ev);
                    isMoreFingers = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMoreFingers) {
//                    setX(getX() + ev.getRawX() - mLastX);
//                    setY(getY() + ev.getRawY() - mLastY);
//                    scrollBy((int)(ev.getRawX() - mLastX),(int)(ev.getRawY() - mLastY));
                    scrollTo((int)(getX() + ev.getX() - mLastX),(int)(getY() + ev.getY() - mLastY));
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                } else if (ev.getPointerCount() == 2) {
                    //两个或以上手指的时候，放大、缩小、滑动
                    try {
                        PointF mNewPointer = middleOfTwoFinger(ev);
                        float newDistance = spacingOfTwoFinger(ev);
                        scaleFactor = newDistance / mOldDistance;
                        scaleFactor = checkingScale(getScaleX(), scaleFactor);
                        setScaleX(getScaleX() * scaleFactor);
                        setScaleY(getScaleY() * scaleFactor);
                        setPivotX(mNewPointer.x);
                        setPivotY(mNewPointer.y);
//                        mOuterMatrix.postScale(scaleFactor,scaleFactor,mNewPointer.x,mNewPointer.y);
                        mOldDistance = newDistance;

                        setX(getX() + mNewPointer.x - mOldPointer.x);
                        setY(getY() + mNewPointer.y - mOldPointer.y);
                        mOldPointer = mNewPointer;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
    private static final float MAX_SCALE = 2.0F;
    private static final float MIN_SCALE = 0.2f;
    private float checkingScale(float scale, float scaleFactor) {
        if ((scale <= MAX_SCALE && scaleFactor > 1.0) || (scale >= MIN_SCALE && scaleFactor < 1.0)) {
            if (scale * scaleFactor < MIN_SCALE) {
                scaleFactor = MIN_SCALE / scale;
            }
            if (scale * scaleFactor > MAX_SCALE) {
                scaleFactor = MAX_SCALE / scale;
            }
        }
        return scaleFactor;
    }
}
