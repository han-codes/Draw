package com.csci4020.draw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Drawing extends View {

    // Each RadioButton feature is given an int value
    public static final int BRUSH_FEATURE = 1;
    public static final int LINE_FEATURE = 2;
    public static final int RECTANGLE_FEATURE = 3;
    public static final int STICKER_FEATURE = 4;

    // Initial selected feature is the brush
    private int currentFeature = BRUSH_FEATURE;

    private int currentColor = Color.BLACK;
    private Paint paintStroke;
    private int strokeWidth = 2;

    public Drawing(Context context) {
        super(context);

        setUpDrawing();
    }

    public Drawing(Context context, AttributeSet attrs) {
        super(context, attrs);

        setUpDrawing();
    }

    public Drawing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setUpDrawing();
    }

    private void setUpDrawing() {

        paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintStroke.setColor(currentColor);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setStrokeWidth(strokeWidth);
        paintStroke.setStrokeJoin(Paint.Join.ROUND);
    }
}
