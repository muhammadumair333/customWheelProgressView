package com.umair.customwheelprogresslib.painter.progress;

import android.content.Context;
import android.graphics.BlurMaskFilter;

public class BlurProgressWheelPainter extends ProgressWheelPainterImpl {

    public BlurProgressWheelPainter(int color, float max, int margin, float startAngle, float endAngle, int lineWidthSize, int lineSpaceSize, int strokeWidthSize, Context context) {
        super(color, max, margin, startAngle, endAngle, lineWidthSize, lineSpaceSize, strokeWidthSize, context);
        paint.setMaskFilter(new BlurMaskFilter(45, BlurMaskFilter.Blur.NORMAL));
    }
}
