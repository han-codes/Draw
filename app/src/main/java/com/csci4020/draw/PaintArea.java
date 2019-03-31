package com.csci4020.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Stack;

enum TOOLS
{
	BRUSH,
	RECTANGLE,
	LINE;
}

public class PaintArea extends View
{
    // Each RadioButton feature is given an int value
    public static final int BRUSH_FEATURE = 1;
    public static final int LINE_FEATURE = 2;
    public static final int RECTANGLE_FEATURE = 3;
    public static final int STICKER_FEATURE = 4;

    // Initial selected feature is the brush
    private int currentFeature = BRUSH_FEATURE;
	private int currentHeight;
	private int currentWidth;
    private float currentX;
    private float currentY;

	private TOOLS currentTool;
	private Canvas canvas;
    private int currentColor = Color.BLACK;
    private int strokeWidth = 2;
    private Paint mainPaint;
    private Paint linePaint;

	Stack<Shape> shapeStack = new Stack<>();
    public Stack<Integer> shapePosition;

	private boolean isDrawing = false;

	public PaintArea(Context context)
	{
		super(context);
		this.setup();
	}

	public PaintArea(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setup();
	}

	public PaintArea(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		this.setup();
	}

	private void setup()
	{

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		// go through shapes 1 by 1
		for (Shape s : shapeStack){
            s.getPaintToUse()
			if (s.getPaintToUse() == 1) {
				mainPaint.setColor(s.getFillColor());
				s.draw(canvas, mainPaint);
			} else if (s.getPaintToUse() == Shape.fillColor) {
				linePaint.setColor(s.getColor());
				linePaint.setStrokeWidth(s.getThickness());
				s.draw(canvas, linePaint);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		this.currentHeight = h;
		this.currentWidth = w;

		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
	}

	@Override
	public boolean performClick()
	{
		return super.performClick();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width;
		int height;

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int desiredWidth = (int) DrawActivity.convertDpToPx(100, getContext());
		int desiredHeight = (int) DrawActivity.convertDpToPx(100, getContext());

		if (widthMode == MeasureSpec.EXACTLY)
		{
			width = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST)
		{
			width = Math.min(desiredWidth, widthSize);
		}
		else
		{
			width = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY)
		{
			height = heightSize;
		}
		else if (heightMode == MeasureSpec.AT_MOST)
		{
			height = Math.min(desiredHeight, heightSize);
		}
		else
		{
			height = desiredHeight;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		try
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				performClick();
			}

			float x = event.getX();
			float y = event.getY();

			// TODO: - Check if there's a better way to set up a switch statement inside of BRUSH depending on which event.getAction()
			switch (currentTool)
			{
				case BRUSH:
				    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startPath(x, y);
                            invalidate();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            continuePath(x, y);
                            invalidate();
                            break;
                        case MotionEvent.ACTION_UP:

                            shapePosition.push(shapeStack.size());
                            invalidate();
                            break;
                }
					break;
				case RECTANGLE:
				    onDrawRectangle();
				case LINE:
				    onDrawLine(event);

//					shapeStack.push(new Line((int) x, (int) y, (int) x + 1, (int) y + 1, color, strokeWidth));
//					this.drawShape(shapeStack.peek(), event);
					break;
			}

			return true;
		} catch (RuntimeException e)
		{
			Log.e("csci4020", "Error on touch event");
			return false;
		}
	}

	public void drawShape(Shape shape, MotionEvent event)
	{
		shape.onDraw(event);
		invalidate();
	}

	public void setCurrentTool(TOOLS currentTool)
	{
		this.currentTool = currentTool;
	}

    //Create a line on first touch, and move the line while the user is dragging their finger around
    private void onDrawLine(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Line line = new Line(x, y, x+1, y+1, currentColor, strokeWidth);
            shapeStack.push(line);
            isDrawing = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            isDrawing = false;
            shapePosition.push(shapeStack.size());
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (isDrawing) {
                ((Line) shapeStack.peek()).setEndx(((int) event.getX()));
                ((Line) shapeStack.peek()).setEndy(((int) event.getY()));
            }
        }
        invalidate();
    }

    public void onDrawRectangle(MotionEvent event, Stack<Shape> shapeStack, Stack<Integer> shapePosition){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Rectangle rect = new Rectangle(color, x, y, x + 1, y + 1);
            rect.setColor(color);
            shapeStack.push(rect);
            shapeStack.push(rect);
            isDrawing = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            isDrawing = false;
            shapePosition.push(shapeStack.size());
        }
        //update the last drawn shape if we're still drawing it and moved
        else if (event.getAction() == MotionEvent.ACTION_MOVE){
            ((Rectangle) shapeStack.peek()).setRight( (int) event.getX());
            ((Rectangle) shapeStack.peek()).setBottom( (int) event.getY());
        }
        invalidate();
    }

    /**
     * The initial start of the stroke path
     */
    public void startPath(float x, float y)
    {
        // Set up a temporary path
        FreePath temporaryPath = new FreePath();
        temporaryPath.setColor(currentColor);
        temporaryPath.setThickness(strokeWidth);
        temporaryPath.moveTo(x,y);

        // Set up the beginning stroke
        StrokePath strokePath = new StrokePath();
        strokePath.setColor(currentColor);
        strokePath.setStrokeWidth(currentWidth);
        strokePath.moveTo(x, y);
        currentX = x;
        currentY = y;
    }

    public void continuePath(float x, float y){

        FreePath temporaryPath = (FreePath) shapeStack.get(shapeStack.size() - 1);

        if ( Math.abs(currentX - x) >= 4 || Math.abs(currentY - y) >= 4 ) {

            temporaryPath.quadTo(currentX,currentY, (x + currentX) / 2, (y + currentY) / 2);
            currentX = x;
            currentY = y;
            shapeStack.add(temporaryPath);
        }
    }


}

interface Shape
{
    // Get's fill color
    int getColor();
    int getThickness();

    int fillColor = 1;
    int strokeColor = 0;
    void draw(Canvas canvas, Paint paint);

    int getFillColor();
    void setFillColor(int fillColor);

    int getStrokeColor();
    void setStrokeColor(int strokeColor);

    void onDraw(MotionEvent event);
    int getPaintToUse();
}
