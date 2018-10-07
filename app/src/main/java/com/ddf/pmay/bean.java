package com.ddf.pmay;

import android.app.Application;

public class bean extends Application {

    public String BASE_URL = "http://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com/softcode/";

    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this);

    }

}