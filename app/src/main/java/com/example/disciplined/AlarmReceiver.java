package com.example.disciplined;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import static android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;


public class AlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, alarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.setAction(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        }
        intent.putExtra("ID", intent.getIntExtra("ID",0));
        context.startService(serviceIntent);
    }
}
