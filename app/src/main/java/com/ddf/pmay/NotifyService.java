package com.ddf.pmay;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ddf.pmay.loginPOJO.loginBean;

import java.util.Timer;
import java.util.TimerTask;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NotifyService extends Service {

    Timer timer;
    //ConnectionDetector cd;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void doSomethingRepeatedly() {
        timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {



                try{


                    EasyLocationMod easyLocationMod = new EasyLocationMod(getApplicationContext());

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    final String lat = String.valueOf(l[0]);
                    final String lon = String.valueOf(l[1]);

                    Log.d("latitude" , lat);
                    Log.d("latitude" , lon);


                    bean b = (bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiInterface cr = retrofit.create(ApiInterface.class);





                    Call<loginBean> call = cr.track(SharePreferenceUtils.getInstance().getString("id") , lat , lon);


                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {



                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {

                        }
                    });


                }
                catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }
        }, 0, 1000 * 60 * 15);



    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        receive r = new receive();

        if (r!=null) {
            try {

                unregisterReceiver(r);
                r=null;

            }catch (Exception e)
            {

            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        doSomethingRepeatedly();

        return Service.START_STICKY;
    }


}
