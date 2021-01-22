package com.example.disciplined;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
import static com.example.disciplined.MainActivity.alarmList;
import static com.example.disciplined.MainActivity.am12check;
import static com.example.disciplined.MainActivity.remainderOfDay;
import static com.example.disciplined.MainActivity.setAlarmList;
import static com.example.disciplined.MainActivity.setDBalarm;
import static com.example.disciplined.MainActivity.setDbReminder;
import static com.example.disciplined.MainActivity.setRemainderOfDay;

//this class for renewing alarm task list every new day 24h and when the device is booted
public class am12 extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,service.class));
    }

  public static class service extends IntentService{
      public service() {
          super("service");
      }

      @Override
      public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
          return START_STICKY;
      }

      @Override
      public void onCreate() {
          super.onCreate();
          alarmList=new ArrayList<>();
          remainderOfDay=new ArrayList<>();
          setDbReminder(getApplication());
          setRemainderOfDay(getApplication());
          setDBalarm(getApplication());
          setAlarmList(getApplication());
          am12check (getApplication());
          Toast.makeText(getApplicationContext(), "loading Tasks and Reminders...", Toast.LENGTH_LONG).show();
          am12check(getApplicationContext());

      }
      public static void am12check(Context context){
          Calendar am=Calendar.getInstance();
          am.set(am.get(Calendar.YEAR),
                  am.get(Calendar.MONTH),
                  am.get(Calendar.DAY_OF_MONTH)+1,
                  0,0,0);
          AlarmManager am12=(AlarmManager) context.getSystemService(ALARM_SERVICE);
          Intent intent=new Intent(context,am12.class);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              intent.setAction(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
          }
          PendingIntent pendIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
          if (am12 != null) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                  am12.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, am.getTimeInMillis(), pendIntent);
              }else
                  am12.setExact(AlarmManager.RTC_WAKEUP, am.getTimeInMillis(), pendIntent);
          }

      }

      @Override
      protected void onHandleIntent(@Nullable Intent intent) {
         }
  }
}
