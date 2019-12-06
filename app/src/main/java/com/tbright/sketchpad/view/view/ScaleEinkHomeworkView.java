package com.tbright.sketchpad.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ScaleEinkHomeworkView extends FrameLayout {
    private static final float MAX_SCALE = 2.0F;
    private static final float MIN_SCALE = 0.2f;

    private EinkHomeworkView einkHomeworkView;
    private float mOldDistance;
    private PointF mOldPointer;
    private float scaleFactor;
    private float[] mMatrixValus = new float[9];
    private float mBorderX, mBorderY;
    public ScaleEinkHomeworkView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScaleEinkHomeworkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScaleEinkHomeworkView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        einkHomeworkView = new EinkHomeworkView(context);
        addView(einkHomeworkView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(2400,3503.6f);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //是不是多手指触摸屏幕
    private boolean isMoreFingers = false;
    //记录位置
    private int mLastX;
    private int mLastY;
    /**
     * 是否拦截父容器的事件
     * isIntercept : true.拦截父容器（就是子类需要当前的事件，让父类不要拦截事件），false.不拦截父类（就是子类不需要当前的事件，让父类自行处理）
     */
    private void isInterceptParentTouchEvent(boolean isIntercept){
        getParent().requestDisallowInterceptTouchEvent(isIntercept);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                if (ev.getPointerCount() == 1 && (currentMode == EinkHomeworkView.CORRECT_MODE || currentMode == EinkHomeworkView.RASURE_MODE)) {
                    isInterceptParentTouchEvent(true);
                    return einkHomeworkView.onTouchEvent(ev);
                }

            case MotionEvent.ACTION_POINTER_DOWN:
                if(ev.getPointerCount() > 1){
                    mOldDistance = spacingOfTwoFinger(ev);
                    mOldPointer = middleOfTwoFinger(ev);
                    isMoreFingers = true;
                    isInterceptParentTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //一个手指，在EinkHomeworkView.MOVE_MODE情况下，滑动，否则事件给EinkHomeworkView
                if (!isMoreFingers) {
                    if (currentMode == EinkHomeworkView.MOVE_MODE) {//移动
                        einkHomeworkView.setX(einkHomeworkView.getX() + ev.getX() - mLastX);
                        einkHomeworkView.setY(einkHomeworkView.getY() + ev.getY() -mLastY);
                        mLastX = (int) ev.getX();
                        mLastY = (int) ev.getY();
                        isInterceptParentTouchEvent(false);
                        checkingBorder();
                    } else {
                        Log.e("AAA","ScaleEinkHomeworkView :ACTION_MOVE");
                        isInterceptParentTouchEvent(true);
                        return einkHomeworkView.onTouchEvent(ev);
                    }
                } else if(ev.getPointerCount() == 2){
                    //两个或以上手指的时候，放大、缩小、滑动
                    isInterceptParentTouchEvent(true);
                    try {
                        PointF mNewPointer = middleOfTwoFinger(ev);

                        float newDistance = spacingOfTwoFinger(ev);
                        scaleFactor = newDistance / mOldDistance;
                        scaleFactor = checkingScale(einkHomeworkView.getScaleX(), scaleFactor);
                        einkHomeworkView.setScaleX(einkHomeworkView.getScaleX() * scaleFactor);
                        einkHomeworkView.setScaleY(einkHomeworkView.getScaleY() * scaleFactor);
                        einkHomeworkView.setPivotX(mNewPointer.x);
                        einkHomeworkView.setPivotY(mNewPointer.y);
                        mOldDistance = newDistance;

                        einkHomeworkView.setX(einkHomeworkView.getX() + mNewPointer.x - mOldPointer.x);
                        einkHomeworkView.setY(einkHomeworkView.getY() + mNewPointer.y - mOldPointer.y);
                        mOldPointer = mNewPointer;
                        checkingBorder();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(ev.getPointerCount() == 1){
                    isMoreFingers = false;
                }
                Log.e("AAA","ScaleEinkHomeworkView :ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_UP:
                isMoreFingers = false;
                einkHomeworkView.getMatrix().getValues(mMatrixValus);
                einkHomeworkView.setScaleAndOffset(einkHomeworkView.getScaleX(), mMatrixValus[2], mMatrixValus[5]);
                isInterceptParentTouchEvent(false);
                break;
        }
        return true;
    }



    private int currentMode = EinkHomeworkView.CORRECT_MODE;

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        einkHomeworkView.setCurrentMode(currentMode);
    }

    /**
     * 计算两个触控点之间的距离
     *
     * @param event 触控事件
     * @return 触控点之间的距离
     */
    private float spacingOfTwoFinger(MotionEvent event) {
        if (event.getPointerCount() != 2) return 0;
        double dx = event.getX(0) - event.getX(1);
        double dy = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 获取两指之间的坐标
     * @param event 触控事件
     * @return
     */
    private PointF middleOfTwoFinger(MotionEvent event) {
        float mx = (event.getX(0) + event.getX(1)) / 2;
        float my = (event.getY(0) + event.getY(1)) / 2;
        PointF middle = new PointF(mx, my);
        return middle;
    }

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

    private void checkingBorder() {
        PointF offset = offsetBorder();
        einkHomeworkView.setX(einkHomeworkView.getX() + offset.x);
        einkHomeworkView.setY(einkHomeworkView.getY() + offset.y);
        if (einkHomeworkView.getScaleX() == 1) {
            einkHomeworkView.setX(0);
            einkHomeworkView.setY(0);
        }
    }

    private PointF offsetBorder() {
        PointF offset = new PointF(0, 0);
        if (einkHomeworkView.getScaleX() > 1) {
            einkHomeworkView.getMatrix().getValues(mMatrixValus);
            if (mMatrixValus[2] > -(mBorderX * (einkHomeworkView.getScaleX() - 1))) {
                offset.x = -(mMatrixValus[2] + mBorderX * (einkHomeworkView.getScaleX() - 1));
            }

            if (mMatrixValus[2] + einkHomeworkView.getWidth() * einkHomeworkView.getScaleX() - mBorderX * (einkHomeworkView.getScaleX() - 1) < getWidth()) {
                offset.x = getWidth() - (mMatrixValus[2] + einkHomeworkView.getWidth() * einkHomeworkView.getScaleX() - mBorderX * (einkHomeworkView.getScaleX() - 1));
            }

            if (mMatrixValus[5] > -(mBorderY * (einkHomeworkView.getScaleY() - 1))) {
                System.out.println("offsetY:" + mMatrixValus[5] + " borderY:" + mBorderY + " scale:" + getScaleY() + " scaleOffset:" + mBorderY * (getScaleY() - 1));
                offset.y = -(mMatrixValus[5] + mBorderY * (einkHomeworkView.getScaleY() - 1));
            }

            if (mMatrixValus[5] + einkHomeworkView.getHeight() * einkHomeworkView.getScaleY() - mBorderY * (einkHomeworkView.getScaleY() - 1) < getHeight()) {
                offset.y = getHeight() - (mMatrixValus[5] + einkHomeworkView.getHeight() * einkHomeworkView.getScaleY() - mBorderY * (einkHomeworkView.getScaleY() - 1));
            }
        }
        return offset;
    }
    public void pause(boolean isPause){
        einkHomeworkView.pause(isPause);
    }

    public void addBitmap(Bitmap examPaperBitmap, Bitmap studentAnswerBitmap, Bitmap teacherCorrectBitmap, int deviceWidth, int deviceHeight, float zoom) {
        einkHomeworkView.addBitmap(examPaperBitmap,studentAnswerBitmap,teacherCorrectBitmap,deviceWidth,deviceHeight,zoom);
    }
}
