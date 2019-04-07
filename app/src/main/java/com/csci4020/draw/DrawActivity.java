package com.csci4020.draw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Stack;

import abak.tr.com.boxedverticalseekbar.BoxedVertical;

public class DrawActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private final static int REQUEST_PHOTO = 100;
    private final static int REQUEST_EMAIL = 101;

    private final static String KEY_SHAPES = "KEY_SHAPES";
    private final static String KEY_SHAPE_POSITIONS = "KEY_SHAPE_POSITIONS";
    private final static String KEY_BITMAP = "KEY_BITMAP";

    File file;
    File publicFile = null;
    File publicDirectory;
    String fileLocation = null;
    FileOutputStream publicFos;

    Bitmap bitmap;
    Bitmap alteredBitmap;

    AlertDialog stickerAlert;
    PaintArea paintArea;
    BoxedVertical brushThickness;
    ConstraintLayout colorIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);

        paintArea = findViewById(R.id.paintArea);
        paintArea.setDrawingCacheEnabled(true);

        setUpButtonClickListeners();

        if (savedInstanceState != null) {
            paintArea.setShapes((Stack) savedInstanceState.getSerializable(KEY_SHAPES));
            paintArea.setShapePositions((Stack) savedInstanceState.getSerializable(KEY_SHAPE_POSITIONS));

            Bitmap bitmap = null;
            String filename = savedInstanceState.getString(KEY_BITMAP, "");

            if (!filename.equals("")) {
                try {
                    FileInputStream fileInputStream = this.openFileInput(filename);
                    bitmap = BitmapFactory.decodeStream(fileInputStream);
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                paintArea.setNewImage(bitmap, bitmap);
            }
        }
    }

    private void setUpButtonClickListeners() {

        findViewById(R.id.open_file_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        findViewById(R.id.save_file_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        findViewById(R.id.send_message_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailImage();
            }
        });

        findViewById(R.id.undo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paintArea != null) {
                    paintArea.undo();
                }
            }
        });

        final AlertDialog alert = setupColorDialog();

        findViewById(R.id.color_picker_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 alert.show();
            }
        });

        findViewById(R.id.constraintLayout_current_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 alert.show();
            }
        });

        findViewById(R.id.new_page_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paintArea != null) {
                    paintArea.clear();
                }
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        stickerAlert = setupStickerDialog();

        findViewById(R.id.radioButton_sticker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintArea.setCurrentTool(TOOLS.STICKER.getNumbericType());
                stickerAlert.show();
            }
        });

        findViewById(R.id.radioButton_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintArea.toggleDrawingFrame();
            }
        });



        publicDirectory = getPublicFile();

        brushThickness = findViewById(R.id.boxedvertical_thickness);

        brushThickness.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
            @Override
            public void onPointsChanged(BoxedVertical boxedVertical, int i) {
                paintArea.setStrokeThickness(i);
            }

            @Override
            public void onStartTrackingTouch(BoxedVertical boxedVertical) {

            }

            @Override
            public void onStopTrackingTouch(BoxedVertical boxedVertical) {

            }
        });

        colorIndicator = findViewById(R.id.constraintLayout_current_color);
        colorIndicator.setBackgroundColor(paintArea.getColor());
        brushThickness.setValue(5);
    }

    private AlertDialog setupStickerDialog(){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View stickerAlert = inflater.inflate(R.layout.sticker_alert, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);

        builder.setView(stickerAlert);
        builder.setTitle("Select Sticker");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                RadioGroup r = stickerAlert.findViewById(R.id.radioGroup_sticker);

                int sticker = 1;
                switch (r.getCheckedRadioButtonId()){
                    case R.id.radioButton_star: sticker = PaintArea.STAR_STICKER;
                        break;
                    case R.id.radioButton_leaf: sticker = PaintArea.STICKER_LEAF;
                        break;
                    case R.id.radioButton_lee: sticker = PaintArea.STICKER_LEE;
                        break;
                }

                paintArea.setSticker(sticker);
            }
        });

        AlertDialog alert = builder.create();
        return alert;
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
                paintArea.setCurrentTool(TOOLS.BRUSH.getNumbericType());
                break;
            case R.id.radioButton_line:
                paintArea.setCurrentTool(TOOLS.LINE.getNumbericType());
                break;
            case R.id.radioButton_square:
                paintArea.setCurrentTool(TOOLS.RECTANGLE.getNumbericType());
                break;
            case R.id.radioButton_sticker:
                paintArea.setCurrentTool(TOOLS.STICKER.getNumbericType());
                break;
            case R.id.radioButton_frame:
                break;
        }
    }
    private AlertDialog setupColorDialog(){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View alertView = inflater.inflate(R.layout.color_alert, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);

        final EditText r = alertView.findViewById(R.id.editText_red);
        final EditText g = alertView.findViewById(R.id.editText_green);
        final EditText b = alertView.findViewById(R.id.editText_blue);
        final View colorShow = alertView.findViewById(R.id.colorShow);

        setupEditTexts(r, g, b, colorShow, alertView);
        setupSeekBars(alertView, r, g, b, colorShow);


        builder.setView(alertView);
        builder.setTitle("Choose a color");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                verifyEditText(r);
                verifyEditText(g);
                verifyEditText(b);

                int color = Helper.rgbToHex(r, g, b);

                // set the stroke on the drawing image
                paintArea.setColor(color);

                // update the color indicator on screen
                findViewById(R.id.constraintLayout_current_color).setBackgroundColor(color);
            }
        });

        AlertDialog alert = builder.create();
        return alert;
    }

    private void verifyEditText(EditText e){
        if (e.getText().toString().equals("")){
            e.setText("0");
        }
    }


        private void setupEditTexts(final EditText r, final EditText g, final EditText b, final View colorShow, View alertView){
        class TextListener implements TextWatcher {
            EditText e;
            SeekBar s;

            TextListener(EditText e, SeekBar s){
                this.e = e;
                this.s = s;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    try {
                        if (Integer.parseInt(editable.toString()) > 255) {
//                            Helpers.makeToast("Max value is 255.", getApplicationContext());
                            e.setText("255");
                        }
                        if (Integer.parseInt(editable.toString()) < 0) {
                            // Helpers.makeToast("Min value is 0.", getApplicationContext());
                            e.setText("0");
                        }
                    } catch (NumberFormatException e2) {
                        e2.printStackTrace();
                    }

                    s.setProgress(Integer.parseInt(e.getText().toString()));
                    e.setSelection(e.getText().length());
                    updateColorShow(colorShow, r, g, b);
                }
            }
        };
            class focusChangeListener implements View.OnFocusChangeListener {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus){
                        EditText e = (EditText) view;
                        if (e.getText().toString().equals("")) {
                            e.setText("0");
                        }
                    }
                }
            };

            r.addTextChangedListener(new TextListener(r, (SeekBar) alertView.findViewById(R.id.seekBar_r)));
            r.setOnFocusChangeListener(new focusChangeListener());
            g.addTextChangedListener(new TextListener(g, (SeekBar) alertView.findViewById(R.id.seekBar_g)));
            g.setOnFocusChangeListener(new focusChangeListener());
            b.addTextChangedListener(new TextListener(b, (SeekBar) alertView.findViewById(R.id.seekBar_b)));
            b.setOnFocusChangeListener(new focusChangeListener());
        }

    private void setupSeekBars(View alertView, final EditText r, final EditText g, final EditText b, final View colorShow){
        class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
            EditText e;

            public SeekBarListener(EditText e) {
                this.e = e;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b2) {
                e.setText(Integer.toString(value));
                updateColorShow(colorShow, r, g, b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        ((SeekBar)alertView.findViewById(R.id.seekBar_r)).setOnSeekBarChangeListener(new SeekBarListener(r));
        ((SeekBar)alertView.findViewById(R.id.seekBar_g)).setOnSeekBarChangeListener(new SeekBarListener(g));
        ((SeekBar)alertView.findViewById(R.id.seekBar_b)).setOnSeekBarChangeListener(new SeekBarListener(b));
    }

    private void updateColorShow(View colorShow, EditText r, EditText g, EditText b){
        colorShow.setBackgroundColor(Helper.rgbToHex(r, g, b));
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

    private File getPublicFile()
    {
        String albumName = "TEST ALBUM";
        File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

        return file;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if ( requestCode == REQUEST_PHOTO ){
                try {
                    Uri dataUri = data.getData();
                    BitmapFactory.Options bfo = new BitmapFactory.Options();
                    bfo.inJustDecodeBounds = true;
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(dataUri), null, bfo);
                    bfo.inJustDecodeBounds = false;
                    bfo.inMutable = true;
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(dataUri), null, bfo);
                    alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

                    paintArea.setNewImage(alteredBitmap, bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
