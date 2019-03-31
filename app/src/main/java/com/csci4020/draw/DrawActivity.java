package com.csci4020.draw;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioGroup;

public class DrawActivity extends Activity implements RadioGroup.OnCheckedChangeListener
{
	PaintArea paintArea;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_draw);

		// Identify the paint area
		paintArea = findViewById(R.id.paintArea);
		paintArea.setDrawingCacheEnabled(true);

		setUpButtonClickListeners();
	}

	private void setUpButtonClickListeners()
	{

		findViewById(R.id.open_file_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

			}
		});

		findViewById(R.id.save_file_Button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

			}
		});

		findViewById(R.id.send_message_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

			}
		});

		findViewById(R.id.undo_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

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

			}
		});

		findViewById(R.id.radioButton_frame).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});
	}

	/**
	 * Determine which radioButton feature is checked by the user
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{

        switch (checkedId)
		{
			case R.id.radioButton_brush:
				paintArea.setCurrentTool(TOOLS.BRUSH);
				Log.i("TOOL", "SELECTED BRUSH");
				break;
			case R.id.radioButton_line:
				paintArea.setCurrentTool(TOOLS.LINE);
				break;
			case R.id.radioButton_square:
				paintArea.setCurrentTool(TOOLS.RECTANGLE);
				break;
		}
	}
}
