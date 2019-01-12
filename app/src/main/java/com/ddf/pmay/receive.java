package com.ddf.pmay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class receive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("asdReceiver" , "Received");

        Intent pushIntent = new Intent(context, NotifyService.class);
        //Intent pushIntent = new Intent(context, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(pushIntent);
        }
        else
        {
            context.startService(pushIntent);
        }
    }
}
