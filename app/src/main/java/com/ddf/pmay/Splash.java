package com.ddf.pmay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddf.pmay.loginPOJO.loginBean;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Splash extends AppCompatActivity {

    Timer t;
    private static final int PERMISSIONS_REQUEST = 100;

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progress = findViewById(R.id.progressBar5);


        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = false;
        if (service != null) {
            enabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        }


        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            finish();
        }


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            startApp();
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {

//If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//...then start the GPS tracking service//

            startApp();
        } else {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    void startApp()
    {
        EasyLocationMod easyLocationMod = new EasyLocationMod(this);

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
        double[] l = easyLocationMod.getLatLong();
        String lat = String.valueOf(l[0]);
        String lon = String.valueOf(l[1]);

        Log.d("latitude" , lat);
        Log.d("latitude" , lon);



        String ph = SharePreferenceUtils.getInstance().getString("phone");
        final String pa = SharePreferenceUtils.getInstance().getString("pass");


        if (ph.length() > 0 && pa.length() > 0)
        {
            progress.setVisibility(View.VISIBLE);

            bean b = (bean) getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build();

            ApiInterface cr = retrofit.create(ApiInterface.class);


            Call<loginBean> call = cr.login(ph, pa , lat , lon);

            call.enqueue(new Callback<loginBean>() {
                @Override
                public void onResponse(@NonNull Call<loginBean> call, @NonNull Response<loginBean> response) {

                    if (response.body() != null && response.body().getStatus().equals("1")) {


                        SharePreferenceUtils.getInstance().putString("id", response.body().getData().getUserId());
                        SharePreferenceUtils.getInstance().putString("name", response.body().getData().getUsername());
                        SharePreferenceUtils.getInstance().putString("email", response.body().getData().getEmail());
                        SharePreferenceUtils.getInstance().putString("phone", response.body().getData().getPhone());
                        SharePreferenceUtils.getInstance().putString("pass", pa);
                        SharePreferenceUtils.getInstance().putString("otp", response.body().getData().getOtp());
                        SharePreferenceUtils.getInstance().putString("aid", response.body().getData().getAreaId());

                        Toast.makeText(Splash.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();


                    } else {

                        if (response.body() != null) {
                            Toast.makeText(Splash.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NonNull Call<loginBean> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(Splash.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {

            t = new Timer();

            t.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(Splash.this , Login.class);
                    startActivity(intent);
                    finish();

                }
            } , 1500);
        }




    }

}