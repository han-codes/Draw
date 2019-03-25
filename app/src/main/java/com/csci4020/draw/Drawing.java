package com.csci4020.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
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
    private int strokeWidth = 2;
    private Paint paintStroke;
    private Paint paintBackground;
    private float currentX;
    private float currentY;

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

    // Get the current color being used
    public int getColor() {

        return currentColor;
    }

    // Set the color of the paintStroke
    public void setColor(int currentColor) {

        this.currentColor = currentColor;
        this.paintStroke.setColor(currentColor);
    }

    // MARK: - Different paths for paint stroke

    /**
     * The initial start of the stroke path
     */
    public void startPath(float x, float y) {

        // Set up the initial stroke
        StrokePath strokePath = new StrokePath();
        strokePath.setColor(currentColor);
        strokePath.setStrokeWidth(strokeWidth);
        strokePath.moveTo(x, y);
        currentX = x;
        currentY = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(paintBackground);
    }
}

// Create class to subclass for the different stroke paths
class StrokePath implements Shape {

    private Path path;
    private int currentColor;
    private int strokeWidth;

    public void draw(Canvas canvas, Paint paint) {

        canvas.drawPath(path, paint);
    }

    public void setColor(int currentColor) {

        this.currentColor = currentColor;
    }

    public void setStrokeWidth(int strokeWidth) {

        this.strokeWidth = strokeWidth;
    }

    public void moveTo(float x, float y) {

        path.moveTo(x, y);
    }
}
