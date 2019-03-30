package com.csci4020.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
	private int currentHeight;
	private int currentWidth;
	private float currX;
	private float currY;

	private TOOLS currentTool;
	private Canvas canvas;
	private int color;
	private int thickness;
	Stack<Shape> shapeStack = new Stack<>();

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

		// go thru shaps 1 by 1
		for (Shape s : shapeStack){
			if (s.getPaintToUse() == 1) {
				mainPaint.setColor(s.getColor());
				s.draw(canvas, mainPaint);
			} else if (s.getPaintToUse() == Shape.PAINT_STROKE) {
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

			switch (currentTool)
			{
				case BRUSH:
					break;
				case RECTANGLE:
					break;
				case LINE:
					shapeStack.push(new Line((int) x, (int) y, (int) x + 1, (int) y + 1, color, thickness));
					this.drawShape(shapeStack.peek(), event);
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
}
