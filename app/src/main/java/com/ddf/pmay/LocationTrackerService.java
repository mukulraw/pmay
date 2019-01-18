package com.ddf.pmay;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.ddf.pmay.loginPOJO.loginBean;

import java.util.ArrayList;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LocationTrackerService extends Service {
    private LocationListener locationListener;
    private LocationManager locationManager;
    private IBinder binder = new LocalBinder();
    private boolean isTracking;
    private ArrayList<Location> trackedWaypoints;
    private String bestProvider;
    private Timer timer;

    String latitude, longitude;


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        startInForeground();

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, true);

        isTracking = false;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());

                Log.d("llll", latitude);

                Toast.makeText(LocationTrackerService.this, latitude , Toast.LENGTH_SHORT).show();

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

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    /*public void startTracking() {
        trackedWaypoints = new ArrayList<Location>();
        timer = new Timer();
        distance = new Distance();
        timer.start();
        isTracking = true;
        startInForeground();
    }*/

    private void startInForeground() {
        Intent notificationIntent = new Intent(this, Splash.class);
        String NOTIFICATION_CHANNEL_ID = "com.ddf.pmay";
        String channelName = "My Location Service";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TEST")
                .setContentText("HELLO")
                .setTicker("TICKER")
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            //channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(2 , notification);
    }

    public void stopTracking() {
        isTracking = false;
        stopForeground(true);
    }

    public boolean isTracking() {
        return isTracking;
    }

    public ArrayList<Location> getTrackedWaypoints() {
        return trackedWaypoints;
    }

    /*public Timer getTime() {
        timer.update();
        return timer;
    }

    public Distance getDistance() {
        return distance;
    }
*/
    public int getSteps() {
        return 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public LocationTrackerService getLocationTrackerInstance() {
            return LocationTrackerService.this;
        }
    }
}
