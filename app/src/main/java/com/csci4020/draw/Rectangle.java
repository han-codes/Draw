package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Rectangle implements Shape
{
	private Rect rect;

	Rectangle(int left, int top, int right, int bottom)
	{
		rect = new Rect(left, top, right, bottom);
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{

		canvas.drawRect(this.rect, paint);
	}

	@Override
	public int getColor()
	{
		return 0;
	}

	@Override
	public void onDraw(MotionEvent event)
	{

	}
}
