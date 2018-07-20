package com.hourstack.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Admin on 4/30/2016.
 */
public class Font {

    public static final String SinkinSans_threelight = "SinkinSans_threeLight.otf";
    public static final String Montserrat_Black = "Montserrat_Black.otf";
    public static final String Montserrat_Bold = "Montserrat_Bold.ttf";
    public static final String Montserrat_ExtraBold = "Montserrat_ExtraBold.otf";
    public static final String Montserrat_Light = "Montserrat_Light.otf";
    public static final String Montserrat_Regular = "Montserrat_Regular.otf";
    public static final String Montserrat_SemiBold = "Montserrat_SemiBold.otf";
    public static final String Montserrat_UltraLight = "Montserrat_UltraLight.otf";



    public static void setupFont(Context context, TextView textView, String fontName) {
        if (fontName == null) {
            return;
        }
        Typeface tf = FontCache.get(fontName, context);
        if (tf != null) {
            textView.setTypeface(tf);
        }
    }

    public static void setupFont(Context context, Button button, String fontName) {
        if (fontName == null) {
            return;
        }
        Typeface tf = FontCache.get(fontName, context);
        if (tf != null) {
            button.setTypeface(tf);
        }
    }


    public static void setupFont(Context context, EditText editText, String fontName) {
        if (fontName == null) {
            return;
        }
        Typeface tf = FontCache.get(fontName, context);
        if (tf != null) {
            editText.setTypeface(tf);
        }

//        Typeface font = Typeface.createFromAsset(context
//                .getAssets(), fontName);
//
//        editText.setTypeface(font);
    }
}
