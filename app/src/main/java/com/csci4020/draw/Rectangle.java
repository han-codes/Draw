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


//	Rectangle(int left, int top, int right, int bottom)
//	{
//		rect = new Rect(left, top, right, bottom);
//	}

	Rectangle(int color, int left, int top, int right, int bottom){
		rect = new Rect(left, top, right, bottom);
		this.color = color;
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{

		canvas.drawRect(this.rect, paint);
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
	public int getThickness() {
		return 0;
	}

	@Override
	public void onDraw(MotionEvent event)
	{

	}

	@Override
	public int getPaintToUse() {
		return 0;
	}

	public void setRight(int right){
		rect.right = right;
	}

	public void setBottom(int bottom){
		rect.bottom = bottom;
	}

	public void setColor(int color){
		this.color = color;
	}


}
