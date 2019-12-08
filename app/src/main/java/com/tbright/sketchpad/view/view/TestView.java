package com.tbright.sketchpad.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.tbright.sketchpad.R;
import com.tbright.sketchpad.listactivity.bean.EinkHomeworkViewBean;

import static com.tbright.sketchpad.view.scaledrawinboard.TouchEventUtil.middleOfTwoFinger;
import static com.tbright.sketchpad.view.scaledrawinboard.TouchEventUtil.spacingOfTwoFinger;

public class TestView extends FrameLayout {

    private ImageView ivBg;
    private ImageView ivCanvas;

    private float mBorderX, mBorderY;
    private static final float MAX_SCALE = 2.0F;
    private static final float MIN_SCALE = 0.2f;

    public TestView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TestView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 是否拦截父容器的事件
     * isIntercept : true.拦截父容器（就是子类需要当前的事件，让父类不要拦截事件），false.不拦截父类（就是子类不需要当前的事件，让父类自行处理）
     */
    private void isInterceptParentTouchEvent(boolean isIntercept) {
        getParent().requestDisallowInterceptTouchEvent(isIntercept);
    }

    //记录位置
    private int mLastX;
    private int mLastY;
    private int currentMode = EinkHomeworkView.MOVE_MODE;

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        if(einkHomeworkView != null){
            einkHomeworkView.setCurrentMode(currentMode);
        }
    }

    private float mOldDistance;
    private PointF mOldPointer;
    //是不是多手指触摸屏幕
    private boolean isMoreFingers = false;
    private float scaleFactor;
//    private Matrix mOuterMatrix = new Matrix();
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.concat(mOuterMatrix);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                if (ev.getPointerCount() == 1 && (currentMode == EinkHomeworkView.CORRECT_MODE || currentMode == EinkHomeworkView.RASURE_MODE)) {
                    isInterceptParentTouchEvent(true);
                    if(einkHomeworkView != null){
                        return einkHomeworkView.onTouchEvent(ev);
                    }else {
                        return false;
                    }
                }
            case MotionEvent.ACTION_POINTER_DOWN:
                if (ev.getPointerCount() > 1) {
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
                        setX(getX() + ev.getX() - mLastX);
                        setY(getY() + ev.getY() - mLastY);
                        mLastX = (int) ev.getX();
                        mLastY = (int) ev.getY();
                        isInterceptParentTouchEvent(false);
                        checkingBorder();
                    } else {
                        isInterceptParentTouchEvent(true);
                        if(einkHomeworkView != null){
                            return einkHomeworkView.onTouchEvent(ev);
                        }else {
                            return false;
                        }
                    }
                } else if (ev.getPointerCount() == 2) {
                    //两个或以上手指的时候，放大、缩小、滑动
                    isInterceptParentTouchEvent(true);
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
                        checkingBorder();
                        invalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (ev.getPointerCount() == 1) {
                    isMoreFingers = false;
                }
                Log.e("AAA", "ScaleEinkHomeworkView :ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_UP:
                isMoreFingers = false;
                getMatrix().getValues(mMatrixValus);
                isInterceptParentTouchEvent(false);
                break;
        }
        return true;
    }

    private float[] mMatrixValus = new float[9];

    public void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_view, null);
        addView(view, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ivBg = (ImageView) view.findViewById(R.id.ivBg);
        ivCanvas = (ImageView) view.findViewById(R.id.ivCanvas);
        einkHomeworkView = (SketchView) view.findViewById(R.id.pathView);
    }


    private SketchView einkHomeworkView;
    public void addBitmap(Bitmap examPaperBitmap, Bitmap studentAnswerBitmap, Bitmap teacherCorrectBitmap, int deviceWidth, int deviceHeight, float zoom,EinkHomeworkViewBean.PaperListBean item) {
//        if(einkHomeworkView == null){
//            einkHomeworkView = new SketchView(getContext(), srcBitmapTeacherCorrect);
//            addView(einkHomeworkView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        }

        ivBg.setImageBitmap(examPaperBitmap);
        ivCanvas.setImageBitmap(studentAnswerBitmap);

        if (teacherCorrectBitmap != null) {
            einkHomeworkView.addTeacherBitmap(teacherCorrectBitmap,item);
        }
        float screenWidth = ScreenUtils.getScreenWidth();

        int bgWidth = examPaperBitmap.getWidth();
        int bgHeight = examPaperBitmap.getHeight();

        int studentWidth = studentAnswerBitmap.getWidth();

        float scale = screenWidth * 1.0f / studentWidth;

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivBg.getLayoutParams();
        layoutParams.width = (int) (bgWidth / 1.1435f * scale);// 1.1435f为zoom值
        layoutParams.height = (int) (bgHeight / 1.1435f * scale);// 1.1435f为zoom值
        ivBg.setLayoutParams(layoutParams);
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
        setX(getX() + offset.x);
        setY(getY() + offset.y);
        if (getScaleX() == 1) {
            setX(0);
            setY(0);
        }
    }

    private PointF offsetBorder() {
        PointF offset = new PointF(0, 0);
        if (getScaleX() > 1) {
            getMatrix().getValues(mMatrixValus);
            if (mMatrixValus[2] > -(mBorderX * (getScaleX() - 1))) {
                offset.x = -(mMatrixValus[2] + mBorderX * (getScaleX() - 1));
            }

            if (mMatrixValus[2] + getWidth() * getScaleX() - mBorderX * (getScaleX() - 1) < getWidth()) {
                offset.x = getWidth() - (mMatrixValus[2] + getWidth() * getScaleX() - mBorderX * (getScaleX() - 1));
            }

            if (mMatrixValus[5] > -(mBorderY * (getScaleY() - 1))) {
                System.out.println("offsetY:" + mMatrixValus[5] + " borderY:" + mBorderY + " scale:" + getScaleY() + " scaleOffset:" + mBorderY * (getScaleY() - 1));
                offset.y = -(mMatrixValus[5] + mBorderY * (getScaleY() - 1));
            }

            if (mMatrixValus[5] + getHeight() * getScaleY() - mBorderY * (getScaleY() - 1) < getHeight()) {
                offset.y = getHeight() - (mMatrixValus[5] + getHeight() * getScaleY() - mBorderY * (getScaleY() - 1));
            }
        }
        return offset;
    }

}
