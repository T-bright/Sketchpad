package com.tbright.sketchpad.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.tbright.sketchpad.R;
import com.tbright.sketchpad.listactivity.bean.EinkHomeworkViewBean;

import java.util.LinkedList;

public class SketchView extends View {
    //移动模式，这个模式就是手指滑动的时候，当前页面也跟着滑动。单手指，主要是用来区别批阅、擦除事件被占用的情况。这里可以使用双手来滑动，当前不做
    public static int MOVE_MODE = 0;
    public static int CORRECT_MODE = 1;//批阅模式
    public static int RASURE_MODE = 2;//擦除模式
    /**
     * 当前的模式，目前有以下几种：
     * 0.移动，不做批阅和擦除操作。
     * 1.绘制，也就是老师批阅 。
     * 2.擦除，也就是擦除之前的笔迹。都是在老师批阅层上操作。
     *
     * @see EinkHomeworkView#MOVE_MODE
     * @see EinkHomeworkView#CORRECT_MODE
     * @see EinkHomeworkView#RASURE_MODE
     */
    private int currentMode = CORRECT_MODE;
    //屏幕的宽高
    private int screenWidth = ScreenUtils.getScreenWidth();
    private int screenHeight = ScreenUtils.getScreenHeight();

    //擦除画笔
    private Paint mErasurePaint;
    //擦除路径
    private Path mErasurePath;

    //老师批阅之后的原图片
    private Bitmap srcBitmapTeacherCorrect;
    //老师批阅的图片，经过适配当前屏幕之后的图片
    private Bitmap dstBitmapTeacherCorrect;
    //绘制经过适配当前屏幕之后的老师批阅的图片canvas
    private Canvas mCanvasTeacherCorrect;


    //记录位置
    private int mLastX;
    private int mLastY;
    //老师批阅的画笔
    private Paint mCorrectPaint;

    public SketchView(Context context, Bitmap srcBitmapTeacherCorrect) {
        super(context);
        this.srcBitmapTeacherCorrect = srcBitmapTeacherCorrect;
        init(context);
    }

    public SketchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SketchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //擦除画笔
        mErasurePaint = new Paint();
        mErasurePaint.setAntiAlias(true);
        mErasurePaint.setAlpha(0);
        mErasurePaint.setStrokeCap(Paint.Cap.ROUND);
        mErasurePaint.setStrokeJoin(Paint.Join.ROUND);
        mErasurePaint.setStyle(Paint.Style.STROKE);
        mErasurePaint.setStrokeWidth(30);
        mErasurePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //老师批阅的时的批注画笔
        mCorrectPaint = new Paint();
        mCorrectPaint.setAntiAlias(true);
        mCorrectPaint.setColor(Color.parseColor("#ff0000"));
        mCorrectPaint.setStrokeCap(Paint.Cap.ROUND);
        mCorrectPaint.setStrokeJoin(Paint.Join.ROUND);
        mCorrectPaint.setStyle(Paint.Style.STROKE);
        mCorrectPaint.setStrokeWidth(30);

        mCanvasTeacherCorrect = new Canvas();
        //通过资源文件创建Bitmap对象
        srcBitmapTeacherCorrect = BitmapFactory.decodeResource(getResources(), R.mipmap.aab);
        if (srcBitmapTeacherCorrect != null) {
            int bitmapWidth = srcBitmapTeacherCorrect.getWidth();
            float widthScale = screenWidth * 1.0f / bitmapWidth * 1.0f;
            int dstBitmapTeacherCorrectHeight = (int) (srcBitmapTeacherCorrect.getHeight() * widthScale);
            dstBitmapTeacherCorrect = Bitmap.createBitmap(screenWidth, dstBitmapTeacherCorrectHeight, Bitmap.Config.ARGB_8888);
            //双缓冲,装载画布
            mCanvasTeacherCorrect.setBitmap(dstBitmapTeacherCorrect);
            mCanvasTeacherCorrect.drawBitmap(srcBitmapTeacherCorrect, null, new RectF(0, 0, screenWidth, dstBitmapTeacherCorrectHeight), null);
        }
    }

    //当前老师批阅的路径集合
    private LinkedList<Path> mCorrectList = new LinkedList<Path>();
    //当前老师批阅的路径
    private Path mCurrentCorrectPath;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                if (event.getPointerCount() == 1) {
                    if (currentMode == RASURE_MODE) {
                        //这里的擦除路径必须每次都要重新生成
                        mErasurePath = new Path();
                        mErasurePath.moveTo(mLastX, mLastY);
                    } else if (currentMode == CORRECT_MODE) {
                        mCurrentCorrectPath = new Path();
                        mCurrentCorrectPath.moveTo(mLastX, mLastY);
                        mCorrectList.add(mCurrentCorrectPath);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                isScale = true;
                break;
            case MotionEvent.ACTION_MOVE:
//                if(isScale){
//                    return false;
//                }

                if (event.getPointerCount() == 1) {
                    if (currentMode == RASURE_MODE) {
                        mErasurePath.lineTo(mLastX, mLastY);
                        Log.e("AAA", "RASURE_MODE currentModeNum :" + currentMode);
                        mCanvasTeacherCorrect.drawPath(mErasurePath, mErasurePaint);
                    } else if (currentMode == CORRECT_MODE) {
                        Log.e("AAA", "CORRECT_MODE currentModeNum :" + currentMode);
                        mCurrentCorrectPath.quadTo(mLastX, mLastY, (event.getX() + mLastX) / 2, (event.getY() + mLastY) / 2);
                    } else {

                    }
                    mLastX = (int) event.getX();
                    mLastY = (int) event.getY();

                } else {
                    mCurrentCorrectPath.reset();
                    mErasurePath.reset();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mCurrentCorrectPath = null;
                mErasurePath = null;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dstBitmapTeacherCorrect != null) {
            canvas.drawBitmap(dstBitmapTeacherCorrect, 0, 0, null);//老师批阅层的图片
        }
        if (currentMode == CORRECT_MODE) {
            for (Path path : mCorrectList) { // 绘制老师批注轨迹，这个轨迹也是绘制在老师批阅层的图片上的
                mCanvasTeacherCorrect.drawPath(path, mCorrectPaint);
            }
        }
    }

    private EinkHomeworkViewBean.PaperListBean item;

    public void addTeacherBitmap(Bitmap teacherCorrectBitmap, EinkHomeworkViewBean.PaperListBean item) {
        this.item = item;
        this.srcBitmapTeacherCorrect = teacherCorrectBitmap;
        if (srcBitmapTeacherCorrect != null) {
            mCanvasTeacherCorrect.save();
            int bitmapWidth = srcBitmapTeacherCorrect.getWidth();
            float widthScale = screenWidth * 1.0f / bitmapWidth * 1.0f;
            int dstBitmapTeacherCorrectHeight = (int) (srcBitmapTeacherCorrect.getHeight() * widthScale);
            dstBitmapTeacherCorrect = Bitmap.createBitmap(screenWidth, dstBitmapTeacherCorrectHeight, Bitmap.Config.ARGB_8888);
            //双缓冲,装载画布
            mCanvasTeacherCorrect.setBitmap(dstBitmapTeacherCorrect);
            mCanvasTeacherCorrect.drawBitmap(srcBitmapTeacherCorrect, null, new RectF(0, 0, screenWidth, dstBitmapTeacherCorrectHeight), null);
        }
        invalidate();
    }

    /**
     * 设置当前的模式，绘制模式和擦除模式两种
     * 切换模式的时候，需要将批阅path集合清空，同时将批阅的结果和之前的重新合并成一张图片
     */
    public void setCurrentMode(int mCurrentMode) {
        this.currentMode = mCurrentMode;
        item.setTeacherCorrectBitmap(dstBitmapTeacherCorrect);
        Log.e("AAA", "currentMode :" + currentMode);
        if (currentMode == RASURE_MODE) {//擦除模式
            mCorrectList.clear();
        }
    }
}
