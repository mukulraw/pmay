package com.ddf.pmay;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddf.pmay.loginPOJO.LocationService;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom;
    TextView toolbar;
    LinearLayout replace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.textView7);
        replace = findViewById(R.id.replace);


        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.notification:

                        toolbar.setText("Notifications");

                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Notifications frag = new Notifications();
                        ft.replace(R.id.replace, frag);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();
                        break;


                    case R.id.beneficiary:

                        toolbar.setText("My Beneficiary");

                        FragmentManager fm2 = getSupportFragmentManager();
                        FragmentTransaction ft2 = fm2.beginTransaction();
                        Beneficiary frag2 = new Beneficiary();
                        ft2.replace(R.id.replace, frag2);
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft2.commit();


                        break;


                    case R.id.settings:

                        toolbar.setText("Settings");

                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        Settings frag1 = new Settings();
                        ft1.replace(R.id.replace, frag1);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft1.commit();

                        break;
                }

                return true;
            }
        });


        toolbar.setText("My Beneficiary");

        bottom.setSelectedItemId(R.id.beneficiary);

        Intent pushIntent = new Intent(MainActivity.this, NotifyService.class);
        //Intent pushIntent = new Intent(context, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MainActivity.this.startForegroundService(pushIntent);
        }
        else
        {
            MainActivity.this.startService(pushIntent);
        }
    }
}
