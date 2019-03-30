package com.csci4020.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
	Canvas canvas;

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
					break;
			}

			return true;
		} catch (RuntimeException e)
		{
			Log.e("csci4020", "Error on touch event");
			return false;
		}
	}

	public void setCurrentTool(TOOLS currentTool)
	{
		this.currentTool = currentTool;
	}
}
