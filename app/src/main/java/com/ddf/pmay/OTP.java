package com.ddf.pmay;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;

import java.util.Objects;

public class OTP extends AppCompatActivity {

    Toolbar toolbar;
    Pinview otpView;
    TextView resend;
    Button verify;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        toolbar = findViewById(R.id.toolbar);
        otpView = findViewById(R.id.otp_view);
        resend = findViewById(R.id.textView6);
        verify = findViewById(R.id.button4);
        progress = findViewById(R.id.progressBar2);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        verify.setClickable(false);
        verify.setEnabled(false);

        otpView.setPinViewEventListener(new Pinview.PinViewEventListener() {

            @Override
            public void onDataEntered(Pinview pinview, boolean b) {

                if (pinview.getValue().length() == 4)
                {
                    verify.setEnabled(true);
                    verify.setClickable(true);
                }
                else
                {
                    verify.setEnabled(false);
                    verify.setClickable(false);
                }


            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asdasd" , "clicked");

                Intent intent = new Intent(OTP.this , MainActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });


    }
}
