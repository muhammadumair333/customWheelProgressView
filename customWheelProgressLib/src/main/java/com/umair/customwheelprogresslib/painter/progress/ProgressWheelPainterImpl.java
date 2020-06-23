package com.umair.customwheelprogresslib.painter.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

import com.umair.customwheelprogresslib.utils.DimensionUtils;

public class ProgressWheelPainterImpl implements ProgressWheelPainter {

    private RectF circle;
    protected Paint paint;
    private int color;
    private float startAngle;
    private int width;
    private int height;
    private float plusAngle = 0;
    private float max;
    private int strokeWidth;
    private int blurMargin;
    private int lineWidth;
    private int lineSpace;
    private int lineWidthSize;
    private int lineSpaceSize;
    private int strokeWidthSize;
    private Context context;

    public ProgressWheelPainterImpl(int color, float max, int margin, float startAngle, float endAngle,
                                    int lineWidthSize, int lineSpaceSize, int strokeWidthSize, Context context) {
        this.color = color;
        this.max = max;
        this.blurMargin = margin;
        this.context = context;
        this.lineWidthSize = lineWidthSize;
        this.lineSpaceSize = lineSpaceSize;
        this.strokeWidthSize = strokeWidthSize;
        this.startAngle = startAngle;
        initSize();
        init();
    }

    private void initSize() {
        this.lineWidth = DimensionUtils.getSizeInPixels(lineWidthSize, context);
        this.lineSpace = DimensionUtils.getSizeInPixels(lineSpaceSize, context);
        this.strokeWidth = DimensionUtils.getSizeInPixels(strokeWidthSize, context);
    }

    private void init() {
        initPainter();
    }

    private void initPainter() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{lineWidth, lineSpace}, 0));
    }

    private void initExternalCircle() {
        int padding = strokeWidth / 2 + blurMargin;
        circle = new RectF();
        circle.set(padding, padding, width - padding, height - padding);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(circle, startAngle, plusAngle, false, paint);
    }


    @Override
    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width;
        this.height = height;
        initExternalCircle();
    }

    public void setValue(float value) {
        this.plusAngle = (310f * value) / max;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
