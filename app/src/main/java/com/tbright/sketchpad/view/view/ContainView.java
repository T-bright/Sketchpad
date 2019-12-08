package com.tbright.sketchpad.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;

public class ContainView extends FrameLayout {
    private Bitmap examBitmap;
    private Bitmap studentBitmap;

    public ContainView(@NonNull Context context, Bitmap examBitmap, Bitmap studentBitmap) {
        this(context, null);
        this.examBitmap = examBitmap;
        this.studentBitmap = studentBitmap;
        init(context);
    }

    public ContainView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        ImageView examImageView = new ImageView(context);
        addView(examImageView);
        ImageView studentImageView = new ImageView(context);
        addView(studentImageView);
        float studentscale = ScreenUtils.getScreenWidth() * 1.0f / studentBitmap.getWidth();
        FrameLayout.LayoutParams studentlayoutParams = (LayoutParams) studentImageView.getLayoutParams();
        studentlayoutParams.width = ScreenUtils.getScreenWidth();
        studentlayoutParams.height = (int) (studentBitmap.getHeight() * studentscale);
        studentImageView.setLayoutParams(studentlayoutParams);

        float examscale = ScreenUtils.getScreenWidth() * 1.0f / examBitmap.getWidth();
        FrameLayout.LayoutParams examlayoutParams = (LayoutParams) examImageView.getLayoutParams();
        examlayoutParams.width = ScreenUtils.getScreenWidth();
        examlayoutParams.height = (int) (examBitmap.getHeight() * examscale);
        examlayoutParams.gravity = Gravity.CENTER;
        examImageView.setLayoutParams(examlayoutParams);

        examImageView.setImageBitmap(examBitmap);
        examImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        studentImageView.setImageBitmap(studentBitmap);
        studentImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
