package com.csci4020.draw;
/**
 * CSCI 4020
 * ASSIGNMENT 4
 * HANNIE KIM
 * BRIAN LAKE
 */

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

public class Helper
{

	public static float convertDpToPx(int dp, Context context)
	{
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
	}

	//takes in three edit texts and gives back a color based on the values in there
	public static int rgbToHex(EditText er, EditText eg, EditText eb)
	{
		int r, g, b;

		try
		{
			r = Integer.parseInt(er.getText().toString());
		} catch (NumberFormatException e)
		{
			r = 0;
		}

		try
		{
			g = Integer.parseInt(eg.getText().toString());
		} catch (NumberFormatException e)
		{
			g = 0;
		}

		try
		{
			b = Integer.parseInt(eb.getText().toString());
		} catch (NumberFormatException e)
		{
			b = 0;
		}

		return Color.rgb(r, g, b);

	}
}
