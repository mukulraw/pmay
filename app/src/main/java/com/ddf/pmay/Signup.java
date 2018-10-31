package com.ddf.pmay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signup extends AppCompatActivity {

    EditText name, phone, password;
    Button signup;
    ProgressBar progress;
    Spinner location;
    List<String> areasName;
    List<String> areasId;
    String areaId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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


        areasName = new ArrayList<>();
        areasId = new ArrayList<>();

        name = findViewById(R.id.editText5);
        phone = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        progress = findViewById(R.id.progressBar);
        signup = findViewById(R.id.button3);
        location = findViewById(R.id.spinner);

        progress.setVisibility(View.VISIBLE);

        bean b = (bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface cr = retrofit.create(ApiInterface.class);


        final Call<List<areasBean>> call = cr.areas();

        call.enqueue(new Callback<List<areasBean>>() {
            @Override
            public void onResponse(@NonNull Call<List<areasBean>> call, @NonNull Response<List<areasBean>> response) {


                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        areasName.add(response.body().get(i).getAreaName());
                        areasId.add(response.body().get(i).getAreaId());
                    }
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signup.this,
                        android.R.layout.simple_list_item_1, areasName);

                location.setAdapter(adapter);

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<areasBean>> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                areaId = areasId.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = name.getText().toString();
                String ph = phone.getText().toString();
                String pa = password.getText().toString();

                if (n.length() > 0) {
                    if (ph.length() == 10) {

                        if (pa.length() > 0) {

                            progress.setVisibility(View.VISIBLE);

                            bean b = (bean) getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create()).build();

                            ApiInterface cr = retrofit.create(ApiInterface.class);

                            Call<String> call1 = cr.signup(n, ph, pa, areaId , lat , lon);

                            call1.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                    Toast.makeText(Signup.this, response.body(), Toast.LENGTH_SHORT).show();

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            Toast.makeText(Signup.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Signup.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signup.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
