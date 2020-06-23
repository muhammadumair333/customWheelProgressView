package com.umair.customwheelprogresslib.painter.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

import com.umair.customwheelprogresslib.utils.DimensionUtils;

public class InternalWheelPainterImpl implements InternalWheelPainter  {

    private Paint paint;
    private RectF circle;
    private Context context;
    private int width;
    private int height;
    private float startAngle;
    private float finishAngle;
    private int strokeWidth;
    private int blurMargin;
    private int lineWidth;
    private int lineSpace;
    private int lineWidthSize;
    private int lineSpaceSize;
    private int strokeWidthSize;
    private int color;

    public InternalWheelPainterImpl(int color, int margin, float startAngle, float endAngle, int lineWidthSize, int lineSpaceSize, int strokeWidthSize, Context context) {
        this.blurMargin = margin;
        this.context = context;
        this.lineWidthSize = lineWidthSize;
        this.lineSpaceSize = lineSpaceSize;
        this.strokeWidthSize = strokeWidthSize;
        this.startAngle = startAngle;
        finishAngle = endAngle;
        this.color = color;
        initSize();
        initPainter();
    }

    private void initSize() {
        this.lineWidth = DimensionUtils.getSizeInPixels(lineWidthSize, context);
        this.lineSpace = DimensionUtils.getSizeInPixels(lineSpaceSize, context);
        this.strokeWidth = DimensionUtils.getSizeInPixels(strokeWidthSize, context);
    }

    private void initPainter() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{lineWidth, lineSpace}, 0));
    }

    private void initCircle() {
        int padding = strokeWidth / 2 + blurMargin;
        circle = new RectF();
        circle.set(padding, padding, width - padding, height - padding);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(circle, startAngle, finishAngle, false, paint);
    }


    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width;
        this.height = height;
        initCircle();
    }
}
