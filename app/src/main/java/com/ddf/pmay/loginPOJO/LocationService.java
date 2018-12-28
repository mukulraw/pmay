package com.ddf.pmay.loginPOJO;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ddf.pmay.ApiInterface;
import com.ddf.pmay.SharePreferenceUtils;
import com.ddf.pmay.bean;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = LocationService.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static final long INTERVAL = 1000 * 30 * 60;
    private static final long FASTEST_INTERVAL = 1000 * 60 * 30;
    private static final long MEDIUM_INTERVAL = 1000 * 60 * 15;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            startLocationUpdates();
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MEDIUM_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationService.this);
            Log.v(TAG, "Location update started ..............: ");
        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && LocationServices.FusedLocationApi != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            Log.v(TAG, "Location update stopped .......................");
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());

        bean b = (bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface cr = retrofit.create(ApiInterface.class);


        Call<loginBean> call = cr.track(SharePreferenceUtils.getInstance().getString("id"), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));


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
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        Log.v(TAG, "Service Stopped!");
    }

}



