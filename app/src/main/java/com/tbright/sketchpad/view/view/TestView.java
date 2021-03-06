package com.tbright.sketchpad.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.text.TextUtils;
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

    private static final float MAX_SCALE = 2.0F;
    private static final float MIN_SCALE = 0.2f;

    private SketchView einkHomeworkView;
    private float mOldDistance;
    private PointF mOldPointer;
    private float scaleFactor;
    private float[] mMatrixValus = new float[9];
    private float mBorderX, mBorderY;
    private ImageView ivExamImageView;
    private ImageView ivStudentImageView;
    private View containView;
    private FrameLayout flItemRoot;

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

    private void init(Context context) {
        containView = LayoutInflater.from(context).inflate(R.layout.item_eink_homework, null);
        addView(containView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        flItemRoot = (FrameLayout) containView.findViewById(R.id.flItemRoot);
        ivExamImageView = (ImageView) containView.findViewById(R.id.ivExamImageView);
        ivStudentImageView = (ImageView) containView.findViewById(R.id.ivStudentImageView);
        einkHomeworkView = (SketchView) containView.findViewById(R.id.einkHomeworkView);
    }
    //记录位置
    private int mLastX;
    private int mLastY;

    /**
     * 是否拦截父容器的事件
     * isIntercept : true.拦截父容器（就是子类需要当前的事件，让父类不要拦截事件），false.不拦截父类（就是子类不需要当前的事件，让父类自行处理）
     */
    private void isInterceptParentTouchEvent(boolean isIntercept) {
        getParent().requestDisallowInterceptTouchEvent(isIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() >= 2) {//手指大于等于2，通知父类不要拦截事件
            isInterceptParentTouchEvent(true);
        }
        if (currentMode == EinkHomeworkView.CORRECT_MODE || currentMode == EinkHomeworkView.RASURE_MODE) {//手绘模式或者擦除模式，通知父类不要拦截事件
            isInterceptParentTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 拦截 拦截之后交给onTouchEvent处理
     * 拦截的情况：只有在双手处理缩放的情况下，拦截事件，处理缩放手势
     * 不拦截的情况：
     */

    //是否拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() >= 2) {//手指大于等于2，拦截事件
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDistance = spacingOfTwoFinger(ev);
                mOldPointer = middleOfTwoFinger(ev);
                Log.e("BBB", "ACTION_POINTER_DOWN :" +mOldDistance);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getPointerCount() == 2) {
                    //两个或以上手指的时候，放大、缩小、滑动
                    try {
                        PointF mNewPointer = middleOfTwoFinger(ev);
                        float newDistance = spacingOfTwoFinger(ev);
                        if (mOldDistance == 0) {
                            scaleFactor = 1.0f;
                        } else {
                            scaleFactor = newDistance / mOldDistance;
                        }
                        scaleFactor = checkingScale(containView.getScaleX(), scaleFactor);
                        containView.setScaleX(containView.getScaleX() * scaleFactor);
                        containView.setScaleY(containView.getScaleY() * scaleFactor);
                        containView.setPivotX(mNewPointer.x);
                        containView.setPivotY(mNewPointer.y);
                        mOldDistance = newDistance;
                        Log.e("AA", "x :" + (mNewPointer.x - mOldPointer.x));
                        Log.e("AA", "y :" + (mNewPointer.y - mOldPointer.y));
                        containView.setX(containView.getX() + mNewPointer.x - mOldPointer.x);
                        containView.setY(containView.getY() + mNewPointer.y - mOldPointer.y);
                        mOldPointer = mNewPointer;
                        checkingBorder();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_UP:
                containView.getMatrix().getValues(mMatrixValus);
                einkHomeworkView.setScaleAndOffset(containView.getScaleX(), mMatrixValus[2], mMatrixValus[5]);
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
     *
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
        containView.setX(containView.getX() + offset.x);
        containView.setY(containView.getY() + offset.y);
        if (containView.getScaleX() == 1) {
            containView.setX(0);
            containView.setY(0);
        }
    }

    private PointF offsetBorder() {
        PointF offset = new PointF(0, 0);
        if (containView.getScaleX() > 1) {
            containView.getMatrix().getValues(mMatrixValus);
            if (mMatrixValus[2] > -(mBorderX * (containView.getScaleX() - 1))) {
                offset.x = -(mMatrixValus[2] + mBorderX * (containView.getScaleX() - 1));
            }

            if (mMatrixValus[2] + containView.getWidth() * containView.getScaleX() - mBorderX * (containView.getScaleX() - 1) < getWidth()) {
                offset.x = getWidth() - (mMatrixValus[2] + containView.getWidth() * containView.getScaleX() - mBorderX * (containView.getScaleX() - 1));
            }

            if (mMatrixValus[5] > -(mBorderY * (containView.getScaleY() - 1))) {
                System.out.println("offsetY:" + mMatrixValus[5] + " borderY:" + mBorderY + " scale:" + getScaleY() + " scaleOffset:" + mBorderY * (getScaleY() - 1));
                offset.y = -(mMatrixValus[5] + mBorderY * (containView.getScaleY() - 1));
            }

            if (mMatrixValus[5] + containView.getHeight() * containView.getScaleY() - mBorderY * (containView.getScaleY() - 1) < getHeight()) {
                offset.y = getHeight() - (mMatrixValus[5] + containView.getHeight() * containView.getScaleY() - mBorderY * (containView.getScaleY() - 1));
            }
        }
        return offset;
    }

    public void addBitmap(EinkHomeworkViewBean.PaperListBean paperListBean) {
        String examPaperBitmapPath = paperListBean.getExampaperImagePath();
        String studentAnswerBitmapPath = paperListBean.getStudentAnswerImagePath();
        Bitmap examPaperBitmap = null;
        Bitmap studentAnswerBitmap = null;
        if (!TextUtils.isEmpty(examPaperBitmapPath)) {
            examPaperBitmap = BitmapFactory.decodeFile(examPaperBitmapPath);
        }
        if (!TextUtils.isEmpty(studentAnswerBitmapPath)) {
            studentAnswerBitmap = BitmapFactory.decodeFile(studentAnswerBitmapPath);
        }
        if (examPaperBitmap != null && studentAnswerBitmap != null) {

            float screenWidth = ScreenUtils.getScreenWidth();

            int studentWidth = studentAnswerBitmap.getWidth();
            int studentHeight = studentAnswerBitmap.getHeight();
            float scale = screenWidth * 1.0f / studentWidth;

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) flItemRoot.getLayoutParams();
            layoutParams.width = (int) screenWidth;// 1.1435f为zoom值
            layoutParams.height = (int) (studentHeight * scale);// 1.1435f为zoom值
            flItemRoot.setLayoutParams(layoutParams);

            ivExamImageView.setImageBitmap(examPaperBitmap);
            ivStudentImageView.setImageBitmap(studentAnswerBitmap);
        }
        einkHomeworkView.addBitmap(paperListBean);
    }

    public void setOnClickCorrectRectListener(SketchView.OnClickCorrectRectListener onClickCorrectRectListener) {
        einkHomeworkView.setOnClickCorrectRectListener(onClickCorrectRectListener);
    }
}
