package com.ddf.pmay;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ddf.pmay.loginPOJO.loginBean;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.ddf.pmay.bean.CHANNEL_ID;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class NotifyService extends Service {

    Timer timer;
    //ConnectionDetector cd;
    Context context;

    private FusedLocationProviderClient mFusedLocationClient;
    String latitude, longitude;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 60 * 1000 * 15;  /* 10 secs */
    private long FASTEST_INTERVAL = 60 * 1000 * 5; /* 2 sec */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


       // startLocationUpdates();

    }

    private void doSomethingRepeatedly() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {


                try {


                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }


                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // GPS location can be null if GPS is switched off
                                    if (location != null) {


                                        latitude = String.valueOf(location.getLatitude());
                                        longitude = String.valueOf(location.getLongitude());

                                        Toast.makeText(context, latitude, Toast.LENGTH_SHORT).show();

                                        Log.d("llll", latitude);

                                        bean b = (bean) getApplicationContext();

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(b.BASE_URL)
                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create()).build();

                                        ApiInterface cr = retrofit.create(ApiInterface.class);


                                        Call<loginBean> call = cr.track(SharePreferenceUtils.getInstance().getString("id"), latitude, longitude);


                                        call.enqueue(new Callback<loginBean>() {
                                            @Override
                                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<loginBean> call, Throwable t) {

                                            }
                                        });

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                    e.printStackTrace();
                                }
                            });


                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }
        }, 0, 1000 * 5);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        receive r = new receive();

        if (r != null) {
            try {

                unregisterReceiver(r);
                r = null;

            } catch (Exception e) {

            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //doSomethingRepeatedly();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("PMAY")
                .setContentText("App is running in the background")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        doSomethingRepeatedly();

        return Service.START_STICKY;
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        Log.d("llll", latitude);

        //Toast.makeText(context, latitude, Toast.LENGTH_SHORT).show();

        bean b = (bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface cr = retrofit.create(ApiInterface.class);


        Call<loginBean> call = cr.track(SharePreferenceUtils.getInstance().getString("id"), latitude, longitude);


        call.enqueue(new Callback<loginBean>() {
            @Override
            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

            }

            @Override
            public void onFailure(Call<loginBean> call, Throwable t) {

            }
        });


        /*String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();*/
        // You can now create a LatLng Object for use with maps
    }

}
