package com.hourstack.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.hourstack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Rahil on 02-11-2015.
 */
public class UIUtil {
    private static ProgressDialog progressDialog;

    public static boolean checkNetwork(Context context) {
        boolean wifiAvailable = false;
        boolean mobileAvailable = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    wifiAvailable = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    mobileAvailable = true;
        }
        return wifiAvailable || mobileAvailable;
    }

    public static void log(String message) {
        Log.e("TheHighLife", message);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showDialog(Context context) {


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog.show();
    }

    public static void showDialogCustomString(Context context, String str) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(str);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void changeDialogString(String str) {
        progressDialog.setMessage(str);
    }

    public static void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static String getTrimmedStringFromAPI(String value) {

        if (value == null) {
            return "";
        } else if (value.equals("null") || value.equals("Null") || value.equals("NULL")) {

            return "";
        } else if (value.equals("")) {

            return "";
        } else {
            return value;
        }
    }



    public static void showCustomDialog(String title, String str, Activity activity) {
        // custom dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_simple_dailog_ok, null);
        dialogBuilder.setView(dialogView);

        TextView txt_message_dialog = (TextView) dialogView.findViewById(R.id.txt_message_dialog);
        txt_message_dialog.setText(str);

        TextView txt_title_dialog = (TextView) dialogView.findViewById(R.id.txt_title_dialog);
        txt_title_dialog.setText(title);

        TextView btn_ok = (TextView) dialogView.findViewById(R.id.btn_ok);

        Font.setupFont(activity, btn_ok, Font.Montserrat_Bold);
        Font.setupFont(activity, txt_title_dialog, Font.Montserrat_Light);
        Font.setupFont(activity, txt_message_dialog, Font.Montserrat_Light);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }




    public static String convertDateTime(String fromFormat, String toFormat, String dateOriginalGot) {

        try {
            //SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //// Getting Source format here
            SimpleDateFormat fmt = new SimpleDateFormat(fromFormat);

            fmt.setTimeZone(TimeZone.getDefault());

            Date date = fmt.parse(dateOriginalGot);

            //SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, MMM d, ''yyyy");

            //// Setting Destination format here
            SimpleDateFormat fmtOut = new SimpleDateFormat(toFormat);

            return fmtOut.format(date);

        } catch (Exception e) {

            e.printStackTrace();

            e.getMessage();

        }

        return "";

    }




    public static String convertDateonly(String toFormat, String dateOriginalGot) {

        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");



            fmt.setTimeZone(TimeZone.getDefault());

            Date date = fmt.parse(dateOriginalGot);

            //SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, MMM d, ''yyyy");

            //// Setting Destination format here
            SimpleDateFormat fmtOut = new SimpleDateFormat(toFormat);

            return fmtOut.format(date);

        } catch (Exception e) {

            e.printStackTrace();

            e.getMessage();

        }

        return "";

    }


    public static String convertDate(String toFormat, String dateOriginalGot) {

        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



            fmt.setTimeZone(TimeZone.getDefault());

            Date date = fmt.parse(dateOriginalGot);

            //SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, MMM d, ''yyyy");

            //// Setting Destination format here
            SimpleDateFormat fmtOut = new SimpleDateFormat(toFormat);

            return fmtOut.format(date);

        } catch (Exception e) {

            e.printStackTrace();

            e.getMessage();

        }

        return "";

    }
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param picturePath
     * @return
     */
    public static int getImageAngle(String picturePath) {
        try {
            ExifInterface ei = new ExifInterface(picturePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.i("TEST", "orientation : " + 90);
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.i("TEST", "orientation : " + 180);
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.i("TEST", "orientation : " + 270);
                    return 270;
                default:
                    Log.i("TEST", "orientation : " + 0);
                    return 0;
            }

        } catch (IOException e) {
            Log.e("TEST", "" + e.getMessage());
            return 0;
        }

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
    }
    public static File createImageFile() throws IOException {
        File myDir = new File(Environment.getExternalStorageDirectory() + "/" + "TheHighLife");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, "TheHighLife" + System.currentTimeMillis() + ".jpg");
        return file;
    }




    public static String getCommaSapratedStringFromList(List<String> list) {
        // Converting ArrayList to String in Java using advanced for-each loop
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str).append(","); //separating contents using semi comma
        }

        String strfromArrayList = sb.toString();

        if (list.size() > 0) {

            strfromArrayList = strfromArrayList.substring(0, strfromArrayList.length() - 1);

        }


        return strfromArrayList;
    }








    public static int get_device_height(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int height = metrics.heightPixels;

        return height;
    }


    public static int get_device_width(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int width = metrics.widthPixels;

        return width;
    }

    public static int getStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }



    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
