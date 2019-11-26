package com.tbright.sketchpad.view.pinchimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class PinchImageViewDemo extends AppCompatImageView {
    //屏幕的宽高
    private int screenWidth = 1920;
    private int screenHeight = 1080;
    private Matrix mOuterMatrix = new Matrix();


    /**
     * 最下层的试卷图片
     */
    private Bitmap testPaperBitmap;

    /**
     * 学生作答的图片
     */
    private Bitmap studentAnswerBitmap;

    /**
     * 老师批阅之后的原图片,如果老师未批注（手画的）则为空
     * 目前学生作答的图片和老师批阅的图片尺寸是一样的。最下层的试卷图片目前会大于学生作答的图片，以后的情况不一定。
     * 为了统一，学生作答的图片先铺满，然后最下层的试卷做缩放
     */
    private Bitmap srcBitmapTeacherCorrect;


    public PinchImageViewDemo(Context context) {
        super(context);
        init(context);
    }

    public PinchImageViewDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PinchImageViewDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = MathUtils.matrixTake();
        setImageMatrix(getCurrentImageMatrix(matrix));
        MathUtils.matrixGiven(matrix);
    }

    public Matrix getCurrentImageMatrix(Matrix matrix) {
        //获取内部变换矩阵
        matrix = getInnerMatrix(matrix);
        //乘上外部变换矩阵
        matrix.postConcat(mOuterMatrix);
        return matrix;
    }

    public Matrix getInnerMatrix(Matrix matrix) {
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }
        //原图大小
        RectF tempSrc = MathUtils.rectFTake(0, 0, getScaleBaseBitmap().getWidth(), getScaleBaseBitmap().getHeight());
        //控件大小
        RectF tempDst = MathUtils.rectFTake(0, 0, getWidth(), getHeight());
        //计算fit center矩阵
        matrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER);
        //释放临时对象
        MathUtils.rectFGiven(tempDst);
        MathUtils.rectFGiven(tempSrc);
        return matrix;
    }

    private Bitmap getScaleBaseBitmap(){
        if(studentAnswerBitmap == null){
            return testPaperBitmap;
        }
        return studentAnswerBitmap;
    }
    /**
     * 添加图片
     *
     * @param testPaperBitmap         ：试卷图片
     * @param studentAnswerBitmap     ：学生作答图片
     * @param srcBitmapTeacherCorrect ：老师批阅的图片
     */
    public void addBitmap(Bitmap testPaperBitmap, Bitmap studentAnswerBitmap, Bitmap srcBitmapTeacherCorrect) {
        this.testPaperBitmap = testPaperBitmap;
        this.studentAnswerBitmap = studentAnswerBitmap;
        this.srcBitmapTeacherCorrect = srcBitmapTeacherCorrect;
        Bitmap tempBitmap = studentAnswerBitmap;
        if (tempBitmap == null) {
            tempBitmap = testPaperBitmap;
        }
        //原图大小
        int bitmapWidth = tempBitmap.getWidth();
        float widthScale = screenWidth * 1.0f / bitmapWidth * 1.0f;
        setMeasuredDimension(bitmapWidth, (int) (tempBitmap.getHeight() * widthScale));
    }
}
