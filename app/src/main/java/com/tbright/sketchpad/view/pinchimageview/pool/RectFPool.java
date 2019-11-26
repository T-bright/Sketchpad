package com.tbright.sketchpad.view.pinchimageview.pool;

import android.graphics.RectF;

public class RectFPool extends ObjectsPool<RectF> {

    public RectFPool(int size) {
        super(size);
    }

    @Override
    protected RectF newInstance() {
        return new RectF();
    }

    @Override
    protected RectF resetInstance(RectF obj) {
        obj.setEmpty();
        return obj;
    }
}
