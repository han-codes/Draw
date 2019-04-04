package com.csci4020.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

enum TOOLS
{
	BRUSH,
	RECTANGLE,
	LINE,
	STICKER
}

public class PaintArea extends View
{
	// Each RadioButton feature is given an int value
	public static final int BRUSH_FEATURE = 1;
	public static final int LINE_FEATURE = 2;
	public static final int RECTANGLE_FEATURE = 3;
	public static final int STICKER_FEATURE = 4;

	// Initial selected feature is the brush
	private int currentFeature = BRUSH_FEATURE;
	private int currentHeight;
	private int currentWidth;
	private float currentX;
	private float currentY;


	private Random random;

	private TOOLS currentTool = TOOLS.BRUSH;
	private Canvas canvas;
	private int currentColor = Color.BLACK;
	private int strokeWidth = 2;
	private Paint backgroundPaint;
	private Paint mainPaint;
	private Paint linePaint;
	private Path path;

	private Bitmap bitmap;
	Bitmap outsideFrame;
	Bitmap currentBitmap;
	Bitmap stickerStar;
	Bitmap stickerLee;
	Bitmap stickerLeaf;


	public static final int STAR_STICKER = 1;

	Matrix matrix;

	Stack<Shape> shapeStack = new Stack<>();
	public Stack<Integer> shapePosition;
	ArrayList<Path> paths;


	private boolean isDrawing = false;

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

		random = new Random();
		shapeStack = new Stack<>();
		matrix = new Matrix();
		paths = new ArrayList<>();
		shapePosition = new Stack<>();
		this.bitmap = null;

		backgroundPaint = new Paint();
		backgroundPaint.setColor(0xffffffff);
		backgroundPaint.setStyle(Paint.Style.FILL);

		currentColor = 0xff444444;
		setStrokeThickness(5);
		mainPaint = new Paint();
		mainPaint.setColor(currentColor);
		mainPaint.setStyle(Paint.Style.FILL);

		linePaint = new Paint();
		linePaint.setColor(currentColor);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeWidth(strokeWidth);
		linePaint.setStrokeJoin(Paint.Join.ROUND);


		path = new Path();

		setupStickerBitmaps();
	}

	/**
	 * Setup for using stickers
	 */

	private void setupStickerBitmaps()
	{
		Drawable androidDrawable = getResources().getDrawable((R.drawable.star));

		int size = (int) Helper.convertDpToPx(50, getContext());

		stickerStar = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(stickerStar);
		androidDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		androidDrawable.draw(canvas);

		currentBitmap = stickerStar;
	}


	// MARK: - Get/Set
	public void setStrokeThickness(int dpSize)
	{
		strokeWidth = (int) Helper.convertDpToPx(dpSize, getContext());
	}

	/**
	 * Set current bitmap to selected sticker
	 */

	public void setSticker(int stickerId)
	{

		switch (stickerId)
		{
			case STAR_STICKER:
				currentBitmap = stickerStar;
				break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		canvas.drawPaint(backgroundPaint);

		if (bitmap != null)
		{
			canvas.drawBitmap(this.bitmap, null, new Rect(0, 0, currentWidth, currentHeight), null);
		}

		// go through shapes 1 by 1
		for (Shape s : shapeStack)
		{
			s.getPaintToUse();
			if (s.getPaintToUse() == 1)
			{
				mainPaint.setColor(s.getFillColor());
				s.draw(canvas, mainPaint);
			}
			else if (s.getPaintToUse() == Shape.fillColor)
			{
				linePaint.setColor(s.getColor());
				linePaint.setStrokeWidth(s.getThickness());
				s.draw(canvas, linePaint);
			}
		}

		drawFrame(canvas, mainPaint);

		this.canvas = canvas;
	}

	private boolean drawingFrame = false;

	/**
	 * Toggling the frame
	 */

	public void toggleDrawingFrame()
	{
		drawingFrame = !drawingFrame;
		invalidate();
	}

	/**
	 * Drawing the frame
	 */

	private void drawFrame(Canvas canvas, Paint paint)
	{

		if (drawingFrame)
		{
			if (outsideFrame == null)
			{
				Drawable frameDrawable = getResources().getDrawable((R.drawable.frame));

				outsideFrame = Bitmap.createBitmap(this.getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

				Canvas canvas4 = new Canvas(outsideFrame);
				frameDrawable.setBounds(0, 0, canvas4.getWidth(), canvas4.getHeight());
				frameDrawable.draw(canvas4);
			}

			canvas.drawBitmap(outsideFrame, 0, 0, paint);
		}
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

		int desiredWidth = (int) Helper.convertDpToPx(100, getContext());
		int desiredHeight = (int) Helper.convertDpToPx(100, getContext());

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
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			performClick();
		}

		float x = event.getX();
		float y = event.getY();

		switch (this.currentTool)
		{
			case BRUSH:
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						startPath(x, y);
						invalidate();
						break;
					case MotionEvent.ACTION_MOVE:
						continuePath(x, y);
						invalidate();
						break;
					case MotionEvent.ACTION_UP:
						stopPath(x, y);
						shapePosition.push(shapeStack.size());
						invalidate();
						break;
				}
				break;
			case RECTANGLE:
				onDrawRectangle(event);
			case LINE:
				onDrawLine(event);
		}

		return true;
	}

	public void setNewImage(Bitmap alteredBitmap, Bitmap bitmap)
	{

		this.bitmap = bitmap;

		invalidate();
	}

	public void drawShape(Shape shape, MotionEvent event)
	{
		shape.onDraw(event);
		invalidate();
	}

	public void setCurrentTool(TOOLS currentTool)
	{
		Log.i("Draw","Current tool set to " + currentTool);
		this.currentTool = currentTool;
	}

	public void setShapes(Stack<Shape> shapeStack)
	{
		this.shapeStack = shapeStack;
		invalidate();
	}

	public void setShapePosition(Stack<Integer> shapePosition)
	{
		this.shapePosition = shapePosition;
		invalidate();
	}

	//Create a line on first touch, and move the line while the user is dragging their finger around
	private void onDrawLine(MotionEvent event)
	{

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				int x = (int) event.getX();
				int y = (int) event.getY();

				Line line = new Line(x, y, x + 1, y + 1, currentColor, strokeWidth);
				shapeStack.push(line);
				isDrawing = true;
			case MotionEvent.ACTION_UP:
				isDrawing = false;
				shapePosition.push(shapeStack.size());
			case MotionEvent.ACTION_MOVE:
				if (isDrawing)
				{
					((Line) shapeStack.peek()).setEndx(((int) event.getX()));
					((Line) shapeStack.peek()).setEndy(((int) event.getY()));
				}
		}

		invalidate();
	}

	private void onDrawRectangle(MotionEvent event)
	{

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				int x = (int) event.getX();
				int y = (int) event.getY();

				Rectangle rect = new Rectangle(currentColor, x, y, x + 1, y + 1);
				rect.setColor(currentColor);
				shapeStack.push(rect);
				isDrawing = true;
			case MotionEvent.ACTION_UP:
				isDrawing = false;
				shapePosition.push(shapeStack.size());
			case MotionEvent.ACTION_MOVE:
				((Rectangle) shapeStack.peek()).setRight((int) event.getX());
				((Rectangle) shapeStack.peek()).setBottom((int) event.getY());
		}

		invalidate();
	}

	//Create a line on first touch, and move the line while the user is dragging their finger around
	private void onDrawSticker(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int x = (int) event.getX();
			int y = (int) event.getY();

			Sticker sticker = new Sticker(x, y, currentBitmap);
			shapeStack.push(sticker);
			isDrawing = true;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			isDrawing = false;
			shapePosition.push(shapeStack.size());
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (isDrawing)
			{
				((Sticker) shapeStack.peek()).setX(((int) event.getX()));
				((Sticker) shapeStack.peek()).setY(((int) event.getY()));
			}
		}
		invalidate();
	}

	// MARK: - PATHS

	/**
	 * The initial start of the stroke path
	 */

	public void startPath(float x, float y)
	{
		Log.i("Draw", "Began a free path");
		// Set up a temporary path
		FreePath temporaryPath = new FreePath();
		temporaryPath.setColor(currentColor);
		temporaryPath.setThickness(strokeWidth);
		temporaryPath.moveTo(x, y);
		currentX = x;
		currentY = y;
		shapeStack.add(temporaryPath);
	}

	public void continuePath(float x, float y)
	{
		Log.i("Draw", "Continuing the free path");
		FreePath temporaryPath = (FreePath) shapeStack.get(shapeStack.size() - 1);

		if (Math.abs(currentX - x) >= 4 || Math.abs(currentY - y) >= 4)
		{

			temporaryPath.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2);
			currentX = x;
			currentY = y;
			shapeStack.add(temporaryPath);
		}
	}

	/**
	 * When the stroke stops, adds the shape, and keeps x and y coordinate.
	 */

	public void stopPath(float x, float y)
	{
		Log.i("Draw", "Finished a free path");
		FreePath temporaryPath = (FreePath) shapeStack.get(shapeStack.size() - 1);
		temporaryPath.lineTo(x, y);
		currentX = x;
		currentY = y;
		shapeStack.add(temporaryPath);
	}

	public void clear()
	{
		path.reset();
		shapeStack.clear();
		shapePosition.clear();
		bitmap = null;
		;
		invalidate();
	}


	/**
	 * Undos the last position or clears completely
	 */

	public void undo()
	{

		if (shapeStack.size() >= 1 && shapePosition.size() >= 2)
		{

			int startPos = shapePosition.pop();
			int stopPos = shapePosition.pop();
			for (int i = startPos; i > stopPos; i--)
			{
				shapeStack.pop();
			}
			shapePosition.push(stopPos);
			invalidate();
		}
		else
		{
			clear();
		}
	}
}

interface Shape
{
	// Get's fill color
	int getColor();

	int getThickness();

	int fillColor = 1;
	int strokeColor = 0;

	void draw(Canvas canvas, Paint paint);

	int getFillColor();

	void setFillColor(int fillColor);

	int getStrokeColor();

	void setStrokeColor(int strokeColor);

	void onDraw(MotionEvent event);

	int getPaintToUse();
}
