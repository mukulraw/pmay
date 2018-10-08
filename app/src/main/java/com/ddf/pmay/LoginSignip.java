package com.ddf.pmay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddf.pmay.loginPOJO.loginBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginSignip extends AppCompatActivity {


    EditText phone;
    Button login;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signip);

        phone = findViewById(R.id.editText);
        login = findViewById(R.id.button3);
        progress = findViewById(R.id.progressBar);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String p = phone.getText().toString();

                if (p.length() == 10) {


                    progress.setVisibility(View.VISIBLE);

                    bean b = (bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiInterface cr = retrofit.create(ApiInterface.class);


                    Call<loginBean> call = cr.login(p);

                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(@NonNull Call<loginBean> call, @NonNull Response<loginBean> response) {

                            if (response.body() != null && response.body().getStatus().equals("1")) {


                                SharePreferenceUtils.getInstance().putString("id", response.body().getData().getUserId());
                                SharePreferenceUtils.getInstance().putString("name", response.body().getData().getUsername());
                                SharePreferenceUtils.getInstance().putString("email", response.body().getData().getEmail());
                                SharePreferenceUtils.getInstance().putString("phone", response.body().getData().getPhone());
                                SharePreferenceUtils.getInstance().putString("otp", response.body().getData().getOtp());

                                Toast.makeText(LoginSignip.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginSignip.this, OTP.class);
                                startActivity(intent);


                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(@NonNull Call<loginBean> call, @NonNull Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                } else {
                    Toast.makeText(LoginSignip.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
