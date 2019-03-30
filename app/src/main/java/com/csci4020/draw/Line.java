package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Line implements Shape
{
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public Line(int startX, int startY, int endX, int endY)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawLine(startX, startY, endX, endY, paint);
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
