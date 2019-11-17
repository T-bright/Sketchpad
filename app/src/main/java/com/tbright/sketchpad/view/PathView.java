package com.tbright.sketchpad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView extends View {
    private Bitmap backgroundBitmap;

    public PathView(Context context) {
        super(context);
        init(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Paint mPaint;
    private Matrix backgroundBitmapMatrix = new Matrix();

    private void init(Context context) {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景图片
        setBackgroundBitmap(canvas);
    }

    private void setBackgroundBitmap(Canvas canvas) {
        if (backgroundBitmap != null) {
            //设置缩放比
            backgroundBitmapMatrix.postScale(scale,scale);
            //设置位移
            backgroundBitmapMatrix.postTranslate(0,0);
            canvas.drawBitmap(backgroundBitmap, backgroundBitmapMatrix, mPaint);
        }
    }

    private float scale ;
    public void setBackgroundBitmap(Bitmap bitmap) {
        this.backgroundBitmap = bitmap;
        int width = backgroundBitmap.getWidth();
        int height = backgroundBitmap.getHeight();
        int dstWidth = 1080;
        int dstHeight = (int) ((height * 1.0f / width* 1.0f ) * 1080);
        scale = dstHeight*1.0f/dstWidth*1.0f;
    }
}
