package com.ddf.pmay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

                if (p.length() == 10)
                {


                    Intent intent = new Intent(LoginSignip.this , OTP.class);
                    startActivity(intent);


                }
                else
                {
                    Toast.makeText(LoginSignip.this , "Invalid phone number" , Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
