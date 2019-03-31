package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Line implements Shape
{
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private boolean isDrawing = false;
	private int color;
	private int thickness;

	Line(int startX, int startY, int endX, int endY, int color, int thickness)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.color = color;
		this.thickness = thickness;
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawLine(startX, startY, endX, endY, paint);
	}

	@Override
	public int getFillColor() {
		return 0;
	}

	@Override
	public void setFillColor(int fillColor) {

	}

	@Override
	public int getStrokeColor() {
		return 0;
	}

	@Override
	public void setStrokeColor(int strokeColor) {

	}

//	@Override
	public int getColor()
	{
		return 0;
	}

	@Override
	public void onDraw(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int x = (int) event.getX();
			int y = (int) event.getY();
			isDrawing = true;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			isDrawing = false;
			//TODO: WHY DO WE NEED TO TRACK A SHAPE SIZE?
			// shapePositions.push(shapes.size());
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (isDrawing)
			{
				this.endX = ((int) event.getX());
				this.endY = ((int) event.getY());
			}
		}
	}
}
