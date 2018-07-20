package com.hourstack.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hourstack.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AppUtil {



    static ProgressDialog progressDialog;

    public static RequestBody getImageRequestBody(File file) {
        RequestBody imagebody = null;
        try {
            imagebody = RequestBody.create(MediaType.parse("*/*"), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagebody;
    }

    public static RequestBody getImageRequestBodyPNG(File file) {
        RequestBody imagebody = null;
        try {
            imagebody = RequestBody.create(MediaType.parse("image/png"), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagebody;
    }

    public static RequestBody getStringRequestBody(String path) {
        RequestBody strbody = null;
        try {
            strbody = RequestBody.create(MediaType.parse("text/plain"), path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strbody;
    }


    public static String getStringRequestBody(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void Toast(Context context, String text) {
        if (context != null) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }
    }

    public static String getMonthNumber(String monthName) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("MMM").parse(monthName));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int monthInt = cal.get(Calendar.MONTH) + 1;
        return String.valueOf(monthInt);
    }

    public static boolean isValidMail(String mailString) {
        return !TextUtils.isEmpty(mailString) && (android.util.Patterns.EMAIL_ADDRESS.matcher(mailString).matches());
    }

    public static boolean isValidPhone(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static String getAndroidId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

//    public static void DisplayMessageStatus(Context c, String message) {
//        // TODO Auto-generated method stub
//
//        final Dialog dialog1 = new Dialog(c,  R.style.ProgressHUD);
//
//        // tell the Dialog to use the dialog.xml as it's layout description
//        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog1.setContentView(R.layout.custom_alert_status);
//
//
//        LinearLayout linear_ok = (LinearLayout) dialog1.findViewById(R.id.linear_ok);
//
//        TextView tv_message_details = (TextView) dialog1.findViewById(R.id.tv_message_details);
//        tv_message_details.setText(message);
//
//
//        linear_ok.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                dialog1.dismiss();
//
//            }
//        });
//
//
//        dialog1.show();
//    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    public static String convertDateFormate(String date, String inputFormate, String outputFormate) {
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormate);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormate);
        String newFormat = formatter.format(testDate);
        return newFormat;
    }

    public static String convertDateToTimestamp(String str_date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);
            System.out.println("Timestamp>> " + date.getTime());
            return String.valueOf(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
    }

    public static String getDateTimeInMillis() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        return cal.getTimeInMillis() + "";
    }

    public static String getDateMonth() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        String date = DateFormat.format("yyyy/MMM/dd", cal.getTimeInMillis()).toString();
        return date;
    }

    public static String getDateFromTimestamp(long timestamp) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000);
        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        //Date parsed = format.parse("2011-03-01 15:10:37"); // => Date is in UTC now
        TimeZone tz = TimeZone.getDefault();
        Date parsed = null;
        try {
            parsed = sourceFormat.parse(sourceFormat.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        if (parsed != null) {
            SimpleDateFormat destFormat = new SimpleDateFormat("dd/MM/yyyy");
            destFormat.setTimeZone(tz);
            result = destFormat.format(parsed);
        }
        return result;

    }

    public static String getTimestampFromDate(String datestring) {

        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        c.set(Calendar.YEAR, Integer.parseInt(datestring.split("/")[2]));
        c.set(Calendar.MONTH, Integer.parseInt(datestring.split("/")[1]) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datestring.split("/")[0]));
        return "" + (c.getTimeInMillis() / 1000L);

    }


//    public static void resizeImage(final Context context, final String path, final ResizeImageListener listener) {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... voids) {
//                try {
//                    return getRightAngleImage(context, path);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//                return "";
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                listener.onImageResize(s);
//            }
//        }.execute();
//    }

    public static String getRightAngleImage(Context context, String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(context, degree, photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }

    private static String rotateImage(Context context, int degree, String path) {
        String imagePath = "";
        try {
            Bitmap b = BitmapFactory.decodeFile(path);
            Bitmap scaled = scaleDown(b, 800, true);
            if (degree > 0 && scaled.getWidth() > scaled.getHeight()) {
                Matrix matrix = new Matrix();
                matrix.setRotate(degree);
                scaled = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);
            }
            imagePath = getAppStoragePath(context, "") + "/temp_" /*+ Calendar.getInstance().getTimeInMillis()*/ + ".jpg";
            FileOutputStream out = new FileOutputStream(new File(imagePath));
            scaled.compress(Bitmap.CompressFormat.JPEG, 85, out);
            out.flush();
            out.close();
            scaled.recycle();
            b.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    private static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        Bitmap newBitmap;
        if (realImage.getWidth() > maxImageSize || realImage.getHeight() > maxImageSize) {
            float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
            int width = Math.round(ratio * realImage.getWidth());
            int height = Math.round(ratio * realImage.getHeight());
            newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        } else {
            newBitmap = Bitmap.createScaledBitmap(realImage, realImage.getWidth(), realImage.getHeight(), filter);
        }
        return newBitmap;
    }

    public static String getAppStoragePath(Context context, String dir) {
        String path;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name);
        } else {
            path = context.getFilesDir() + "/" + context.getString(R.string.app_name);
        }

        String directory = +dir.trim().length() > 0 ? (dir) : "";
        File file = new File(path + directory);
        if (!file.exists())
            file.mkdirs();

        return file.getAbsolutePath();
    }

    public static boolean isNumeric(String str) {
        try {
            if (str.contains("/")) {
                return true;
            }
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Log.i("", "Encrpted: -> " + hexString);
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch (NumberFormatException nfe) {
                //System.out.println("Could not parse " + nfe);
                Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
            }
        }
        return result;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
                && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public static boolean isPasswordValid(final String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
