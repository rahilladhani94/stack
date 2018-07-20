package com.hourstack.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import com.hourstack.Activities.ActivitySplashScreen;
import com.hourstack.R;
import com.hourstack.Utils.Constants;
import com.hourstack.Utils.SessionManager;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;




public class service extends Service {

    SessionManager sessionManager;
    int counter;
    public static CountDownTimer timer;
    Context context;
    String reminder = "no";
    String overallocation = "no";
    String proximity = "no";

    Double latitude = 0.0, longitude = 0.0;
    @Override
    public void onCreate() {
        context = this;
        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getKeyCurrentseconds().length()==0){
            sessionManager.setKeyCurrentseconds("0");
            counter = 0;
        }
        else{
            counter = Integer.parseInt(sessionManager.getKeyCurrentseconds());
        }

        runtimer();
    }

    private void runtimer() {

        timer = new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished)
                    {

                    }

                    @Override
                    public void onFinish() {
                        try{
                        incrementConter();

                         timer.start();
                        }catch(Exception e){
                            Log.e("Error", "Error: " + e.toString());
                        }
                    }
                }.start();
    }

    private void incrementConter() {



        sessionManager.setKeyCurrentseconds(""+counter);
        if(sessionManager.getKeyTimerreminder().equalsIgnoreCase("yes")){

            if(sessionManager.getKeyTimerremindertime().length()>0)
            {
                int sessionseconds = Integer.parseInt(sessionManager.getKeyTimerremindertime());

                if(reminder.equalsIgnoreCase("no")){
                    if(counter>sessionseconds){
                        sendNotification("Timer is running out of time.");
                        reminder = "yes";

                    }
                }

            }
        }


        if(sessionManager.getKeyOverallocation().equalsIgnoreCase("yes")) {

            if (!sessionManager.getKeyAllocatedseconds().equalsIgnoreCase("allocated_seconds")) {
                int sessionseconds = Integer.parseInt(sessionManager.getKeyAllocatedseconds());


                if (overallocation.equalsIgnoreCase("no")) {
                    if (counter > sessionseconds) {
                        sendNotification("You have reached your allocated time.");
                        overallocation = "yes";
                    }

                }


            }

        }
        if(sessionManager.getKeyProximity().equalsIgnoreCase("yes")) {
            {
                Log.e("lat_timer",""+sessionManager.getKeyTimerlat());
                Log.e("long_timer",""+sessionManager.getKeyTimerlong());

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {

                    return;
                }


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (myLocation == null)
                {

                    return;


                }
                else {

                    longitude = myLocation.getLongitude();
                    latitude = myLocation.getLatitude();
                    Log.e("latitude",""+latitude);
                    Log.e("longitude",""+longitude);


                }



                if(sessionManager.getKeyTimerlat() !=null && sessionManager.getKeyTimerlong() !=null ){}
                Log.e("lat_current",""+latitude);
                Log.e("long_current",""+longitude);


                Log.e("lat_checkedin",""+sessionManager.getKeyTimerlat());
                Log.e("long_checkedin",""+sessionManager.getKeyTimerlong());

                Location startPoint = new Location("locationA");
  //                          startPoint.setLatitude(23.013054);
//                           startPoint.setLongitude(72.562515);

                startPoint.setLatitude(latitude);
                startPoint.setLongitude(longitude);

                Location endPoint = new Location("locationA");
                endPoint.setLatitude(Double.parseDouble(sessionManager.getKeyTimerlat()));
                endPoint.setLongitude(Double.parseDouble(sessionManager.getKeyTimerlong()));

                double distance = startPoint.distanceTo(endPoint);
                Log.e("distance", "" + distance);



                if(distance>5000){
                    if (proximity.equalsIgnoreCase("no")) {

                        sendNotification("Your timer is running and you are changing your location.");
                        proximity = "yes";


                    }
                }
            }
        }
        counter++;
    }
    private void sendNotification(String msg) {

        Log.e("message","message");

        Intent intent = new Intent(this, ActivitySplashScreen.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("HourStack")
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(counter, notificationBuilder.build());


    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.favicon : R.drawable.app_icon;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        // Perform your long running operations here.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        runtimer();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("de","de");
        timer.cancel();
        sessionManager.setKeyCurrentseconds(""+counter);



    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}

