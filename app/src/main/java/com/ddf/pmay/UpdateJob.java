package com.ddf.pmay;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
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

                                Log.d("asdasd" , "validated");


                                progress.setVisibility(View.VISIBLE);

                                bean b = (bean) getApplicationContext();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.BASE_URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiInterface cr = retrofit.create(ApiInterface.class);

                                Call<String> call = cr.updateJob(getIntent().getStringExtra("id") , stg , "1" , sd , ed);

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
