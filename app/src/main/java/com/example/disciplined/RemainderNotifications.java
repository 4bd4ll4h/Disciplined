package com.example.disciplined;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.media.AudioAttributesCompat;

import java.net.URI;

public class RemainderNotifications extends Application {
    public static final String RemainderChannle="remainderChannle";
    public static final String RemainderChannleLow="remainderChannleLow";
    public static final String AlarmChannle="AlarmChannel";


    @Override
    public void onCreate() {
        super.onCreate();
        creatChannle();
    }
    public void creatChannle(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel remainderChannle=new NotificationChannel(
                    RemainderChannle
                    ,"important Remainders"
                    , NotificationManager.IMPORTANCE_HIGH
            );
            remainderChannle.setDescription("This channel showed the Important remainders notifications");
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            remainderChannle.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),audioAttributes);
            NotificationManager manager=getSystemService(NotificationManager.class);
            NotificationChannel remainderChannleLow=new NotificationChannel(
                    RemainderChannleLow
                    ,"low Remainders"
                    , NotificationManager.IMPORTANCE_LOW
            );
            remainderChannleLow.setDescription("This channel showed remainders notifications");
            NotificationChannel AlarmService=new NotificationChannel(
                    AlarmChannle
                    ,"Alarm Service"
                    , NotificationManager.IMPORTANCE_HIGH
            );
            AlarmService.setDescription("This channel showed the alarm service");

            manager.createNotificationChannel(remainderChannle);
            manager.createNotificationChannel(remainderChannleLow);
            manager.createNotificationChannel(AlarmService);
        }
    }
}
