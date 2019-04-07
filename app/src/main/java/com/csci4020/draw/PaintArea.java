package com.csci4020.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

//enum TOOLS
//{
//	BRUSH,
//	RECTANGLE,
//	LINE,
//	STICKER
//}

//enum PAINT_STYLE
//{
//	FILL_ONLY,
//	STROKE_ONLY,
//	FILL_AND_STROKE
//}

public class PaintArea extends View
{
	// Each RadioButton feature is given an int value
	public static final int BRUSH_FEATURE = 1;
	public static final int LINE_FEATURE = 2;
	public static final int RECTANGLE_FEATURE = 3;
	public static final int STICKER_FEATURE = 4;

	// Initial selected feature is the brush
//	private int currentFeature = BRUSH_FEATURE;
	private int currentFeature = BRUSH_FEATURE;
	private int currentHeight;
	private int currentWidth;
	private float currentX;
	private float currentY;


	private Random rand;

//	private TOOLS currentTool = TOOLS.BRUSH;
	private int currentTool = BRUSH_FEATURE;

//	private int color = 0xFFCCCCCC;
	private int color;
//	private int strokeWidth = 2;
 	private int thickness = 2;
	private Paint backgroundPaint;
	private Paint mainPaint;
	private Paint linePaint;
	private Path path;
	Canvas canvas;
	Matrix matrix;
	Bitmap bitmap;

	Bitmap outsideFrame;
	Bitmap currentBitmap;
	Bitmap stickerStar;
	Bitmap stickerLee;
	Bitmap stickerLeaf;


	public static final int STAR_STICKER = 1;
    public static final int STICKER_LEAF = 2;
    public static final int STICKER_LEE = 3;



//	Stack<Shape> shapeStack = new Stack<>();
	Stack<Shape> shapes;
	public Stack<Integer> shapePosition;
	ArrayList<Path> paths;



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

		rand = new Random();
		shapes = new Stack<>();
		matrix = new Matrix();
		paths = new ArrayList<>();
		shapePosition = new Stack<>();
		this.bitmap = null;

		backgroundPaint = new Paint();
		backgroundPaint.setColor(0xffffffff);
		backgroundPaint.setStyle(Paint.Style.FILL);

		color = 0xff00f0f0;
		setStrokeThickness(5);
		mainPaint = new Paint();
		mainPaint.setColor(color);
		mainPaint.setStyle(Paint.Style.FILL);

		linePaint = new Paint();
		linePaint.setColor(color);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeWidth(thickness);
		linePaint.setStrokeJoin(Paint.Join.ROUND);


		path = new Path();

		setupStickerBitmaps();
	}

	/**
	 * Setup for using stickers
	 */

	// TODO: FINISH SETUP
	private void setupStickerBitmaps()
	{
//		Drawable androidDrawable = getResources().getDrawable((R.drawable.star));
//
//		int size = (int) Helper.convertDpToPx(50, getContext());
//
//		stickerStar = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//
//		Canvas canvas = new Canvas(stickerStar);
//		androidDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//		androidDrawable.draw(canvas);
//
//		currentBitmap = stickerStar;

        Drawable androidDrawable = getResources().getDrawable((R.drawable.star));

        int size = (int) Helper.convertDpToPx(50, getContext());
        stickerStar = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(stickerStar);
        androidDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        androidDrawable.draw(canvas);


        Drawable leeDrawable = getResources().getDrawable((R.drawable.lee));

        stickerLee = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas2 = new Canvas(stickerLee);
        leeDrawable.setBounds(0, 0, canvas2.getWidth(), canvas2.getHeight());
        leeDrawable.draw(canvas2);



        Drawable leafDrawable = getResources().getDrawable((R.drawable.leaves));

        stickerLeaf = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas3 = new Canvas(stickerLeaf);
        leafDrawable.setBounds(0, 0, canvas3.getWidth(), canvas3.getHeight());
        leafDrawable.draw(canvas3);

        currentBitmap = stickerStar;

    }


	/**
	 * Set current bitmap to selected sticker
	 */

	public void setSticker(int stickerId)
	{

        switch(stickerId){
            case STAR_STICKER:
                currentBitmap = stickerStar;
                break;
            case STICKER_LEAF:
                currentBitmap = stickerLeaf;
                break;
            case STICKER_LEE:
                currentBitmap = stickerLee;
                break;
        }
	}

	//
	// MARK: - Getters/Setters
	//
	public void setStrokeThickness(int dpSize)
	{
		thickness = (int) Helper.convertDpToPx(dpSize, getContext());
	}



	public int getColor() {
		if (color == -1){
			return rand.nextInt(0x1000000) + 0xff000000;
		} else {
			return color;
		}
	}

	public void setColor(int color) {
		this.color = color;
		this.linePaint.setColor(color); // sometimes drawing goes in the previous color
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public int getCurrentTool() {
		return currentTool;
	}

	public void setCurrentTool(int currentTool) {
		this.currentTool = currentTool;
	}

	public Stack<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(Stack<Shape> shapes) {
		this.shapes = shapes;
		invalidate();
	}

	public Stack<Integer> getShapePositions() {
		return shapePosition;
	}

	public void setShapePositions(Stack<Integer> shapePositions) {
		this.shapePosition = shapePositions;
		invalidate();
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
		for (Shape s : shapes)
		{
			// TODO THIS IS CHECKING IF IT IS PAINTING A FILL OR STROKE
			if (s.getPaintToUse() == Shape.PAINT_FILL)
			{
				Log.i("Draw","Fill only is the paint style");
				// TODO THIS SEEMS LIKE IT'S DUPLICATING THE SAME STEP. ??
				mainPaint.setColor(s.getColor());
				s.draw(canvas, mainPaint);
			}
			else if (s.getPaintToUse() == Shape.PAINT_STROKE)
			{
				Log.i("Draw","Stroke only is the paint style");
				linePaint.setColor(s.getColor());
				Log.i("Draw","Stroke Color: " + linePaint.getColor());
				linePaint.setStrokeWidth(s.getThickness());
				Log.i("Draw","Stroke thickness: " + linePaint.getStrokeWidth());
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
		currentHeight = h;
		currentWidth = w;

		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
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
		int dpPixel = 100;
		float actualPixels = Helper.convertDpToPx(dpPixel, getContext());

//		int width;
//		int height;

//		int desiredWidth = (int) Helper.convertDpToPx(100, getContext());
//		int desiredHeight = (int) Helper.convertDpToPx(100, getContext());

		int desiredWidth = (int) actualPixels;
		int desiredHeight = (int) actualPixels;

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int width;
		int height;

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

	private boolean isDrawing = false;

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
//		if (event.getAction() == MotionEvent.ACTION_DOWN)
//		{
//			performClick();
//		}
//
//		float x = event.getX();
//		float y = event.getY();
//
//		switch (currentTool)
//		{
//			case BRUSH_FEATURE:
//				switch (event.getAction())
//				{
//					case MotionEvent.ACTION_DOWN:
//						startPath(x, y);
//						invalidate();
//						break;
//					case MotionEvent.ACTION_MOVE:
//						continuePath(x, y);
//						invalidate();
//						break;
//					case MotionEvent.ACTION_UP:
//						stopPath(x, y);
//						shapePosition.push(shapes.size());
//						invalidate();
//						break;
//				}
//				break;
//			case RECTANGLE_FEATURE:
//				onDrawRectangle(event);
//			case LINE_FEATURE:
//				Log.i("Draw","Going to use the line");
//				onDrawLine(event);
//			case STICKER_FEATURE:
//				onDrawSticker(event);
//		}
//
//		return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            performClick();         //needed by android studio to handle normal click event stuff
        }

        float x = event.getX();
        float y = event.getY();

//        Log.i(TAG_PICT_DRAW, "x = " + x + ", y = " + y);

        if (currentTool == BRUSH_FEATURE) {
            switch (event.getAction()) {
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
//                    compressDrawnLines();
                    shapePosition.push(shapes.size());
                    invalidate();
                    break;
            }
        }

        if (currentTool == RECTANGLE_FEATURE){
            onDrawRectangle(event);
        }
        if (currentTool == LINE_FEATURE){
            onDrawLine(event);
        }
        if (currentTool == STICKER_FEATURE){
            onDrawSticker(event);
        }

        return true;
	}



//	public void drawShape(Shape shape, MotionEvent event)
//	{
//		shape.onDraw(event);
//		invalidate();
//	}
//
//	public void setCurrentTool(int currentTool)
//	{
//		Log.i("Draw", "Current tool set to " + currentTool);
//		this.currentTool = currentTool;
//	}
//
//	public void setShapes(Stack<Shape> shapeStack)
//	{
//		this.shapeStack = shapeStack;
//		invalidate();
//	}
//
//	public void setShapePosition(Stack<Integer> shapePosition)
//	{
//		this.shapePosition = shapePosition;
//		invalidate();
//	}

	//Create a line on first touch, and move the line while the user is dragging their finger around
	private void onDrawLine(MotionEvent event)
	{

//		switch (event.getAction())
//		{
//			case MotionEvent.ACTION_DOWN:
//				int x = (int) event.getX();
//				int y = (int) event.getY();
//
//				Line line = new Line(x, y, x + 1, y + 1, color, thickness);
//				shapes.push(line);
//				isDrawing = true;
//				Log.i("Draw","Action down ondrawline");
//			case MotionEvent.ACTION_UP:
//				isDrawing = false;
//				shapePosition.push(shapes.size());
//				Log.i("Draw","Action up ondrawline");
//			case MotionEvent.ACTION_MOVE:
//				if (isDrawing)
//				{
//					Log.i("Draw","Action move and drawing ondrawline");
//
////					((Line) shapes.peek()).setEndx(((int) event.getX()));
////					((Line) shapes.peek()).setEndy(((int) event.getY()));
//				}
//		}
//
//		invalidate();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Line line = new Line(x, y, x+1, y+1, color, thickness);
            shapes.push(line);
            isDrawing = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            isDrawing = false;
            shapePosition.push(shapes.size());
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (isDrawing) {
                ((Line) shapes.peek()).setEndx(((int) event.getX()));
                ((Line) shapes.peek()).setEndy(((int) event.getY()));
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

				Rectangle rect = new Rectangle(color, x, y, x + 1, y + 1);
				rect.setColor(color);
				shapes.push(rect);
				isDrawing = true;
			case MotionEvent.ACTION_UP:
				isDrawing = false;
				shapePosition.push(shapes.size());
			case MotionEvent.ACTION_MOVE:
				((Rectangle) shapes.peek()).setRight((int) event.getX());
				((Rectangle) shapes.peek()).setBottom((int) event.getY());
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
			shapes.push(sticker);
			isDrawing = true;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			isDrawing = false;
			shapePosition.push(shapes.size());
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (isDrawing)
			{
				((Sticker) shapes.peek()).setX(((int) event.getX()));
				((Sticker) shapes.peek()).setY(((int) event.getY()));
			}
		}
		invalidate();
	}

	public void setNewImage(Bitmap alteredBitmap, Bitmap bitmap)
	{

		this.bitmap = bitmap;

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
		MyPath temporaryPath = new MyPath();
		temporaryPath.setColor(color);
		temporaryPath.setThickness(thickness);
		temporaryPath.moveTo(x, y);
		currentX = x;
		currentY = y;
		shapes.add(temporaryPath);
	}

	public void continuePath(float x, float y)
	{
		Log.i("Draw", "Continuing the free path");
		MyPath temporaryPath = (MyPath)shapes.get(shapes.size() - 1);

		if (Math.abs(currentX - x) >= 4 || Math.abs(currentY - y) >= 4)
		{

			temporaryPath.quadTo(currentX, currentY, (x+currentX)/2, (y+currentY)/2);
			currentX = x;
			currentY = y;
			shapes.add(temporaryPath);
		}
	}

	/**
	 * When the stroke stops, adds the shape, and keeps x and y coordinate.
	 */

	public void stopPath(float x, float y)
	{
		Log.i("Draw", "Finished a free path");
		MyPath temporaryPath = (MyPath)shapes.get(shapes.size()-1);
		temporaryPath.lineTo(x, y);
		currentX = x;
		currentY = y;
		shapes.add(temporaryPath);
	}

	public void clear()
	{
		path.reset();
		shapes.clear();
		shapePosition.clear();
		bitmap = null;
		invalidate();
	}


	/**
	 * Undos the last position or clears completely
	 */

	public void undo()
	{

		MyPath path = null;

		if (shapes.size() >= 1 && shapePosition.size() >= 2)
		{

			int startPos = shapePosition.pop();
			int stopPos = shapePosition.pop();
			for (int i = startPos; i > stopPos; i--)
			{
				shapes.pop();
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

//==============================================================================================
//=     SHAPE CLASSES
//==============================================================================================
//Defines a shape, which must know how to draw itself when given a canvas and a paint,
// have a color, and know what type of paint to use.
// using this lets us have a stack of shapes that we can draw in order
interface Shape{
	void draw(Canvas canvas, Paint paint);
	int getColor();
	int getThickness();

	int PAINT_FILL = 1;
	int PAINT_STROKE = 0;
	int getPaintToUse();
}

//Basic wrapper class for Rect that lets it also hold a color
//==============================================================================================
//=     RECTANGLE
//==============================================================================================
class Rectangle implements Shape{
	private Rect rect;
	private int color;

	Rectangle(int color, int left, int top, int right, int bottom){
		rect = new Rect(left, top, right, bottom);
		this.color = color;
	}

	public void draw(Canvas canvas, Paint paint){
		canvas.drawRect(getRect(), paint);
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

	public int getColor(){
		return color;
	}

	public Rect getRect(){
		return rect;
	}

	public int getThickness(){return 1;};

	@Override
	public int getPaintToUse() {
		return PAINT_FILL;
	}

}

//==============================================================================================
//=     LINE
//==============================================================================================
class Line implements Shape{
	private int startx;
	private int starty;
	private int endx;
	private int endy;
	private int color;
	private int thickness;



	public Line(int startx, int starty, int endx, int endy, int color, int thickness) {
		this.startx = startx;
		this.starty = starty;
		this.endx = endx;

		this.endy = endy;
		this.color = color;
		this.thickness = thickness;
	}

	public void draw(Canvas canvas, Paint paint){
		canvas.drawLine(startx, starty, endx, endy, paint);
	}

	public void setEndx(int endx) {
		this.endx = endx;
	}

	public void setEndy(int endy) {
		this.endy = endy;
	}

	public int getThickness() {
		return thickness;
	}

	public int getColor(){
		return color;
	}

	@Override
	public int getPaintToUse() {
		return PAINT_STROKE;
	}
}

//==============================================================================================
//=     MYPATH
//==============================================================================================
class MyPath implements Shape {
	private Path path;
	private int color;
	private int thickness;

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setThickness(int thickness){
		this.thickness = thickness;
	}

	public MyPath(){
		this.path = new Path();
		this.color = 0xFF000000; // default to black
		this.thickness = 5;
	}

	public MyPath(Path path, int color, int thickness) {
		this.path = path;
		this.color = color;
		this.thickness = thickness;
	}

	public void draw(Canvas canvas, Paint paint){
		canvas.drawPath(path, paint);
	}

	@Override
	public int getColor() {
		return color;
	}

	public int getThickness(){return thickness;};

	@Override
	public int getPaintToUse() {
		return PAINT_STROKE;
	}

	public void moveTo(float x, float y){
		path.moveTo(x,y);
	}

	public void quadTo(float x, float y, float x2, float y2){
		path.quadTo(x,y,x2,y2);
	}

	public void lineTo(float x, float y){
		path.lineTo(x,y);
	}
}

//==============================================================================================
//=     STICKER
//==============================================================================================
class Sticker implements Shape {
	private int x, y;
	private Bitmap bitmap;

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bitmap, x, y, paint);
	}

	@Override
	public int getColor() {
		return 0xff000000;
	}

	@Override
	public int getThickness() {
		return 1;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Sticker(int x, int y, Bitmap bitmap) {

		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
	}

	@Override

	public int getPaintToUse() {
		return PAINT_FILL;
	}
}

//interface Shape
//{
//	// Get's fill color
//	int getColor();
//
//	int getThickness();
//
//	int fillColor = 1;
//	int strokeColor = 0;
//
//	void draw(Canvas canvas, Paint paint);
//
//	int getFillColor();
//
//	void setFillColor(int fillColor);
//
//	int getStrokeColor();
//
//	void setStrokeColor(int strokeColor);
//
//	void onDraw(MotionEvent event);
//
//	PAINT_STYLE getPaintStyle();
//}
