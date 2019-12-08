package com.tbright.sketchpad.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.blankj.utilcode.util.ScreenUtils;
import com.tbright.sketchpad.R;

import java.util.LinkedList;

public class EinkHomeworkView extends View {
    //移动模式，这个模式就是手指滑动的时候，当前页面也跟着滑动。单手指，主要是用来区别批阅、擦除事件被占用的情况。这里可以使用双手来滑动，当前不做
    public static int MOVE_MODE = 0;
    public static int CORRECT_MODE = 1;//批阅模式
    public static int RASURE_MODE = 2;//擦除模式
    //试卷层，就一层放置试卷的图片，直接放在最下面即可，先不管

    //批阅层，有两层，下面一层放置批改后的图片，上面一层放置画画板（这个时候是批阅）或者放置一个透明的图片（这个时候是橡皮擦擦除功能）
    //当点击橡皮擦的时候：有三种情况：1.老师未批阅，当前页显示的是之前批阅了一半的试卷。此时下一层是之前批阅图片，上面是透明的

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
    private int screenWidth = 2400;
    private int screenHeight = 1080;
    //最下层的试卷图片
    private Bitmap testPaperBitmap;
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


    public EinkHomeworkView(Context context) {
        this(context, null);
    }

    public EinkHomeworkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EinkHomeworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        initTestPaperBitmap();
        initTeacherCorrect();
        initTestPaperRect();
    }


    //初始化试卷图片
    private void initTestPaperBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.aa);
        int bitmapHeight =getBitmapHeights(bitmap);
        testPaperBitmap = Bitmap.createBitmap(screenWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas testPaperCanvas = new Canvas(testPaperBitmap);
        testPaperCanvas.drawBitmap(bitmap, null, new RectF(0, 0, screenWidth, bitmapHeight), null);
    }

    //初始化老师的批阅层
    private void initTeacherCorrect() {
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

        //通过资源文件创建Bitmap对象
        srcBitmapTeacherCorrect = BitmapFactory.decodeResource(getResources(), R.mipmap.aab);
        int dstBitmapTeacherCorrectHeight = getBitmapHeights(srcBitmapTeacherCorrect);
        dstBitmapTeacherCorrect = Bitmap.createBitmap(screenWidth, dstBitmapTeacherCorrectHeight, Bitmap.Config.ARGB_8888);
        //双缓冲,装载画布
        mCanvasTeacherCorrect = new Canvas(dstBitmapTeacherCorrect);
        mCanvasTeacherCorrect.drawBitmap(srcBitmapTeacherCorrect, null, new RectF(0, 0, screenWidth, dstBitmapTeacherCorrectHeight), null);
    }

    private int getBitmapHeights( Bitmap bitmap){
        int bitmapWidth = bitmap.getWidth();
        float widthScale = screenWidth*1.0f/bitmapWidth*1.0f;
        return (int) (bitmap.getHeight() * widthScale);
    }

    //绘制试题输入框的位置路径
    private Path testPaperRectPath;
    //绘制试题输入框的画笔
    private Paint testPaperRectPaint;
    //绘制试题框的范围
    private Region testPaperRectRegion;

    //如果试卷的试题已经切好了，后端会传过来试卷的答题输入框的坐标，根据坐标绘制
    private void initTestPaperRect() {
        testPaperRectPath = new Path();

        testPaperRectPaint = new Paint();
        testPaperRectPaint.setAntiAlias(true);
        testPaperRectPaint.setColor(Color.parseColor("#ff0000"));
        testPaperRectPaint.setStrokeCap(Paint.Cap.ROUND);
        testPaperRectPaint.setStrokeJoin(Paint.Join.ROUND);
        testPaperRectPaint.setStyle(Paint.Style.STROKE);
        testPaperRectPaint.setStrokeWidth(4);
        testPaperRectPath.addRect(200,200,400,400, Path.Direction.CW);

        testPaperRectRegion = new Region();
        testPaperRectRegion.set(200,200,400,400);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(testPaperBitmap, 0, 0, null);//最底层的试卷
        canvas.drawPath(testPaperRectPath,testPaperRectPaint);//话批阅框，可点击
        canvas.drawBitmap(dstBitmapTeacherCorrect, 0, 0, null);//老师批阅层的图片
        if (currentMode == CORRECT_MODE) {
            for (Path path : mCorrectList) { // 绘制老师批注轨迹，这个轨迹也是绘制在老师批阅层的图片上的
                mCanvasTeacherCorrect.drawPath(path, mCorrectPaint);
            }
        }
        drawText(canvas);
    }
    private void drawText(Canvas canvas){

    }
    //当前老师批阅的路径集合
    private LinkedList<Path> mCorrectList = new LinkedList<Path>();
    //当前老师批阅的路径
    private Path mCurrentCorrectPath;
    //    private boolean isScale = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                if(event.getPointerCount() == 1){
                    if (currentMode == RASURE_MODE) {
                        //这里的擦除路径必须每次都要重新生成
                        mErasurePath = new Path();
                        mErasurePath.moveTo(mLastX, mLastY);
                    } else if(currentMode == CORRECT_MODE){
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
                Log.e("AAA","einkHomeworkView :ACTION_MOVE");
                if(event.getPointerCount() == 1){
                    if (currentMode == RASURE_MODE) {
                        mErasurePath.lineTo(mLastX, mLastY);
                        mCanvasTeacherCorrect.drawPath(mErasurePath, mErasurePaint);
                    } else if(currentMode == CORRECT_MODE){
                        mCurrentCorrectPath.quadTo(mLastX,mLastY,(event.getX() + mLastX) / 2,(event.getY() + mLastY) / 2);
                    }else {

                    }
                    mLastX = (int) event.getX();
                    mLastY = (int) event.getY();

                }else {
                    mCurrentCorrectPath.reset();
                    mErasurePath.reset();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
//                if(isScale){
//                    mErasurePath.reset();
//                    mCurrentCorrectPath.reset();
//                    isScale = false;
//                }
                if(testPaperRectRegion.contains(mLastX,mLastY)){
                    Toast.makeText(getContext(),"被点击了", Toast.LENGTH_LONG).show();
                }
                mCurrentCorrectPath = null;
                mErasurePath = null;
                break;
            default:
                break;
        }
        return true;
    }


    public int getCurrentMode() {
        return currentMode;
    }

    /**
     * 设置当前的模式，绘制模式和擦除模式两种
     * 切换模式的时候，需要将批阅path集合清空，同时将批阅的结果和之前的重新合并成一张图片
     *
     * @param currentMode {@link EinkHomeworkView#CORRECT_MODE }{@link EinkHomeworkView#RASURE_MODE }
     */
    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        if (currentMode == RASURE_MODE) {//擦除模式
//            mCorrectList.clear();
        }
    }

    public void setScaleAndOffset(float scaleX, float mMatrixValu, float mMatrixValu1) {
//        mScale = scaleX;
//        mOffset.x = mMatrixValu;
//        mOffset.y = mMatrixValu1;
    }
}
