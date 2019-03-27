package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class FreePath implements Shape
{
	private Path path;

	public FreePath()
	{
		this.path = new Path();
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawPath(path, paint);
	}

	@Override
	public int getColor()
	{
		return 0;
	}
}
