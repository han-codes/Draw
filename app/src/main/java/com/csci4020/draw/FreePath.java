package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class FreePath implements Shape
{
	private Path path;
	public int color;
	public int strokeWidth;

	public FreePath()
	{
		this.path = new Path();
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawPath(path, paint);
	}

//	@Override
	public int getColor()
	{
		return 0;
	}

    @Override
    public int getThickness() {
        return 0;
    }

    public void setThickness(int strokeWidth){
        this.strokeWidth = strokeWidth;
    }

    @Override
	public void onDraw(MotionEvent event)
	{

	}

    @Override
    public int getPaintToUse() {
        return 0;
    }

    @Override
    public void setFillColor(int fillColor) {

    }

    @Override
    public int getFillColor() {
        return 0;
    }

    @Override
    public int getStrokeColor() {
        return 0;
    }

    @Override
	public void setStrokeColor(int strokeColor) {

	}

    public void setColor(int color) {
        this.color = color;
    }

    public void moveTo(float x, float y){
        path.moveTo(x,y);
    }

    public void quadTo(float x, float y, float x2, float y2){
        path.quadTo(x, y, x2, y2);
    }

    public void lineTo(float x, float y){
        path.lineTo(x, y);
    }
}

