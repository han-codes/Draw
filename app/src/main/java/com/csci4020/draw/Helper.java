package com.csci4020.draw;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Helper {

    public static float convertSpToPx(int sp, Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics);
    }

    public static float convertDpToPx(int dp, Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
}
