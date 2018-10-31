package com.ddf.pmay;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateJob extends AppCompatActivity {

    TextView name , fname , state , district , city , ward , pname;

    Button submit;
    ImageButton back;

    private int mYear, mMonth, mDay;

    TextView stage , payment , sdate , edate;

    ProgressBar progress;

    String stg = "" , pay = "Pending" , sd = "" , ed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job);

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


        name = findViewById(R.id.textView13);
        fname = findViewById(R.id.textView20);
        state = findViewById(R.id.textView22);
        district = findViewById(R.id.textView23);
        city = findViewById(R.id.textView24);
        ward = findViewById(R.id.textView21);
        pname = findViewById(R.id.textView30);
        submit = findViewById(R.id.button6);
        back = findViewById(R.id.imageButton);

        progress = findViewById(R.id.progressBar4);

        stage = findViewById(R.id.textView33);
        payment = findViewById(R.id.textView36);
        sdate = findViewById(R.id.textView39);
        edate = findViewById(R.id.textView42);

        name.setText(getIntent().getStringExtra("name") + " - " + getIntent().getStringExtra("bid"));
        fname.setText(getIntent().getStringExtra("fname"));
        state.setText(getIntent().getStringExtra("state"));
        district.setText(getIntent().getStringExtra("district"));
        city.setText(getIntent().getStringExtra("city"));
        pname.setText(getIntent().getStringExtra("pname"));
        ward.setText("Ward No. : " + getIntent().getStringExtra("ward"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(UpdateJob.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.stage_popup);
                dialog.show();

                RadioGroup rg = dialog.findViewById(R.id.group);
                RadioButton rb1 = dialog.findViewById(R.id.stg1);
                RadioButton rb2 = dialog.findViewById(R.id.stg2);
                RadioButton rb3 = dialog.findViewById(R.id.stg3);
                RadioButton rb4 = dialog.findViewById(R.id.stg4);


                String sta = getIntent().getStringExtra("stage");

                switch (sta) {
                    case "Stage 1":
                        rb1.setEnabled(false);
                        break;
                    case "Stage 2":
                        rb1.setEnabled(false);
                        rb2.setEnabled(false);
                        break;
                    case "Stage 3":
                        rb1.setEnabled(false);
                        rb2.setEnabled(false);
                        rb3.setEnabled(false);
                        break;
                    case "Stage 4":
                        rb1.setEnabled(false);
                        rb2.setEnabled(false);
                        rb3.setEnabled(false);
                        rb4.setEnabled(false);
                        stg = sta;
                        break;
                }


                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        Log.d("asdasd" , String.valueOf(i));
                        Log.d("asdasd" , String.valueOf(radioGroup.getCheckedRadioButtonId()));


                            RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                            stage.setText(rb.getText());
                            stg = rb.getText().toString();

                            dialog.dismiss();
                    }
                });

            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(UpdateJob.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.payment_popup);
                dialog.show();

                RadioGroup rg = dialog.findViewById(R.id.group);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                        payment.setText(rb.getText());
                        pay = rb.getText().toString();

                        dialog.dismiss();

                    }
                });

            }
        });

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateJob.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String d = dayOfMonth + "-" + String.valueOf(monthOfYear + 1) + "-" + year;

                                sdate.setText(d);
                                sd = d;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateJob.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String d = dayOfMonth + "-" + String.valueOf(monthOfYear + 1) + "-" + year;

                                edate.setText(d);
                                ed = d;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });



        if (getIntent().getStringExtra("stage").equals("Stage 4"))
        {
            submit.setText("FINISH");

        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stg.length() > 0)
                {
                    if (!pay.equals("Pending"))
                    {
                        if (sd.length() > 0)
                        {
                            if (ed.length() > 0)
                            {

                                if (getIntent().getStringExtra("stage").equals("Stage 4"))
                                {
                                    Log.d("asdasd" , "validated");


                                    progress.setVisibility(View.VISIBLE);

                                    bean b = (bean) getApplicationContext();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create()).build();

                                    ApiInterface cr = retrofit.create(ApiInterface.class);

                                    Call<String> call = cr.finishJob(getIntent().getStringExtra("id") , getIntent().getStringExtra("iid") , stg , "1" , sd , ed , lat , lon);

                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                            if (response.body() != null) {
                                                if (response.body().equals("1"))
                                                {
                                                    Toast.makeText(UpdateJob.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                                progress.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                                }
                                else
                                {
                                    Log.d("asdasd" , "validated");


                                    progress.setVisibility(View.VISIBLE);

                                    bean b = (bean) getApplicationContext();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create()).build();

                                    ApiInterface cr = retrofit.create(ApiInterface.class);

                                    Call<String> call = cr.updateJob(getIntent().getStringExtra("id") , getIntent().getStringExtra("iid") , stg , "1" , sd , ed , lat , lon);

                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                            if (response.body() != null) {
                                                if (response.body().equals("1"))
                                                {
                                                    Toast.makeText(UpdateJob.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                                progress.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                                }



                            }
                            else
                            {
                                Toast.makeText(UpdateJob.this, "Please select an approx. Completion Date", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(UpdateJob.this, "Please select a project Start Date", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(UpdateJob.this, "Payment has not received yet", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(UpdateJob.this, "Please select a stage", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
