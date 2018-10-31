package com.ddf.pmay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddf.pmay.loginPOJO.loginBean;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginSignip extends AppCompatActivity {


    EditText phone, password;
    Button login;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signip);

        phone = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        login = findViewById(R.id.button3);
        progress = findViewById(R.id.progressBar);


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
        final String lat = String.valueOf(l[0]);
        final String lon = String.valueOf(l[1]);

        Log.d("latitude" , lat);
        Log.d("latitude" , lon);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pho = phone.getText().toString();
                final String pass = password.getText().toString();

                if (pho.length() == 10) {

                    if (pass.length() > 0) {
                        progress.setVisibility(View.VISIBLE);

                        bean b = (bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        ApiInterface cr = retrofit.create(ApiInterface.class);





                        Call<loginBean> call = cr.login(pho, pass , lat , lon);

                        call.enqueue(new Callback<loginBean>() {
                            @Override
                            public void onResponse(@NonNull Call<loginBean> call, @NonNull Response<loginBean> response) {

                                if (response.body() != null && response.body().getStatus().equals("1")) {


                                    SharePreferenceUtils.getInstance().putString("id", response.body().getData().getUserId());
                                    SharePreferenceUtils.getInstance().putString("name", response.body().getData().getUsername());
                                    SharePreferenceUtils.getInstance().putString("email", response.body().getData().getEmail());
                                    SharePreferenceUtils.getInstance().putString("phone", response.body().getData().getPhone());
                                    SharePreferenceUtils.getInstance().putString("pass", pass);
                                    SharePreferenceUtils.getInstance().putString("otp", response.body().getData().getOtp());
                                    SharePreferenceUtils.getInstance().putString("aid", response.body().getData().getAreaId());

                                    Toast.makeText(LoginSignip.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginSignip.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();


                                } else {

                                    if (response.body() != null) {
                                        Toast.makeText(LoginSignip.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(@NonNull Call<loginBean> call, @NonNull Throwable t) {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(LoginSignip.this, t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(LoginSignip.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(LoginSignip.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
