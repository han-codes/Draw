package com.csci4020.draw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Stack;

public class DrawActivity extends Activity implements RadioGroup.OnCheckedChangeListener
{
	PaintArea paintArea;

	private final static String KEY_SHAPES = "KEY_SHAPES";
	private final static String KEY_SHAPE_POSITIONS = "KEY_SHAPE_POSITIONS";
	private final static String KEY_BITMAP = "KEY_BITMAP";

	private final static int REQUEST_PHOTO = 100;

	File file;
	File publicFile = null;
	File publicDirectory;
	String fileLocation = null;
	FileOutputStream publicFos;

	AlertDialog stickerAlert;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_draw);

		// Identify the paint area
		paintArea = findViewById(R.id.paintArea);
		paintArea.setDrawingCacheEnabled(true);

		setUpButtonClickListeners();

		if (savedInstanceState != null)
		{
			paintArea.setShapes((Stack) savedInstanceState.getSerializable(KEY_SHAPES));
			paintArea.setShapePositions((Stack) savedInstanceState.getSerializable(KEY_SHAPE_POSITIONS));

			// bitmap
			Bitmap bmp = null;
			String filename = savedInstanceState.getString(KEY_BITMAP, "");

			if (!filename.equals(""))
			{
				try
				{
					FileInputStream fis = this.openFileInput(filename);
					bmp = BitmapFactory.decodeStream(fis);
					fis.close();
//                    Log.i(TAG_DRAW_ACT, "bmp = " + bmp);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				paintArea.setNewImage(bmp, bmp);
			}
		}
	}

	private void setUpButtonClickListeners()
	{

		findViewById(R.id.open_file_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				openImage();
			}
		});

		findViewById(R.id.save_file_Button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				saveImage();
			}
		});

		findViewById(R.id.send_message_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				emailImage();
			}
		});

		findViewById(R.id.undo_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (paintArea != null)
				{
					paintArea.undo();
				}
			}
		});


		findViewById(R.id.constraintLayout_current_color).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});

		findViewById(R.id.new_page_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (paintArea != null)
				{
					paintArea.clear();
				}
			}
		});


		findViewById(R.id.color_picker_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});

		findViewById(R.id.new_page_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});

		// set listeners to Radio Group
		RadioGroup radioGroup = findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);

		findViewById(R.id.radioButton_sticker).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				paintArea.setCurrentTool(PaintArea.STICKER_FEATURE);
				stickerAlert.show();
			}
		});

		findViewById(R.id.radioButton_frame).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				paintArea.toggleDrawingFrame();
			}
		});

		publicDirectory = getPublicFile();
	}

	/**
	 * Determine which radioButton feature is checked by the user
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		PaintArea paintArea = findViewById(R.id.paintArea);

		switch (checkedId)
		{
			case R.id.radioButton_brush:
				paintArea.setCurrentTool(PaintArea.BRUSH_FEATURE);
				break;
			case R.id.radioButton_line:
				paintArea.setCurrentTool(PaintArea.LINE_FEATURE);
				break;
			case R.id.radioButton_square:
				paintArea.setCurrentTool(PaintArea.RECTANGLE_FEATURE);
				break;
			case R.id.radioButton_sticker:
				break;
			case R.id.radioButton_frame:
				break;
		}
	}

	private void openImage()
	{
		Intent choosePictureIntent = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, REQUEST_PHOTO);
	}

	private void saveImage()
	{

		this.fileLocation = MediaStore.Images.Media.insertImage(getContentResolver(), paintArea.getDrawingCache(), "title.png", "desc123");

		Log.i("File Location", "this.fileLocation = " + this.fileLocation);

		Toast.makeText(getApplicationContext(), "Saved to Gallery", Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("SetWorldReadable")
	public void emailImage()
	{
		saveImagePublic();
		if (this.publicFile != null)
		{
			this.publicFile.setReadable(true, false);
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("text/plain");
			emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(this.publicFile));
			startActivity(Intent.createChooser(emailIntent, "Select app to send this image."));
		}
	}

	public void saveImagePublic()
	{
		File publicDir = getPublicAlbumStorageDirectory("Emailed Photos");
		String filename = "myfile.png";
		this.publicFile = new File(publicDir, filename);
		try
		{
			this.publicFos = new FileOutputStream(publicFile);
			Bitmap bitmapFromView = paintArea.getDrawingCache();
			bitmapFromView.compress(Bitmap.CompressFormat.PNG, 100, this.publicFos);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private File getPublicFile()
	{
		return file;
	}

	public File getPublicAlbumStorageDirectory(String albumName)
	{
		// Get the directory for the user's public pictures directory.
		File file = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), albumName);
		if (!file.mkdirs())
		{
			Log.e("StorageDirectory", "Directory not created");
		}
		return file;
	}


	// MARK: - Dealing with stickers
}
