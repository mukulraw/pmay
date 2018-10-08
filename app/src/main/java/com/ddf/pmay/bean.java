package com.ddf.pmay;

import android.app.Application;
import android.content.Context;

public class bean extends Application {

    private static Context context;

    public String BASE_URL = "http://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com/pmay/";

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        FontsOverride.setDefaultFont(this);

    }

}