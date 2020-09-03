package com.example.disciplined;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.disciplined.MainActivity.alarmList;
import static com.example.disciplined.MainActivity.am12check;
import static com.example.disciplined.MainActivity.remainderOfDay;
import static com.example.disciplined.MainActivity.setAlarmList;
import static com.example.disciplined.MainActivity.setDBalarm;
import static com.example.disciplined.MainActivity.setDbReminder;
import static com.example.disciplined.MainActivity.setRemainderOfDay;

public class am12 extends IntentService {


    public am12() {
        super("am12");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        alarmList=new ArrayList<>();
        remainderOfDay=new ArrayList<>();
        setDbReminder(getApplication());
        setRemainderOfDay(getApplication());
        setDBalarm(getApplication());
        setAlarmList(getApplication());
        am12check (getApplication());
        Toast.makeText(this, "loading Tasks and Reminders...", Toast.LENGTH_LONG).show();
        Log.i("loading3","loading Tasks and Reminders");
    }
}
