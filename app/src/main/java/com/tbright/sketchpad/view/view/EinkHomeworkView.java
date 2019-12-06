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
    private int screenWidth = ScreenUtils.getScreenWidth();
    private int screenHeight = ScreenUtils.getScreenHeight();
    //最下层的试卷图片
    private Bitmap testPaperBitmap;

    //学生作答图片。放在试卷图片上面
    private Bitmap mStudentAnswerBitmap;

    //擦除画笔
    private Paint mErasurePaint;
    //擦除路径
    private Path mErasurePath;

    //老师批阅之后的原图片
//    private Bitmap srcBitmapTeacherCorrect;
    //老师批阅的图片，经过适配当前屏幕之后的图片
    private Bitmap dstBitmapTeacherCorrect;
    //绘制经过适配当前屏幕之后的老师批阅的图片canvas
    private Canvas mCanvasTeacherCorrect;
    private PointF mOffset = new PointF(0, 0);
    //记录位置
    private int mLastX;
    private int mLastY;
    //老师批阅的画笔
    private Paint mCorrectPaint;
    private float mScale = 1.0f;


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
        initTeacherCorrect();
        initTestPaperBitmap();
        initTestPaperRect();
    }


    //初始化试卷图片
    private void initTestPaperBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.aa);
//        int bitmapHeight =getBitmapHeights(bitmap);
//        testPaperBitmap = Bitmap.createBitmap(screenWidth, dstBitmapTeacherCorrect.getHeight(), Bitmap.Config.ARGB_4444);
//        Canvas testPaperCanvas = new Canvas(testPaperBitmap);
//        testPaperCanvas.drawBitmap(bitmap, null, new RectF(0, 0, screenWidth, dstBitmapTeacherCorrect.getHeight()), null);

//        testPaperBitmap = Bitmap.createBitmap(2400, 3396, Bitmap.Config.ARGB_4444);
//        Canvas testPaperCanvas = new Canvas(testPaperBitmap);
//        testPaperCanvas.drawBitmap(bitmap, null, new RectF(0, 54, 2400, 3450), null);
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
        mErasurePaint.setStrokeWidth(50);
        mErasurePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //老师批阅的时的批注画笔
        mCorrectPaint = new Paint();
        mCorrectPaint.setAntiAlias(true);
        mCorrectPaint.setColor(Color.parseColor("#ff0000"));
        mCorrectPaint.setStrokeCap(Paint.Cap.ROUND);
        mCorrectPaint.setStrokeJoin(Paint.Join.ROUND);
        mCorrectPaint.setStyle(Paint.Style.STROKE);
        mCorrectPaint.setStrokeWidth(2);

//        //通过资源文件创建Bitmap对象
        Bitmap srcBitmapTeacherCorrect = BitmapFactory.decodeResource(getResources(), R.mipmap.aab);
//        setTeacherCorrectCanvas(srcBitmapTeacherCorrect);
    }

    private void setTeacherCorrectCanvas(Bitmap srcBitmap) {
        int dstBitmapTeacherCorrectHeight = getBitmapHeights(srcBitmap);
        dstBitmapTeacherCorrect = Bitmap.createBitmap(screenWidth, dstBitmapTeacherCorrectHeight, Bitmap.Config.ARGB_4444);
        //双缓冲,装载画布
        mCanvasTeacherCorrect = new Canvas(dstBitmapTeacherCorrect);
        mCanvasTeacherCorrect.drawBitmap(srcBitmap, null, new RectF(0, 0, screenWidth, dstBitmapTeacherCorrectHeight), null);
    }

    /**
     * 添加图片
     *
     * @param examPaperBitmap
     * @param studentAnswerBitmap
     * @param teacherCorrectBitmap
     * @param deviceWidth
     * @param deviceHeight
     */
    public void addBitmap(Bitmap examPaperBitmap, Bitmap studentAnswerBitmap, Bitmap teacherCorrectBitmap, int deviceWidth, int deviceHeight, float zoom) {

        if (examPaperBitmap != null) {
            if (studentAnswerBitmap != null) {
                deviceWidth = studentAnswerBitmap.getWidth();
                deviceHeight = studentAnswerBitmap.getHeight();
            }
            float scale = 1.0f * screenWidth / deviceWidth;
            //进行等比缩放
            int examPaperBitmapWidth = (int) (examPaperBitmap.getWidth() / zoom * scale);
            int examPaperBitmapHeight = (int) (examPaperBitmap.getHeight() / zoom * scale);
            testPaperBitmap = Bitmap.createBitmap(examPaperBitmapWidth, examPaperBitmapHeight, Bitmap.Config.ARGB_4444);
            float left = (screenWidth - examPaperBitmapWidth) / 2.0f;
            float top = (deviceHeight * scale - examPaperBitmapHeight) / 2.0f;
            float right = examPaperBitmapWidth - left;
            float bottom = examPaperBitmapHeight - top;
            Canvas testPaperCanvas = new Canvas(testPaperBitmap);
            testPaperCanvas.drawBitmap(examPaperBitmap, null, new RectF(left, top, right, bottom), null);
        }
        if (studentAnswerBitmap != null) {
            int studentAnswerBitmapWidth = studentAnswerBitmap.getWidth();
            int studentAnswerBitmapHeight = studentAnswerBitmap.getHeight();
            float scale = 1.0f * screenWidth / studentAnswerBitmapWidth;
            int afterScaleHeight = (int) (studentAnswerBitmapHeight * scale);
            mStudentAnswerBitmap = Bitmap.createBitmap(screenWidth, afterScaleHeight, Bitmap.Config.ARGB_4444);
            Canvas studentAnswerBitmapCanvas = new Canvas(mStudentAnswerBitmap);
            studentAnswerBitmapCanvas.drawBitmap(studentAnswerBitmap, null, new RectF(0, 0, screenWidth, afterScaleHeight), null);
        }
        if (teacherCorrectBitmap != null) {
            setTeacherCorrectCanvas(teacherCorrectBitmap);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (dstBitmapTeacherCorrect != null) {
            setMeasuredDimension(screenWidth, dstBitmapTeacherCorrect.getHeight());
        }
    }

    private int getBitmapHeights( Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        float widthScale = screenWidth * 1.0f / bitmapWidth * 1.0f;
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
        testPaperRectPath.addRect(200, 200, 400, 400, Path.Direction.CW);

        testPaperRectRegion = new Region();
        testPaperRectRegion.set(200, 200, 400, 400);
    }

    private boolean isPause = false;

    public void pause(boolean isPause) {
        this.isPause = isPause;
    }

    //这里绘制多张图片会有很严重的卡顿，所以当手指抬起的时候，将当前页面所有的绘制保存成一张图片。这样会很流畅。
    //https://blog.csdn.net/u011814346/article/details/80665102 参考
    private Bitmap bgBitmap = null;
    private Canvas canvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        if (!isPause) {
            if(currentMode == RASURE_MODE || currentMode ==CORRECT_MODE){
                //绘制最底层试卷和学生作答的图片
                drawExamAndStudent(canvas);
                //绘制老师的批阅框
                drawRectPath(canvas);
                //绘制老师的批阅层
                drawTeacherCorrect(canvas);
            }else {
                if(bgBitmap == null){
                    bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
                    Canvas tempCanvas = new Canvas(bgBitmap);
                    //绘制最底层试卷和学生作答的图片
                    drawExamAndStudent(tempCanvas);
                    //绘制老师的批阅框
                    drawRectPath(tempCanvas);
                    //绘制老师的批阅层
                    drawTeacherCorrect(tempCanvas);
                }
                canvas.drawBitmap(bgBitmap, 0, 0, null);
            }
//            if(bgBitmap == null){
//
//            }else {
//                if(currentMode == RASURE_MODE || currentMode ==CORRECT_MODE){
//                    //绘制最底层试卷和学生作答的图片
//                    drawExamAndStudent(tempCanvas);
//                    //绘制老师的批阅框
//                    drawRectPath(tempCanvas);
//                    //绘制老师的批阅层
//                    drawTeacherCorrect(tempCanvas);
//                    canvas.drawBitmap(bgBitmap, 0, 0, null);
//                }else {
//                    canvas.drawBitmap(bgBitmap, 0, 0, null);
//                }
//            }
        }
    }

    //绘制最底层试卷和学生作答的图片
    private void drawExamAndStudent(Canvas canvas){
        if (testPaperBitmap != null) {
            canvas.drawBitmap(testPaperBitmap, 0, 0, null);//最底层的试卷
        }
        if (mStudentAnswerBitmap != null) {
            canvas.drawBitmap(mStudentAnswerBitmap, 0, 0, null);//学生作答的图片
        }
    }
    //绘制老师的批阅框
    private void drawRectPath(Canvas canvas){
        canvas.drawPath(testPaperRectPath, testPaperRectPaint);//话批阅框，可点击
    }
    //绘制老师批阅层
    private void drawTeacherCorrect(Canvas canvas){
        if (dstBitmapTeacherCorrect != null) {
            canvas.drawBitmap(dstBitmapTeacherCorrect, 0, 0, null);//老师批阅层的图片
        }
        if (mCanvasTeacherCorrect != null) {
            for (Path path : mCorrectList) { // 绘制老师批注轨迹，这个轨迹也是绘制在老师批阅层的图片上的
                mCanvasTeacherCorrect.drawPath(path, mCorrectPaint);
            }
            if (currentMode == RASURE_MODE && mErasurePath != null && mErasurePaint != null) {
                mCanvasTeacherCorrect.drawPath(mErasurePath, mErasurePaint);
            }
        }
    }
    //当前老师批阅的路径集合
    private LinkedList<Path> mCorrectList = new LinkedList<Path>();
    //当前老师批阅的路径
    private Path mCurrentCorrectPath;

    //是否按下
    private boolean isActionDown = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = (event.getX() - mOffset.x) / mScale;
        float y = (event.getY() - mOffset.y) / mScale;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isActionDown = true;
//                mLastX = (int) event.getX();
//                mLastY = (int) event.getY();
                mLastX = (int) x;
                mLastY = (int) y;
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
                if (currentMode == RASURE_MODE) {
                    if (mErasurePath != null) {
                        mErasurePath.reset();
                    }
                } else if (currentMode == CORRECT_MODE) {
                    if (mCurrentCorrectPath != null) {
                        mCorrectList.remove(mCurrentCorrectPath);
                        mCurrentCorrectPath.reset();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    if (currentMode == RASURE_MODE) {
                        mErasurePath.lineTo(mLastX, mLastY);
//                        mCanvasTeacherCorrect.drawPath(mErasurePath, mErasurePaint);
                        Log.e("AAA", "正在擦除--------");
                    } else if (currentMode == CORRECT_MODE) {
                        mCurrentCorrectPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                        Log.e("AAA", "正在绘制--------");
                    } else {
                        Log.e("AAA", "正在移动--------");
                    }
                    mLastX = (int) x;
                    mLastY = (int) y;

                } else {
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
                isActionDown = false;
                if (testPaperRectRegion.contains(mLastX, mLastY)) {
                    Toast.makeText(getContext(), "被点击了", Toast.LENGTH_LONG).show();
                }
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
            Log.e("AAA", "进入擦除模式");
//            mCorrectList.clear();
        } else if(currentMode == MOVE_MODE){
//            if(getWidth() == 0){
//                return;
//            }
//            bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
//            Canvas tempCanvas = new Canvas(bgBitmap);
//            //绘制最底层试卷和学生作答的图片
//            drawExamAndStudent(tempCanvas);
//            //绘制老师的批阅框
//            drawRectPath(tempCanvas);
//            //绘制老师的批阅层
//            drawTeacherCorrect(tempCanvas);
//            canvas.drawBitmap(bgBitmap, 0, 0, null);
            bgBitmap = null;
        }
    }

    public void setScaleAndOffset(float scaleX, float mMatrixValu, float mMatrixValu1) {
        mScale = scaleX;
        mOffset.x = mMatrixValu;
        mOffset.y = mMatrixValu1;
    }
}
