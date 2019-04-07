//package com.csci4020.draw;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.util.Log;
//import android.view.MotionEvent;
//
//public class Line implements Shape
//{
//	private int startX;
//	private int startY;
//	private int endX;
//	private int endY;
//	private boolean isDrawing = false;
//	private int strokeColor = 0xFFCCCCCC;
//	private int thickness = 2;
//	private PAINT_STYLE paintStyle = PAINT_STYLE.STROKE_ONLY;
//
//	Line(int startX, int startY, int endX, int endY, int strokeColor, int thickness)
//	{
//		this.startX = startX;
//		this.startY = startY;
//		this.endX = endX;
//		this.endY = endY;
//		this.strokeColor = strokeColor;
//		this.thickness = thickness;
//	}
//
//	@Override
//	public void draw(Canvas canvas, Paint paint)
//	{
//		Log.i("Draw","Line shape draw method was called");
//		canvas.drawLine(startX, startY, endX, endY, paint);
//	}
//
//	@Override
//	public int getFillColor()
//	{
//		return 0;
//	}
//
//	@Override
//	public void setFillColor(int fillColor)
//	{
//
//	}
//
//	@Override
//	public int getStrokeColor()
//	{
//		return this.strokeColor;
//	}
//
//	@Override
//	public void setStrokeColor(int strokeColor)
//	{
//		this.strokeColor = strokeColor;
//	}
//
//	@Override
//	public int getColor()
//	{
//		//TODO USE STROKE COLOR INSTEAD OF GET COLOR
//		return this.strokeColor;
//	}
//
//	@Override
//	public int getThickness()
//	{
//		return this.thickness;
//	}
//
//	@Override
//	public void onDraw(MotionEvent event)
//	{
//
//	}
//
//	@Override
//	public PAINT_STYLE getPaintStyle()
//	{
//		return this.paintStyle;
//	}
//
//
//	public void setEndx(int endx)
//	{
//		this.endX = endx;
//	}
//
//	public void setEndy(int endy)
//	{
//		this.endY = endy;
//	}
//}
