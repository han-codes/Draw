package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class FreePath extends Path implements Shape
{
	private Path path;
	public int color;
	public int strokeWidth;
	private PAINT_STYLE paintStyle = PAINT_STYLE.STROKE_ONLY;

	public FreePath()
	{
		this.path = new Path();
		this.strokeWidth = 5;
		this.color = 0xFFCCCCCC;
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawPath(this, paint);
	}

	//TODO SHAPE INTERFACE NEEDS TO BE MODIFIED. NOTHING SHOULD ONLY HAVE A COLOR. SHOULD BE FILL OR STROKE
	@Override
	public int getColor()
	{
		return this.color;
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
	public PAINT_STYLE getPaintStyle()
	{
		return this.paintStyle;
	}


	@Override
    public void setFillColor(int fillColor) {

    }

    @Override
    public int getFillColor() {
        return this.color; // TODO THIS NEEDS TO ACTUALLY RETURN A fill COLOR
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
}

