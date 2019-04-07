package com.csci4020.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Stack;

public class Rectangle implements Shape
{
	private Rect rect;
	private int color;
	private int strokeColor;
	private int fillColor;


	Rectangle(int fillColor, int left, int top, int right, int bottom){
		this.rect = new Rect(left, top, right, bottom);
		this.fillColor = fillColor;
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawRect(this.rect, paint);
	}

	@Override
	public int getFillColor() {
		return this.fillColor;
	}

	@Override
	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public int getStrokeColor() {
		return this.strokeColor;
	}

	@Override
	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

//	@Override
	public int getColor()
	{
		// TODO USE GET FILL COLOR METHOD
		return this.fillColor;
	}

	@Override
	public int getThickness() {
		return 0;
	}

	@Override
	public void onDraw(MotionEvent event)
	{

	}

	@Override
	public PAINT_STYLE getPaintStyle()
	{
		// TODO NEED TO IMPLEMENT THIS FUNCTION
		return null;
	}

	public void setRight(int right){
		this.rect.right = right;
	}

	public void setBottom(int bottom){
		this.rect.bottom = bottom;
	}

	public void setColor(int color){
		this.color = color;
	}


}
