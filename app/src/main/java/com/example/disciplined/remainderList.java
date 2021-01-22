package com.example.disciplined;

import android.app.Application;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static com.example.disciplined.MainActivity.remainderOfDay;
import static com.example.disciplined.MainActivity.setDbReminder;
import static com.example.disciplined.MainActivity.setRemainderOfDay;
import static com.example.disciplined.RemainderNotifications.RemainderChannle;
import static com.example.disciplined.RemainderNotifications.RemainderChannleLow;

public class remainderList  extends IntentService {

    int currentIndex = 0;


    public remainderList() {
        super("remainderList");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        currentIndex = intent.getIntExtra("ID", 0);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDbReminder(getApplication());
        disciplined_repository repository=new disciplined_repository((getApplication()));
        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
        Intent mainActivty=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent mainAc=PendingIntent.getActivity(getApplicationContext(),0,mainActivty,0);
        Intent doneService=new Intent(this,doneSerice.class);
        doneService.putExtra("id",remainderOfDay.get(currentIndex).getTaskId());
        PendingIntent donePindding=PendingIntent.getService(getApplicationContext(),0,doneService,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification =new NotificationCompat.Builder(getApplicationContext(),
                repository.getTask(remainderOfDay.get(currentIndex).getTaskId()).getTask().getImportance()<=5?
                        RemainderChannleLow:RemainderChannle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remainderOfDay.get(currentIndex).getTitle())
                .setContentText(remainderOfDay.get(currentIndex).getDescription())
                .setPriority(repository.getTask(remainderOfDay.get(currentIndex).getTaskId()).getTask().getImportance()<=5?
                        Notification.PRIORITY_LOW:NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remainderOfDay.get(currentIndex).getDescription())
                        .setSummaryText(repository.getTask(remainderOfDay.get(currentIndex).getTaskId()).getTask().getTitle()))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(mainAc)
                .setAutoCancel(true)
                .addAction(R.drawable.done,getString(R.string.done),donePindding)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        managerCompat.notify(Integer.valueOf(String.valueOf( remainderOfDay.get(currentIndex).getTaskId())),notification);
        remainderOfDay.remove(currentIndex);
        setRemainderOfDay( getApplication());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
