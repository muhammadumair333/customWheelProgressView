package com.umair.customwheelprogresslib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.umair.customwheelprogresslib.painter.progress.BlurProgressWheelPainter;
import com.umair.customwheelprogresslib.painter.progress.ProgressWheelPainter;
import com.umair.customwheelprogresslib.painter.progress.ProgressWheelPainterImpl;
import com.umair.customwheelprogresslib.painter.wheel.InternalWheelPainter;
import com.umair.customwheelprogresslib.painter.wheel.InternalWheelPainterImpl;
import com.umair.customwheelprogresslib.utils.DimensionUtils;

public class ProgressView extends View {

    private ValueAnimator progressValueAnimator;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private InternalWheelPainter internalVelocimeterPainter;
    private ProgressWheelPainter progressVelocimeterPainter;
    private ProgressWheelPainter blurProgressVelocimeterPainter;
    private int min = 0;
    private float progressLastValue = min;
    private int max = 100;
    private float value;
    private int duration = 1000;
    private long progressDelay = 350;
    private int margin = 5;
    private int insideProgressColor = Color.parseColor("#094e35");
    private int externalProgressColor = Color.parseColor("#9cfa1d");
    private int progressBlurColor = Color.parseColor("#44ff2b");
    private int bottomVelocimeterColor = Color.parseColor("#1E1E1E");
    private int internalVelocimeterColor = Color.WHITE;
    private int lineWidth = 2;
    private int lineSpacing = 3;
    private int strokeWidth = 10;
    private float startAngle = 120;
    private float endAngle = 300;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        } else {
            size = width;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        internalVelocimeterPainter.onSizeChanged(h, w);
        progressVelocimeterPainter.onSizeChanged(h, w);
        blurProgressVelocimeterPainter.onSizeChanged(h, w);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray attributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.VelocimeterView);
        initAttributes(attributes);

        int marginPixels = DimensionUtils.getSizeInPixels(margin, getContext());
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        internalVelocimeterPainter =
                new InternalWheelPainterImpl(insideProgressColor, marginPixels, startAngle, endAngle, lineWidth, lineSpacing, strokeWidth, getContext());
        progressVelocimeterPainter =
                new ProgressWheelPainterImpl(externalProgressColor, max, marginPixels, startAngle, endAngle, lineWidth, lineSpacing, strokeWidth, getContext());
        blurProgressVelocimeterPainter =
                new BlurProgressWheelPainter(progressBlurColor, max, marginPixels, startAngle, endAngle, lineWidth, lineSpacing, strokeWidth, getContext());
        initValueAnimator();

    }

    private void initAttributes(TypedArray attributes) {
        insideProgressColor =
                attributes.getColor(R.styleable.VelocimeterView_inside_progress_color, insideProgressColor);
        externalProgressColor = attributes.getColor(R.styleable.VelocimeterView_external_progress_color,
                externalProgressColor);
        progressBlurColor =
                attributes.getColor(R.styleable.VelocimeterView_progress_blur_color, progressBlurColor);
        bottomVelocimeterColor =
                attributes.getColor(R.styleable.VelocimeterView_bottom_velocimeter_color,
                        bottomVelocimeterColor);

        internalVelocimeterColor =
                attributes.getColor(R.styleable.VelocimeterView_internal_velocimeter_color,
                        internalVelocimeterColor);
        max = attributes.getInt(R.styleable.VelocimeterView_max, max);
        lineWidth = attributes.getInt(R.styleable.VelocimeterView_progressLineWidth, lineWidth);
        lineSpacing = attributes.getInt(R.styleable.VelocimeterView_progressLineSpacing, lineSpacing);
        strokeWidth = attributes.getInt(R.styleable.VelocimeterView_progressStrokeWidth, strokeWidth);
        startAngle = attributes.getFloat(R.styleable.VelocimeterView_startAngle, startAngle);
        endAngle = attributes.getFloat(R.styleable.VelocimeterView_endAngle, endAngle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        blurProgressVelocimeterPainter.draw(canvas);
        internalVelocimeterPainter.draw(canvas);
        progressVelocimeterPainter.draw(canvas);
        invalidate();
    }

    public void setValue(float value) {
        this.value = value;
        if (value <= max && value >= min) {
            animateProgressValue();
        }
    }

    public void setValue(float value, boolean animate) {
        this.value = value;
        if (value <= max && value >= min) {
            if (!animate) {
                updateValueProgress(value);

            } else {
                animateProgressValue();
            }
        }
    }

    private void initValueAnimator() {
        progressValueAnimator = new ValueAnimator();
        progressValueAnimator.setInterpolator(interpolator);
        progressValueAnimator.addUpdateListener(new ProgressAnimatorListenerImp());
    }

    private void animateProgressValue() {
        if (progressValueAnimator != null) {
            progressValueAnimator.setFloatValues(progressLastValue, value);
            progressValueAnimator.setDuration(duration + progressDelay);
            progressValueAnimator.start();
        }
    }

    public void setProgress(Interpolator interpolator) {
        this.interpolator = interpolator;

        if (progressValueAnimator != null) {
            progressValueAnimator.setInterpolator(interpolator);
        }
    }

    public float getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private void updateValueProgress(float value) {
        progressVelocimeterPainter.setValue(value);
        blurProgressVelocimeterPainter.setValue(value);
    }


    private class ProgressAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();
            updateValueProgress(value);
            progressLastValue = value;
        }
    }
}
