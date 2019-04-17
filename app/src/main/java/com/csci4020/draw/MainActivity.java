package com.csci4020.draw;
/**
 * CSCI 4020
 * ASSIGNMENT 4
 * HANNIE KIM
 * BRIAN LAKE
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), DrawActivity.class);
				startActivity(intent);
			}
		});
	}
}
